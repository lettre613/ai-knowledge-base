package com.lettre.knowledge.vector;


public record VectorRecord(


        String vectorId,

        Long chunkId,

        Long documentId,

        Long userId,

        float[] vector


) {


}
