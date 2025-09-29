package com.matheusfilipefreitas.bpmn_runner_api.model.script.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.ElementInfo;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.types.ElementType;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.grammar.ProcessParser;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.grammar.ProcessParserBaseListener;

public class IdCollectorListener extends ProcessParserBaseListener {
    private final List<ElementInfo> elements = new ArrayList<>();
    private final Stack<String> processContextStack = new Stack<>();

    @Override
    public void exitPool(ProcessParser.PoolContext ctx) {
        String id = ctx.ID().getText();
        String label = stripQuotes(ctx.STRING().getText());
        elements.add(new ElementInfo(id, ElementType.POOL, label, (String) null));
    }

    @Override
    public void enterProcess(ProcessParser.ProcessContext ctx) {
        String processId = ctx.ID().getText();
        processContextStack.push(processId);
    }

    @Override
    public void exitProcess(ProcessParser.ProcessContext ctx) {
        String id = ctx.ID().getText();
        String label = stripQuotes(ctx.STRING().getText());
        elements.add(new ElementInfo(id, ElementType.PROCESS, label, (String) null));
        
        processContextStack.pop();
    }

    @Override
    public void exitTaskRule(ProcessParser.TaskRuleContext ctx) {
        String id = ctx.ID().getText();
        String label = stripQuotes(ctx.STRING().getText());
        String type = ctx.TaskType().getText();
        String currentProcessId = processContextStack.peek();
        elements.add(new ElementInfo(id, ElementType.TASK, label, type, currentProcessId));
    }

    @Override
    public void exitGatewayRule(ProcessParser.GatewayRuleContext ctx) {
        String id = ctx.ID().getText();
        String label = stripQuotes(ctx.STRING().getText());
        String type = ctx.GatewayType().getText();
        String currentProcessId = processContextStack.peek();
        elements.add(new ElementInfo(id, ElementType.GATEWAY, label, type, currentProcessId));
    }

    @Override
    public void exitStartRule(ProcessParser.StartRuleContext ctx) {
        String id = ctx.ID().getText();
        String currentProcessId = processContextStack.peek();
        elements.add(new ElementInfo(id, ElementType.START_EVENT, currentProcessId));
    }

    @Override
    public void exitEndRule(ProcessParser.EndRuleContext ctx) {
        String id = ctx.ID().getText();
        String currentProcessId = processContextStack.peek();
        elements.add(new ElementInfo(id, ElementType.END_EVENT, currentProcessId));
    }

    @Override
    public void exitMessageRef(ProcessParser.MessageRefContext ctx) {
        String id = ctx.ID().getText();
        elements.add(new ElementInfo(id, ElementType.MESSAGE, (String) null));
    }

    public List<ElementInfo> getElements() {
        return elements;
    }

    private String stripQuotes(String text) {
        if (text != null && text.length() >= 2 && text.startsWith("\"") && text.endsWith("\"")) {
            return text.substring(1, text.length() - 1);
        }
        return text;
    }
}
