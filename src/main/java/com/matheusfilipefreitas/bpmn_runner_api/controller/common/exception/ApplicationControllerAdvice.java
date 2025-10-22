package com.matheusfilipefreitas.bpmn_runner_api.controller.common.exception;

import com.matheusfilipefreitas.bpmn_runner_api.common.exception.ApiErrors;
import com.matheusfilipefreitas.bpmn_runner_api.common.exception.interpreter.InterpreterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.List;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(InterpreterException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handlerInterpreterException (InterpreterException exception) {
        List<String> errors = exception.getErrors();
        return new ApiErrors(errors);
    }
}
