package com.matheusfilipefreitas.bpmn_runner_api.controller.common.exception;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.matheusfilipefreitas.bpmn_runner_api.common.exception.ApiErrors;
import com.matheusfilipefreitas.bpmn_runner_api.common.exception.interpreter.InterpreterException;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(InterpreterException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiErrors> handlerInterpreterException(InterpreterException exception) {
        List<String> errors = exception.getErrors() == null || exception.getErrors().isEmpty()
            ? List.of(exception.getMessage())
            : exception.getErrors();

        return ResponseEntity
            .status(BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new ApiErrors(errors));
    }
}
