package com.matheusfilipefreitas.bpmn_runner_api.service.api;

import java.util.List;
import java.util.Optional;

import com.google.cloud.firestore.DocumentReference;
import com.matheusfilipefreitas.bpmn_runner_api.model.api.ApiKeyRecord;
import com.matheusfilipefreitas.bpmn_runner_api.model.api.ApiKeyWithOwner;

public interface ApiKeyService {
    ApiKeyRecord createApiKey(String uid, List<String> allowedOrigins, long daysValid);
    Optional<ApiKeyWithOwner> findByKey(String key);
    void incrementRequestCount(DocumentReference docRef);
    ApiKeyRecord renewKey(String uid, String keyId, long additionalDays);
}
