package com.lettre.knowledge.rag;


import com.lettre.knowledge.config.RagProperties;
import com.lettre.knowledge.dto.ChatMessageResponse;
import com.lettre.knowledge.dto.ChatSourceResponse;
import com.lettre.knowledge.embedding.EmbeddingService;
import com.lettre.knowledge.entity.Document;
import com.lettre.knowledge.entity.DocumentChunk;
import com.lettre.knowledge.exception.BusinessException;
import com.lettre.knowledge.llm.LlmService;
import com.lettre.knowledge.mapper.DocumentChunkMapper;
import com.lettre.knowledge.mapper.DocumentMapper;
import com.lettre.knowledge.vector.VectorSearchResult;
import com.lettre.knowledge.vector.VectorStore;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;



@Service
public class RagServiceImpl implements RagService {


    private final EmbeddingService embeddingService;

    private final VectorStore vectorStore;

    private final DocumentChunkMapper documentChunkMapper;

    private final DocumentMapper documentMapper;

    private final LlmService llmService;

    private final RagProperties ragProperties;


    public RagServiceImpl(
            EmbeddingService embeddingService,
            VectorStore vectorStore,
            DocumentChunkMapper documentChunkMapper,
            DocumentMapper documentMapper,
            LlmService llmService,
            RagProperties ragProperties
    ) {
        this.embeddingService = embeddingService;
        this.vectorStore = vectorStore;
        this.documentChunkMapper = documentChunkMapper;
        this.documentMapper = documentMapper;
        this.llmService = llmService;
        this.ragProperties = ragProperties;
    }


    @Override
    public ChatMessageResponse chat(String question, Long userId) {

        if (!StringUtils.hasText(question)) {

            throw new BusinessException(40016, "问题不能为空");

        }

        float[] queryVector = embeddingService.embed(question.trim());

        List<VectorSearchResult> searchResults = vectorStore.search(
                queryVector,
                userId,
                ragProperties.getTopK()
        );

        if (searchResults.isEmpty()) {

            throw new BusinessException(
                    40403,
                    "未找到相关知识，请先上传并完成文档向量化"
            );

        }

        List<ChatSourceResponse> sources = new ArrayList<>();

        StringBuilder contextBuilder = new StringBuilder();

        for (VectorSearchResult result : searchResults) {

            DocumentChunk chunk = documentChunkMapper.selectById(result.chunkId());

            if (chunk == null) {
                continue;
            }

            Document document = documentMapper.selectById(result.documentId());

            ChatSourceResponse source = new ChatSourceResponse();

            source.setDocumentId(result.documentId());
            source.setChunkId(result.chunkId());
            source.setChunkIndex(chunk.getChunkIndex());
            source.setFileName(document != null ? document.getFileName() : null);
            source.setContent(chunk.getContent());
            source.setScore(result.score());

            sources.add(source);

            contextBuilder
                    .append("[来源: ")
                    .append(source.getFileName())
                    .append(" #")
                    .append(source.getChunkIndex())
                    .append("]\n")
                    .append(chunk.getContent())
                    .append("\n\n");

        }

        if (sources.isEmpty()) {

            throw new BusinessException(40403, "未找到可用知识片段");

        }

        String prompt = buildPrompt(question.trim(), contextBuilder.toString());

        String answer = llmService.chat(prompt);

        ChatMessageResponse response = new ChatMessageResponse();

        response.setAnswer(answer);
        response.setSources(sources);

        return response;

    }


    private String buildPrompt(String question, String context) {

        return """
                你是企业知识库助手。请仅根据以下资料回答问题。
                如果资料中没有答案，请明确说明不知道，不要编造。

                【资料】
                %s
                【问题】
                %s
                """.formatted(context, question);

    }


}
