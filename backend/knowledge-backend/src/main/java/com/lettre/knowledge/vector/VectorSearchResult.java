package com.lettre.knowledge.vector;


public record VectorSearchResult(


        String vectorId,

        Long chunkId,

        Long documentId,

        double score,

        String content


) {


}
