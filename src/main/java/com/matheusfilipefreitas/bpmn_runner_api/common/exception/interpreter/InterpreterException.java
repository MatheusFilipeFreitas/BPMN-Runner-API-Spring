package com.matheusfilipefreitas.bpmn_runner_api.common.exception.interpreter;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

@Getter
public class InterpreterException extends RuntimeException {
    private final List<String> errors;

    public InterpreterException(List<String> errors) {
        super("Multiple errors occured!");
        this.errors = errors;
    }

    public InterpreterException(String message) {
        super(message);
        this.errors = Arrays.asList(message);
    }
}
