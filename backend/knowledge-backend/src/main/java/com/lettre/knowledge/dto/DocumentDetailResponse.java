package com.lettre.knowledge.dto;


import com.lettre.knowledge.enums.DocumentStatus;

import java.time.LocalDateTime;


public class DocumentDetailResponse {


    private Long id;

    private String fileName;

    private String fileType;

    private Long fileSize;

    private String fileHash;

    private DocumentStatus status;

    private String errorMessage;

    private Integer chunkCount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getFileName() {
        return fileName;
    }


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getFileType() {
        return fileType;
    }


    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


    public Long getFileSize() {
        return fileSize;
    }


    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }


    public String getFileHash() {
        return fileHash;
    }


    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }


    public DocumentStatus getStatus() {
        return status;
    }


    public void setStatus(DocumentStatus status) {
        this.status = status;
    }


    public String getErrorMessage() {
        return errorMessage;
    }


    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public Integer getChunkCount() {
        return chunkCount;
    }


    public void setChunkCount(Integer chunkCount) {
        this.chunkCount = chunkCount;
    }


    public LocalDateTime getCreateTime() {
        return createTime;
    }


    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }


    public LocalDateTime getUpdateTime() {
        return updateTime;
    }


    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }


}
