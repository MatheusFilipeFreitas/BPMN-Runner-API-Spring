package com.matheusfilipefreitas.bpmn_runner_api.security.service;

import java.util.List;

public interface FirebaseCorsService {
    List<String> getAllowedOriginsByApiKey(String apiKey);
    boolean isOriginAllowed(String origin, String apiKey);
    boolean isApiKeyAllowed(String apiKey);
}
