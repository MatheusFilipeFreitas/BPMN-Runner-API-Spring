package com.matheusfilipefreitas.bpmn_runner_api.common.exception.forbidden;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException {
        private final List<String> errors;

    public ForbiddenException(List<String> errors) {
        super("Multiple errors occured!");
        this.errors = errors;
    }

    public ForbiddenException(String message) {
        super(message);
        this.errors = Arrays.asList(message);
    }
}
