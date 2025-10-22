package com.matheusfilipefreitas.bpmn_runner_api.common.exception.notfound;

import java.util.Arrays;
import java.util.List;

public class NotFoundException extends RuntimeException {
    private final List<String> errors;

    public NotFoundException(List<String> errors) {
        super("Multiple errors occured!");
        this.errors = errors;
    }

    public NotFoundException(String message) {
        super(message);
        this.errors = Arrays.asList(message);
    }
}
