package com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.types;

public enum GatewayType {
    EXCLUSIVE,
    PARALLEL;

    public static GatewayType fromString(String value) {
        try {
            return GatewayType.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid gateway type: " + value);
        }
    }
}
