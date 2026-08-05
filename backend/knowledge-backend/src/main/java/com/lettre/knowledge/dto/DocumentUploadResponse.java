package com.lettre.knowledge.dto;


import com.lettre.knowledge.enums.DocumentStatus;

import java.time.LocalDateTime;


public class DocumentUploadResponse {


    private Long id;

    private String fileName;

    private String fileType;

    private Long fileSize;

    private DocumentStatus status;

    private LocalDateTime createTime;

    private Integer chunkCount;


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


    public DocumentStatus getStatus() {
        return status;
    }


    public void setStatus(DocumentStatus status) {
        this.status = status;
    }


    public LocalDateTime getCreateTime() {
        return createTime;
    }


    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }


    public Integer getChunkCount() {
        return chunkCount;
    }


    public void setChunkCount(Integer chunkCount) {
        this.chunkCount = chunkCount;
    }


}
