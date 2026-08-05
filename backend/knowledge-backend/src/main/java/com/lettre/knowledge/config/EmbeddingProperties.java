package com.lettre.knowledge.config;


import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "embedding")
public class EmbeddingProperties {


    private boolean mock = true;

    private String apiKey = "";

    private String baseUrl = "https://api.openai.com/v1";

    private String model = "text-embedding-3-small";

    private int dimensions = 384;


    public boolean isMock() {
        return mock;
    }


    public void setMock(boolean mock) {
        this.mock = mock;
    }


    public String getApiKey() {
        return apiKey;
    }


    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }


    public String getBaseUrl() {
        return baseUrl;
    }


    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }


    public String getModel() {
        return model;
    }


    public void setModel(String model) {
        this.model = model;
    }


    public int getDimensions() {
        return dimensions;
    }


    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }


}
