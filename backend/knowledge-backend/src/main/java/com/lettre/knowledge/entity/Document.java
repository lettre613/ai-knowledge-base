package com.lettre.knowledge.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lettre.knowledge.enums.DocumentStatus;

import java.time.LocalDateTime;


@TableName("document")
public class Document {


    @TableId(type = IdType.AUTO)
    private Long id;


    private Long userId;


    private String fileName;


    private String fileType;


    private Long fileSize;


    private String fileHash;


    @JsonIgnore
    private String storagePath;


    @JsonIgnore
    private String parsedContent;


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


    public Long getUserId() {
        return userId;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
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


    public String getStoragePath() {
        return storagePath;
    }


    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }


    public String getParsedContent() {
        return parsedContent;
    }


    public void setParsedContent(String parsedContent) {
        this.parsedContent = parsedContent;
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
