package com.matheusfilipefreitas.bpmn_runner_api.model.dto;

import java.util.List;

public record CreateKeyRequest(
    List<String> allowedOrigins,
    long daysValid
) {
    public CreateKeyRequest {
        if (daysValid == 0) daysValid = 30;
    }
}
