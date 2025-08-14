package com.matheusfilipefreitas.bpmn_runner_api.common.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiErrors {

    @Getter
    private List<String> errors;

    public ApiErrors(String messageError) {
        this.errors = Arrays.asList(messageError);
    }

    public ApiErrors(List<String> errors) {
        this.errors = errors;
    }
}
