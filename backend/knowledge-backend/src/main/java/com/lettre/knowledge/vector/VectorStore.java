package com.lettre.knowledge.vector;


import java.util.List;



public interface VectorStore {


    String upsert(VectorRecord record);


    void delete(String vectorId);


    void deleteByDocumentId(Long documentId);


    List<VectorSearchResult> search(
            float[] queryVector,
            Long userId,
            int topK
    );


}
