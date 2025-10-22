package com.matheusfilipefreitas.bpmn_runner_api.common.listener;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;

import com.matheusfilipefreitas.bpmn_runner_api.common.exception.interpreter.InterpreterException;

public class SyntaxErrorListener extends BaseErrorListener {
    @Override
    public void syntaxError(
        Recognizer<? , ?> recognizer,
        Object offendingSymbol,
        int line,
        int charPositionInLine,
        String msg,
        RecognitionException e
    ) {
        msg = handleErrorTypes(msg, offendingSymbol);
        String error = String.format("syntax error in line %d-(char: %d) - %s", line, charPositionInLine, msg);
        throw new InterpreterException(error);
    }

    private String handleErrorTypes(String msg, Object offending) {
        String offendingText = extractText(offending);
        
        if (msg.contains("TaskType")) {
            return handleErrorTaskTypes(msg, offendingText);
        }
        if (msg.contains("GatewayType")) {
            return handleErrorGatewayTypes(msg, offendingText);
        }
        if (msg.contains("expecting 'start'")) {
            return handleStartEventError(msg);
        }
        return msg;
    }

    private String handleErrorTaskTypes(String msg, String offending) {
        return String.format("expected USER, AUTOMATED or MANUAL, but found '%s'", offending);
    }

    private String handleErrorGatewayTypes(String msg, String offending) {
        return String.format("expected EXCLUSIVE or PARALLEL, but found '%s'", offending);
    }

    private String handleStartEventError(String msg) {
        return String.format("expected 'start' at least once in the script");
    }

    private String extractText(Object offending) {
        if (offending instanceof Token token) {
            return token.getText();
        }
        return String.valueOf(offending);
    }
}