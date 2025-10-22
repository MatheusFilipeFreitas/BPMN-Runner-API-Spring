package com.matheusfilipefreitas.bpmn_runner_api.service.api.implementation;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.matheusfilipefreitas.bpmn_runner_api.model.api.ApiKeyRecord;
import com.matheusfilipefreitas.bpmn_runner_api.model.api.ApiKeyWithOwner;
import com.matheusfilipefreitas.bpmn_runner_api.service.api.ApiKeyService;

@Service
public class ApiKeyServiceImpl implements ApiKeyService {
    private final Firestore firestore;

    public ApiKeyServiceImpl(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public ApiKeyRecord createApiKey(String uid, List<String> allowedOrigins, long daysValid) {
        try {
            String key = UUID.randomUUID().toString() + "-" + UUID.randomUUID().toString().substring(0,8);
            Instant now = Instant.now();
            Instant expiry = now.plusSeconds(daysValid * 24 * 3600);

            CollectionReference col = firestore.collection("users").document(uid).collection("apiKeys");
            DocumentReference docRef = col.document();

            ApiKeyRecord rec = new ApiKeyRecord(
                docRef.getId(),
                key,
                Timestamp.of(Date.from(now)),
                Timestamp.of(Date.from(expiry)),
                0,
                allowedOrigins);

            Map<String, Object> data = new HashMap<>();
            data.put("keyId", docRef.getId());
            data.put("key", key);
            data.put("createdAt", Date.from(now));
            data.put("expiresAt", Date.from(expiry));
            data.put("requestCount", 0L);
            data.put("allowedOrigins", allowedOrigins);

            docRef.set(data).get();

            return rec;
        } catch (Exception e) {
            throw new RuntimeException("Could not create api key to user");
        }
    }

    @Override
    public Optional<ApiKeyWithOwner> findByKey(String key) {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collectionGroup("apiKeys")
                .whereEqualTo("key", key)
                .get();

            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            if (docs.isEmpty()) {
                return Optional.empty();
            }

            QueryDocumentSnapshot doc = docs.get(0);
            ApiKeyRecord docData = doc.toObject(ApiKeyRecord.class);

            String documentId = doc.getId();

            ApiKeyRecord rec = new ApiKeyRecord(
                documentId,
                docData.getKey(),
                docData.getCreatedAt(),
                docData.getExpiresAt(),
                docData.getRequestCount(),
                docData.getAllowedOrigins()
            );

            DocumentReference docRef = doc.getReference();
            CollectionReference apiKeysCollection = docRef.getParent();
            DocumentReference userDocRef = apiKeysCollection.getParent();
            final String ownerUid = userDocRef.getId();

            return Optional.of(new ApiKeyWithOwner(rec, ownerUid, docRef));
        } catch (Exception ex) {
            throw new RuntimeException("Error while trying to get key from user");
        }
    }

    @Override
    public void incrementRequestCount(DocumentReference docRef) {
        docRef.update("requestCount", FieldValue.increment(1));
    }

    @Override
    public ApiKeyRecord renewKey(String uid, String keyId, long additionalDays) {
        try {
            DocumentReference ref = firestore.collection("users").document(uid).collection("apiKeys").document(keyId);
            DocumentSnapshot snap = ref.get().get();
            if (!snap.exists()) throw new RuntimeException("Key not found");
            ApiKeyRecord dataFromDoc = snap.toObject(ApiKeyRecord.class);
            Instant newExpiry = Instant.now().plusSeconds(additionalDays * 24 * 3600);
            ref.update("expiresAt", newExpiry.toString()).get();

            ApiKeyRecord rec = new ApiKeyRecord(
                dataFromDoc.getKeyId(),
                dataFromDoc.getKey(),
                dataFromDoc.getCreatedAt(),
                Timestamp.of(Date.from(newExpiry)),
                dataFromDoc.getRequestCount(),
                dataFromDoc.getAllowedOrigins()
            );

            return rec;
        } catch (Exception ex) {
            throw new RuntimeException("Could not renew user key");
        }
    }
}
