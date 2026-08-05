package com.lettre.knowledge.converter;


import com.lettre.knowledge.dto.DocumentDetailResponse;
import com.lettre.knowledge.dto.DocumentListItemResponse;
import com.lettre.knowledge.dto.DocumentUploadResponse;
import com.lettre.knowledge.entity.Document;



public final class DocumentConverter {


    private DocumentConverter() {
    }


    public static DocumentUploadResponse toUploadResponse(Document document) {

        DocumentUploadResponse response = new DocumentUploadResponse();

        response.setId(document.getId());
        response.setFileName(document.getFileName());
        response.setFileType(document.getFileType());
        response.setFileSize(document.getFileSize());
        response.setStatus(document.getStatus());
        response.setCreateTime(document.getCreateTime());
        response.setChunkCount(document.getChunkCount());

        return response;

    }


    public static DocumentListItemResponse toListItem(Document document) {

        DocumentListItemResponse response = new DocumentListItemResponse();

        response.setId(document.getId());
        response.setFileName(document.getFileName());
        response.setFileType(document.getFileType());
        response.setFileSize(document.getFileSize());
        response.setStatus(document.getStatus());
        response.setChunkCount(document.getChunkCount());
        response.setCreateTime(document.getCreateTime());
        response.setUpdateTime(document.getUpdateTime());

        return response;

    }


    public static DocumentDetailResponse toDetail(Document document) {

        DocumentDetailResponse response = new DocumentDetailResponse();

        response.setId(document.getId());
        response.setFileName(document.getFileName());
        response.setFileType(document.getFileType());
        response.setFileSize(document.getFileSize());
        response.setFileHash(document.getFileHash());
        response.setStatus(document.getStatus());
        response.setErrorMessage(document.getErrorMessage());
        response.setChunkCount(document.getChunkCount());
        response.setCreateTime(document.getCreateTime());
        response.setUpdateTime(document.getUpdateTime());

        return response;

    }


}
