package com.lettre.knowledge.dto;


import com.lettre.knowledge.enums.DocumentStatus;


public class DocumentChunkResponse {


    private Long id;

    private DocumentStatus status;

    private Integer chunkCount;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public DocumentStatus getStatus() {
        return status;
    }


    public void setStatus(DocumentStatus status) {
        this.status = status;
    }


    public Integer getChunkCount() {
        return chunkCount;
    }


    public void setChunkCount(Integer chunkCount) {
        this.chunkCount = chunkCount;
    }


}
