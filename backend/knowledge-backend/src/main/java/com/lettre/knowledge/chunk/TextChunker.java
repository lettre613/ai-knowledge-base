package com.lettre.knowledge.chunk;


import java.util.ArrayList;
import java.util.List;



public final class TextChunker {


    private TextChunker() {
    }


    public static List<String> split(
            String text,
            int chunkSize,
            int chunkOverlap
    ) {

        if (text == null || text.isBlank()) {
            return List.of();
        }

        if (chunkSize <= 0) {

            throw new IllegalArgumentException("chunkSize 必须大于 0");

        }

        if (chunkOverlap < 0 || chunkOverlap >= chunkSize) {

            throw new IllegalArgumentException("chunkOverlap 必须 >= 0 且小于 chunkSize");

        }

        String normalized = text.trim();

        List<String> chunks = new ArrayList<>();

        int start = 0;

        while (start < normalized.length()) {

            int end = Math.min(start + chunkSize, normalized.length());

            chunks.add(normalized.substring(start, end));

            if (end >= normalized.length()) {
                break;
            }

            start = end - chunkOverlap;

        }

        return chunks;

    }


    public static int estimateTokenCount(String content) {

        if (content == null || content.isEmpty()) {
            return 0;
        }

        return (content.length() + 3) / 4;

    }


}
