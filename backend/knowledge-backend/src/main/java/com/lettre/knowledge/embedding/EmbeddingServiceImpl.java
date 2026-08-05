package com.lettre.knowledge.embedding;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lettre.knowledge.config.EmbeddingProperties;
import com.lettre.knowledge.exception.BusinessException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;



@Service
public class EmbeddingServiceImpl implements EmbeddingService {


    private final EmbeddingProperties embeddingProperties;

    private final RestClient restClient;

    private final ObjectMapper objectMapper;


    public EmbeddingServiceImpl(
            EmbeddingProperties embeddingProperties,
            ObjectMapper objectMapper
    ) {
        this.embeddingProperties = embeddingProperties;
        this.objectMapper = objectMapper;
        this.restClient = RestClient.create();
    }


    @Override
    public float[] embed(String text) {

        if (!StringUtils.hasText(text)) {

            throw new BusinessException(40013, "Embedding 文本不能为空");

        }

        if (embeddingProperties.isMock()) {
            return mockEmbed(text);
        }

        return remoteEmbed(text);

    }


    private float[] mockEmbed(String text) {

        try {

            int dimensions = embeddingProperties.getDimensions();

            float[] vector = new float[dimensions];

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] seed = digest.digest(text.getBytes(StandardCharsets.UTF_8));

            for (int i = 0; i < dimensions; i++) {

                int seedIndex = i % seed.length;

                vector[i] = ((seed[seedIndex] & 0xFF) / 255.0f) * 2 - 1;

            }

            return normalize(vector);

        } catch (Exception e) {

            throw new BusinessException(50031, "Mock Embedding 生成失败");

        }

    }


    private float[] remoteEmbed(String text) {

        if (!StringUtils.hasText(embeddingProperties.getApiKey())) {

            throw new BusinessException(50032, "Embedding API Key 未配置");

        }

        try {

            Map<String, Object> requestBody = new HashMap<>();

            requestBody.put("model", embeddingProperties.getModel());
            requestBody.put("input", text);

            String responseBody = restClient.post()
                    .uri(embeddingProperties.getBaseUrl() + "/embeddings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + embeddingProperties.getApiKey())
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode embeddingNode = root.path("data").path(0).path("embedding");

            if (!embeddingNode.isArray() || embeddingNode.isEmpty()) {

                throw new BusinessException(50033, "Embedding API 返回数据无效");

            }

            float[] vector = new float[embeddingNode.size()];

            for (int i = 0; i < embeddingNode.size(); i++) {
                vector[i] = (float) embeddingNode.get(i).asDouble();
            }

            return vector;

        } catch (BusinessException e) {

            throw e;

        } catch (Exception e) {

            throw new BusinessException(50033, "Embedding API 调用失败");

        }

    }


    private float[] normalize(float[] vector) {

        double norm = 0;

        for (float value : vector) {
            norm += value * value;
        }

        norm = Math.sqrt(norm);

        if (norm == 0) {
            return vector;
        }

        float[] normalized = new float[vector.length];

        for (int i = 0; i < vector.length; i++) {
            normalized[i] = (float) (vector[i] / norm);
        }

        return normalized;

    }


}
