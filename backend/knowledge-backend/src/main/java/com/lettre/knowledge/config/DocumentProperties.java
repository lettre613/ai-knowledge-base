package com.lettre.knowledge.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;


@ConfigurationProperties(prefix = "document")
public class DocumentProperties {


    private String storagePath = "./data/documents";

    private int maxFileSizeMb = 10;

    private String allowedTypes = "pdf,docx,txt,md";

    private int chunkSize = 500;

    private int chunkOverlap = 50;

    private boolean autoProcess = true;


    public String getStoragePath() {
        return storagePath;
    }


    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }


    public int getMaxFileSizeMb() {
        return maxFileSizeMb;
    }


    public void setMaxFileSizeMb(int maxFileSizeMb) {
        this.maxFileSizeMb = maxFileSizeMb;
    }


    public String getAllowedTypes() {
        return allowedTypes;
    }


    public void setAllowedTypes(String allowedTypes) {
        this.allowedTypes = allowedTypes;
    }


    public List<String> allowedTypeList() {

        return Arrays.stream(allowedTypes.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(type -> !type.isEmpty())
                .toList();

    }


    public long maxFileSizeBytes() {

        return (long) maxFileSizeMb * 1024 * 1024;

    }


    public int getChunkSize() {
        return chunkSize;
    }


    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }


    public int getChunkOverlap() {
        return chunkOverlap;
    }


    public void setChunkOverlap(int chunkOverlap) {
        this.chunkOverlap = chunkOverlap;
    }


    public boolean isAutoProcess() {
        return autoProcess;
    }


    public void setAutoProcess(boolean autoProcess) {
        this.autoProcess = autoProcess;
    }


}
