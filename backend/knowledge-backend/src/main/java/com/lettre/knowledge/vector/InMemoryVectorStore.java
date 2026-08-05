package com.lettre.knowledge.vector;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;



@Component
public class InMemoryVectorStore implements VectorStore {


    private final Map<String, VectorRecord> store = new ConcurrentHashMap<>();


    @Override
    public String upsert(VectorRecord record) {

        store.put(record.vectorId(), record);

        return record.vectorId();

    }


    @Override
    public void delete(String vectorId) {

        if (vectorId != null) {
            store.remove(vectorId);
        }

    }


    @Override
    public void deleteByDocumentId(Long documentId) {

        store.values().removeIf(record ->
                record.documentId().equals(documentId)
        );

    }


    @Override
    public List<VectorSearchResult> search(
            float[] queryVector,
            Long userId,
            int topK
    ) {

        List<VectorSearchResult> results = new ArrayList<>();

        for (VectorRecord record : store.values()) {

            if (!record.userId().equals(userId)) {
                continue;
            }

            double score = cosineSimilarity(queryVector, record.vector());

            VectorSearchResult item = new VectorSearchResult(
                    record.vectorId(),
                    record.chunkId(),
                    record.documentId(),
                    score,
                    null
            );

            results.add(item);

        }

        results.sort(Comparator.comparingDouble(VectorSearchResult::score).reversed());

        if (results.size() <= topK) {
            return results;
        }

        return results.subList(0, topK);

    }


    private double cosineSimilarity(float[] left, float[] right) {

        if (left.length != right.length) {
            return 0;
        }

        double dot = 0;
        double leftNorm = 0;
        double rightNorm = 0;

        for (int i = 0; i < left.length; i++) {

            dot += left[i] * right[i];
            leftNorm += left[i] * left[i];
            rightNorm += right[i] * right[i];

        }

        if (leftNorm == 0 || rightNorm == 0) {
            return 0;
        }

        return dot / (Math.sqrt(leftNorm) * Math.sqrt(rightNorm));

    }


}
