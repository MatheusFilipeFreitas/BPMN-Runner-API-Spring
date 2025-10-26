package com.matheusfilipefreitas.bpmn_runner_api.model.api;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CacheEntry {
    private final List<String> allowedOrigins;
    private final Instant createdAt;

    public CacheEntry(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
        this.createdAt = Instant.now();
    }

    public boolean isExpired() {
        int cacheTtlMinutes = 5;
        return Instant.now().isAfter(createdAt.plusSeconds(cacheTtlMinutes * 60));
    }
}
