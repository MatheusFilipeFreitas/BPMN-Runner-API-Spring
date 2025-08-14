package com.matheusfilipefreitas.bpmn_runner_api.controller.common.exception;

import com.matheusfilipefreitas.bpmn_runner_api.common.exception.ApiErrors;
import com.matheusfilipefreitas.bpmn_runner_api.common.exception.interpreter.InterpreterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(InterpreterException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handlerInterpreterException (InterpreterException exception) {
        String messageError = exception.getMessage();
        return new ApiErrors(messageError);
    }
}
