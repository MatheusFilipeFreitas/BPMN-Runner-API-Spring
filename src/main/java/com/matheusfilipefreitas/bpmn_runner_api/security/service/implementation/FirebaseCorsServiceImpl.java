package com.matheusfilipefreitas.bpmn_runner_api.security.service.implementation;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionGroup;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.matheusfilipefreitas.bpmn_runner_api.model.api.ApiKeyRecord;
import com.matheusfilipefreitas.bpmn_runner_api.model.api.CacheEntry;
import com.matheusfilipefreitas.bpmn_runner_api.security.service.FirebaseCorsService;

@Service
public class FirebaseCorsServiceImpl implements FirebaseCorsService {
    private final Firestore firestore;
    private final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

    public FirebaseCorsServiceImpl(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public List<String> getAllowedOriginsByApiKey(String apiKey) {
        CacheEntry entry = cache.get(apiKey);
        if (entry != null && !entry.isExpired()) {
            return entry.getAllowedOrigins();
        }

        try {
            CollectionGroup apiKeys = firestore.collectionGroup("apiKeys");
            QuerySnapshot snapshot = apiKeys.whereEqualTo("key", apiKey).get().get();

            if (snapshot.isEmpty()) {
                cache.put(apiKey, new CacheEntry(List.of()));
                return List.of();
            }

            DocumentSnapshot doc = snapshot.getDocuments().get(0);
            ApiKeyRecord record = doc.toObject(ApiKeyRecord.class);
            if (record.getExpiresAt().compareTo(Timestamp.now()) <= 0) {
                return null;
            }
            List<String> origins = record.getAllowedOrigins() != null ? record.getAllowedOrigins() : List.of();

            cache.put(apiKey, new CacheEntry(origins));
            return origins;

        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error while trying to retrive allowed origins ", e);
        }
    }

    @Override
    public boolean isOriginAllowed(String origin, String apiKey) {
        return getAllowedOriginsByApiKey(apiKey).stream()
                .anyMatch(o -> o.equalsIgnoreCase(origin));
    }

    @Override
    public boolean isApiKeyAllowed(String apiKey) {
        List<String> origins = getAllowedOriginsByApiKey(apiKey);
        return origins != null;
    }

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void clearExpiredCacheEntries() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
}


