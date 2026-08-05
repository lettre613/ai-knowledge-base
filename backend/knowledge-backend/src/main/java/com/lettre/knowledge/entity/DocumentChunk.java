package com.lettre.knowledge.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;


@TableName("document_chunk")
public class DocumentChunk {


    @TableId(type = IdType.AUTO)
    private Long id;


    private Long documentId;


    private Integer chunkIndex;


    private String content;


    private Integer tokenCount;


    private String vectorId;


    private LocalDateTime createTime;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getDocumentId() {
        return documentId;
    }


    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }


    public Integer getChunkIndex() {
        return chunkIndex;
    }


    public void setChunkIndex(Integer chunkIndex) {
        this.chunkIndex = chunkIndex;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public Integer getTokenCount() {
        return tokenCount;
    }


    public void setTokenCount(Integer tokenCount) {
        this.tokenCount = tokenCount;
    }


    public String getVectorId() {
        return vectorId;
    }


    public void setVectorId(String vectorId) {
        this.vectorId = vectorId;
    }


    public LocalDateTime getCreateTime() {
        return createTime;
    }


    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }


}
