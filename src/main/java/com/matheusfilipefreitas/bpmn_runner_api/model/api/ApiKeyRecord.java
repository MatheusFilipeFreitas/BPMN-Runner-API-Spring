package com.matheusfilipefreitas.bpmn_runner_api.model.api;

import java.time.Instant;
import java.util.List;

public record ApiKeyRecord(
    String keyId, 
    String key,
    Instant createdAt,
    Instant expiresAt,
    long requestCount,
    List<String> allowedOrigins) {
}
