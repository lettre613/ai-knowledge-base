package com.lettre.knowledge.llm;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lettre.knowledge.config.LlmProperties;
import com.lettre.knowledge.exception.BusinessException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;



@Service
public class LlmServiceImpl implements LlmService {


    private final LlmProperties llmProperties;

    private final RestClient restClient;

    private final ObjectMapper objectMapper;


    public LlmServiceImpl(
            LlmProperties llmProperties,
            ObjectMapper objectMapper
    ) {
        this.llmProperties = llmProperties;
        this.objectMapper = objectMapper;
        this.restClient = RestClient.create();
    }


    @Override
    public String chat(String prompt) {

        if (!StringUtils.hasText(prompt)) {

            throw new BusinessException(40016, "LLM 提示词不能为空");

        }

        if (llmProperties.isMock()) {
            return mockChat(prompt);
        }

        return remoteChat(prompt);

    }


    private String mockChat(String prompt) {

        return "【Mock 回答】已根据知识库检索结果生成回复。\n\n"
                + "以下是基于检索资料整理的要点：\n"
                + extractContextPreview(prompt)
                + "\n\n（当前为 Mock 模式，配置 llm.mock=false 后将调用真实 LLM。）";

    }


    private String extractContextPreview(String prompt) {

        int contextStart = prompt.indexOf("【资料】");

        if (contextStart < 0) {
            return "未找到上下文片段。";
        }

        int questionStart = prompt.indexOf("【问题】");

        if (questionStart < 0) {
            return prompt.substring(contextStart).trim();
        }

        String context = prompt.substring(contextStart, questionStart).trim();

        if (context.length() <= 300) {
            return context;
        }

        return context.substring(0, 300) + "...";

    }


    private String remoteChat(String prompt) {

        if (!StringUtils.hasText(llmProperties.getApiKey())) {

            throw new BusinessException(50042, "LLM API Key 未配置");

        }

        try {

            Map<String, Object> systemMessage = new HashMap<>();

            systemMessage.put("role", "system");
            systemMessage.put(
                    "content",
                    "你是企业知识库助手。请仅根据用户提供的资料回答问题；如果资料中没有答案，请明确说明不知道，不要编造。"
            );

            Map<String, Object> userMessage = new HashMap<>();

            userMessage.put("role", "user");
            userMessage.put("content", prompt);

            Map<String, Object> requestBody = new HashMap<>();

            requestBody.put("model", llmProperties.getModel());
            requestBody.put("messages", List.of(systemMessage, userMessage));

            String responseBody = restClient.post()
                    .uri(llmProperties.getBaseUrl() + "/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + llmProperties.getApiKey())
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode contentNode = root.path("choices").path(0).path("message").path("content");

            if (!contentNode.isTextual() || !StringUtils.hasText(contentNode.asText())) {

                throw new BusinessException(50043, "LLM API 返回内容无效");

            }

            return contentNode.asText().trim();

        } catch (BusinessException e) {

            throw e;

        } catch (Exception e) {

            throw new BusinessException(50043, "LLM API 调用失败");

        }

    }


}
