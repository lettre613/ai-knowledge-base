package com.lettre.knowledge.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lettre.knowledge.chunk.TextChunker;
import com.lettre.knowledge.config.DocumentProperties;
import com.lettre.knowledge.converter.DocumentConverter;
import com.lettre.knowledge.dto.DocumentEmbedResponse;
import com.lettre.knowledge.dto.DocumentChunkResponse;
import com.lettre.knowledge.dto.DocumentDetailResponse;
import com.lettre.knowledge.dto.DocumentListItemResponse;
import com.lettre.knowledge.dto.DocumentParseResponse;
import com.lettre.knowledge.dto.DocumentUploadResponse;
import com.lettre.knowledge.entity.Document;
import com.lettre.knowledge.entity.DocumentChunk;
import com.lettre.knowledge.embedding.EmbeddingService;
import com.lettre.knowledge.enums.DocumentStatus;
import com.lettre.knowledge.exception.BusinessException;
import com.lettre.knowledge.mapper.DocumentChunkMapper;
import com.lettre.knowledge.mapper.DocumentMapper;
import com.lettre.knowledge.parser.DocumentParser;
import com.lettre.knowledge.parser.DocumentParserFactory;
import com.lettre.knowledge.service.DocumentService;
import com.lettre.knowledge.storage.LocalDocumentStorage;
import com.lettre.knowledge.storage.StorageResult;
import com.lettre.knowledge.vector.VectorRecord;
import com.lettre.knowledge.vector.VectorStore;

import java.nio.file.Path;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;



@Service
public class DocumentServiceImpl implements DocumentService {


    private final DocumentMapper documentMapper;

    private final DocumentChunkMapper documentChunkMapper;

    private final DocumentProperties documentProperties;

    private final LocalDocumentStorage localDocumentStorage;

    private final DocumentParserFactory documentParserFactory;

    private final EmbeddingService embeddingService;

    private final VectorStore vectorStore;


    public DocumentServiceImpl(
            DocumentMapper documentMapper,
            DocumentChunkMapper documentChunkMapper,
            DocumentProperties documentProperties,
            LocalDocumentStorage localDocumentStorage,
            DocumentParserFactory documentParserFactory,
            EmbeddingService embeddingService,
            VectorStore vectorStore
    ) {
        this.documentMapper = documentMapper;
        this.documentChunkMapper = documentChunkMapper;
        this.documentProperties = documentProperties;
        this.localDocumentStorage = localDocumentStorage;
        this.documentParserFactory = documentParserFactory;
        this.embeddingService = embeddingService;
        this.vectorStore = vectorStore;
    }


    @Override
    public DocumentUploadResponse upload(MultipartFile file, Long userId) {

        validateUploadFile(file);

        String extension = extractFileExtension(file.getOriginalFilename());
        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename()
        );

        StorageResult storageResult = localDocumentStorage.store(
                file,
                userId,
                extension
        );

        if (isDuplicateFile(userId, storageResult.fileHash())) {

            localDocumentStorage.delete(storageResult.storagePath());

            throw new BusinessException(40006, "该文件已上传，请勿重复上传");

        }

        Document document = buildDocumentEntity(
                userId,
                originalFilename,
                extension,
                file.getSize(),
                storageResult
        );

        try {

            documentMapper.insert(document);

        } catch (RuntimeException e) {

            localDocumentStorage.delete(storageResult.storagePath());

            throw e;

        }

        if (documentProperties.isAutoProcess()) {

            processPipeline(document.getId(), userId);

            document = documentMapper.selectById(document.getId());

        }

