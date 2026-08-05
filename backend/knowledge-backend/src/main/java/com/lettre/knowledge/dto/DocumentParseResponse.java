package com.lettre.knowledge.dto;


import com.lettre.knowledge.enums.DocumentStatus;


public class DocumentParseResponse {


    private Long id;

    private DocumentStatus status;

    private Integer contentLength;


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


    public Integer getContentLength() {
        return contentLength;
    }


    public void setContentLength(Integer contentLength) {
        this.contentLength = contentLength;
    }


}
