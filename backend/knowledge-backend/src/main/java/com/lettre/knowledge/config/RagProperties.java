package com.lettre.knowledge.config;


import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "rag")
public class RagProperties {


    private int topK = 3;


    public int getTopK() {
        return topK;
    }


    public void setTopK(int topK) {
        this.topK = topK;
    }


}
