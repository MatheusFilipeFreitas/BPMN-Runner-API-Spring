package com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.types;

public enum TaskType {
    AUTOMATED,
    USER,
    MANUAL;

    public static TaskType fromString(String value) {
        try {
            return TaskType.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid task type: " + value);
        }
    }
}
