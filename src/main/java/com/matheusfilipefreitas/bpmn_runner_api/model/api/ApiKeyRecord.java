package com.matheusfilipefreitas.bpmn_runner_api.model.api;

import java.util.List;

import com.google.cloud.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor

public class ApiKeyRecord {
    private String keyId;
    private String key;
    private Timestamp createdAt;
    private Timestamp expiresAt;
    private long requestCount;
    private List<String> allowedOrigins;
}
