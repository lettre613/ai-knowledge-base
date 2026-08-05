package com.lettre.knowledge.dto;


public class ChatSourceResponse {


    private Long documentId;

    private Long chunkId;

    private Integer chunkIndex;

    private String fileName;

    private String content;

    private Double score;


    public Long getDocumentId() {
        return documentId;
    }


    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }


    public Long getChunkId() {
        return chunkId;
    }


    public void setChunkId(Long chunkId) {
        this.chunkId = chunkId;
    }


    public Integer getChunkIndex() {
        return chunkIndex;
    }


    public void setChunkIndex(Integer chunkIndex) {
        this.chunkIndex = chunkIndex;
    }


    public String getFileName() {
        return fileName;
    }


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public Double getScore() {
        return score;
    }


    public void setScore(Double score) {
        this.score = score;
    }


}