        return DocumentConverter.toUploadResponse(document);

    }


    private void processPipeline(Long documentId, Long userId) {

        parse(documentId, userId);

        chunk(documentId, userId);

        embed(documentId, userId);

    }


    @Override
    public Page<DocumentListItemResponse> page(Long userId, long page, long size) {

        Page<Document> entityPage = new Page<>(page, size);

        LambdaQueryWrapper<Document> wrapper = new LambdaQueryWrapper<Document>()
                .eq(Document::getUserId, userId)
                .orderByDesc(Document::getCreateTime);

        documentMapper.selectPage(entityPage, wrapper);

        Page<DocumentListItemResponse> result = new Page<>(
                entityPage.getCurrent(),
                entityPage.getSize(),
                entityPage.getTotal()
        );

        List<DocumentListItemResponse> records = entityPage.getRecords().stream()
                .map(DocumentConverter::toListItem)
                .toList();

        result.setRecords(records);

        return result;

    }


    @Override
    public DocumentDetailResponse getById(Long id, Long userId) {

        Document document = getOwnedDocument(id, userId);

        return DocumentConverter.toDetail(document);

    }


    @Override
    public DocumentParseResponse parse(Long id, Long userId) {

        Document document = getOwnedDocument(id, userId);

        assertParseable(document);

        markParsing(document);

        try {

            Path filePath = localDocumentStorage.resolveAbsolutePath(
                    document.getStoragePath()
            );

            DocumentParser parser = documentParserFactory.getParser(
                    document.getFileType()
            );

            String parsedContent = parser.parse(filePath).trim();

            if (parsedContent.isEmpty()) {

                throw new BusinessException(40008, "文档解析结果为空");

            }

            markParsed(document, parsedContent);

            DocumentParseResponse response = new DocumentParseResponse();

            response.setId(document.getId());
            response.setStatus(document.getStatus());
            response.setContentLength(parsedContent.length());

            return response;

        } catch (BusinessException e) {

            markFailed(document, e.getMessage());

            throw e;

        } catch (RuntimeException e) {

            markFailed(document, "文档解析失败");

            throw new BusinessException(50010, "文档解析失败");

        }

    }


    @Override
    @Transactional
    public DocumentChunkResponse chunk(Long id, Long userId) {

        Document document = getOwnedDocument(id, userId);

        assertChunkable(document);

        markChunking(document);

        try {

            String parsedContent = document.getParsedContent();

            if (!StringUtils.hasText(parsedContent)) {

                throw new BusinessException(40011, "文档尚未解析，无法切分");

            }

            List<String> chunkTexts = TextChunker.split(
                    parsedContent,
                    documentProperties.getChunkSize(),
                    documentProperties.getChunkOverlap()
            );

            if (chunkTexts.isEmpty()) {

                throw new BusinessException(40012, "文档切分结果为空");

            }

            removeExistingChunks(document.getId());

            saveChunks(document.getId(), chunkTexts);

            markChunked(document, chunkTexts.size());

            DocumentChunkResponse response = new DocumentChunkResponse();

            response.setId(document.getId());
            response.setStatus(document.getStatus());
            response.setChunkCount(document.getChunkCount());

            return response;

        } catch (BusinessException e) {

            markFailed(document, e.getMessage());

            throw e;

        } catch (RuntimeException e) {

            markFailed(document, "文档切分失败");

            throw new BusinessException(50020, "文档切分失败");

        }

    }


    @Override
    @Transactional
    public DocumentEmbedResponse embed(Long id, Long userId) {

        Document document = getOwnedDocument(id, userId);

        assertEmbeddable(document);

        markEmbedding(document);

        try {

            List<DocumentChunk> chunks = listChunks(document.getId());

            if (chunks.isEmpty()) {

                throw new BusinessException(40014, "文档尚未切分，无法向量化");

            }

            vectorStore.deleteByDocumentId(document.getId());

            int embeddedCount = 0;

            for (DocumentChunk chunk : chunks) {

                float[] vector = embeddingService.embed(chunk.getContent());

                String vectorId = buildVectorId(chunk.getId());

                VectorRecord record = new VectorRecord(
                        vectorId,
                        chunk.getId(),
                        document.getId(),
                        userId,
                        vector
                );

                vectorStore.upsert(record);

                chunk.setVectorId(vectorId);

                documentChunkMapper.updateById(chunk);

                embeddedCount++;

            }

            markCompleted(document);

            DocumentEmbedResponse response = new DocumentEmbedResponse();

            response.setId(document.getId());
            response.setStatus(document.getStatus());
            response.setChunkCount(document.getChunkCount());
            response.setEmbeddedCount(embeddedCount);

            return response;

        } catch (BusinessException e) {

            vectorStore.deleteByDocumentId(document.getId());

            clearChunkVectorIds(document.getId());

            markFailed(document, e.getMessage());

            throw e;

        } catch (RuntimeException e) {

            vectorStore.deleteByDocumentId(document.getId());

            clearChunkVectorIds(document.getId());

            markFailed(document, "文档向量化失败");

            throw new BusinessException(50030, "文档向量化失败");

        }

    }


    private void assertEmbeddable(Document document) {

        DocumentStatus status = document.getStatus();

        if (status != DocumentStatus.CHUNKED && status != DocumentStatus.FAILED) {

            throw new BusinessException(
                    40015,
                    "当前文档状态不允许向量化: " + status
            );

        }

    }


    private void markEmbedding(Document document) {

        document.setStatus(DocumentStatus.EMBEDDING);
        document.setErrorMessage(null);
        document.setUpdateTime(LocalDateTime.now());

        documentMapper.updateById(document);

    }


    private void markCompleted(Document document) {

        document.setStatus(DocumentStatus.COMPLETED);
        document.setErrorMessage(null);
        document.setUpdateTime(LocalDateTime.now());

        documentMapper.updateById(document);

    }


    private List<DocumentChunk> listChunks(Long documentId) {

        return documentChunkMapper.selectList(
                new LambdaQueryWrapper<DocumentChunk>()
                        .eq(DocumentChunk::getDocumentId, documentId)
                        .orderByAsc(DocumentChunk::getChunkIndex)
        );

    }


    private void clearChunkVectorIds(Long documentId) {

        List<DocumentChunk> chunks = listChunks(documentId);

        for (DocumentChunk chunk : chunks) {

            chunk.setVectorId(null);

            documentChunkMapper.updateById(chunk);

        }

    }


    private String buildVectorId(Long chunkId) {

        return "v-" + chunkId;

    }


    private void assertChunkable(Document document) {

        DocumentStatus status = document.getStatus();

        if (status != DocumentStatus.PARSED && status != DocumentStatus.FAILED) {

            throw new BusinessException(
                    40010,
                    "当前文档状态不允许切分: " + status
            );

        }

    }


    private void markChunking(Document document) {

        document.setStatus(DocumentStatus.CHUNKING);
        document.setErrorMessage(null);
        document.setUpdateTime(LocalDateTime.now());

        documentMapper.updateById(document);

    }


    private void markChunked(Document document, int chunkCount) {

        document.setStatus(DocumentStatus.CHUNKED);
        document.setChunkCount(chunkCount);
        document.setErrorMessage(null);
        document.setUpdateTime(LocalDateTime.now());

        documentMapper.updateById(document);

    }


    private void removeExistingChunks(Long documentId) {

        documentChunkMapper.delete(
                new LambdaQueryWrapper<DocumentChunk>()
                        .eq(DocumentChunk::getDocumentId, documentId)
        );

    }


    private void saveChunks(Long documentId, List<String> chunkTexts) {

        LocalDateTime now = LocalDateTime.now();

        for (int index = 0; index < chunkTexts.size(); index++) {

            String content = chunkTexts.get(index);

            DocumentChunk chunk = new DocumentChunk();

            chunk.setDocumentId(documentId);
            chunk.setChunkIndex(index);
            chunk.setContent(content);
            chunk.setTokenCount(TextChunker.estimateTokenCount(content));
            chunk.setCreateTime(now);

            documentChunkMapper.insert(chunk);

        }

    }


    private void assertParseable(Document document) {

        DocumentStatus status = document.getStatus();

        if (status != DocumentStatus.UPLOADED && status != DocumentStatus.FAILED) {

            throw new BusinessException(
                    40009,
                    "当前文档状态不允许解析: " + status
            );

        }

    }


    private void markParsing(Document document) {

        document.setStatus(DocumentStatus.PARSING);
        document.setErrorMessage(null);
        document.setUpdateTime(LocalDateTime.now());

        documentMapper.updateById(document);

    }


    private void markParsed(Document document, String parsedContent) {

        document.setParsedContent(parsedContent);
        document.setStatus(DocumentStatus.PARSED);
        document.setErrorMessage(null);
        document.setUpdateTime(LocalDateTime.now());

        documentMapper.updateById(document);

    }


    private void markFailed(Document document, String errorMessage) {

        document.setStatus(DocumentStatus.FAILED);
        document.setErrorMessage(errorMessage);
        document.setUpdateTime(LocalDateTime.now());

        documentMapper.updateById(document);

    }


    private Document getOwnedDocument(Long id, Long userId) {

        Document document = documentMapper.selectById(id);

        if (document == null) {

            throw new BusinessException(40401, "文档不存在");

        }

        if (!document.getUserId().equals(userId)) {

            throw new BusinessException(40301, "无权访问该文档");

        }

        return document;

    }


    private boolean isDuplicateFile(Long userId, String fileHash) {

        Document existing = documentMapper.selectOne(
                new LambdaQueryWrapper<Document>()
                        .eq(Document::getUserId, userId)
                        .eq(Document::getFileHash, fileHash)
        );

        return existing != null;

    }


    private Document buildDocumentEntity(
            Long userId,
            String originalFilename,
            String extension,
            long fileSize,
            StorageResult storageResult
    ) {

        LocalDateTime now = LocalDateTime.now();

        Document document = new Document();

        document.setUserId(userId);
        document.setFileName(originalFilename);
        document.setFileType(extension);
        document.setFileSize(fileSize);
        document.setFileHash(storageResult.fileHash());
        document.setStoragePath(storageResult.storagePath());
        document.setStatus(DocumentStatus.UPLOADED);
        document.setChunkCount(0);
        document.setCreateTime(now);
        document.setUpdateTime(now);

        return document;

    }


    private void validateUploadFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {

            throw new BusinessException(40003, "上传文件不能为空");

        }

        if (file.getSize() > documentProperties.maxFileSizeBytes()) {

            throw new BusinessException(
                    40004,
                    "文件大小不能超过 " + documentProperties.getMaxFileSizeMb() + "MB"
            );

        }

        String extension = extractFileExtension(file.getOriginalFilename());

        if (!documentProperties.allowedTypeList().contains(extension)) {

            throw new BusinessException(
                    40005,
                    "不支持的文件类型，允许: " + documentProperties.getAllowedTypes()
            );

        }

    }


    private String extractFileExtension(String originalFilename) {

        if (!StringUtils.hasText(originalFilename)) {

            throw new BusinessException(40003, "文件名无效");

        }

        int dotIndex = originalFilename.lastIndexOf('.');

        if (dotIndex < 0 || dotIndex == originalFilename.length() - 1) {

            throw new BusinessException(40003, "文件名缺少有效扩展名");

        }

        return originalFilename.substring(dotIndex + 1).toLowerCase(Locale.ROOT);

    }


}
