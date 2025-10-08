package com.matheusfilipefreitas.bpmn_runner_api.model.script.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;

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
        Integer line = ctx.ID().getSymbol().getLine();
        elements.add(new ElementInfo(id, ElementType.POOL, label, (String) null, line));
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
        Integer line = ctx.ID().getSymbol().getLine();
        elements.add(new ElementInfo(id, ElementType.PROCESS, label, (String) null, line));
        
        processContextStack.pop();
    }

    @Override
    public void exitTaskRule(ProcessParser.TaskRuleContext ctx) {
        String id = ctx.ID().getText();
        String label = stripQuotes(ctx.STRING().getText());
        String type = ctx.TaskType().getText();
        String currentProcessId = processContextStack.peek();
        Integer line = ctx.ID().getSymbol().getLine();
        elements.add(new ElementInfo(id, ElementType.TASK, label, type, currentProcessId, line));
    }

    @Override
    public void exitGatewayRule(ProcessParser.GatewayRuleContext ctx) {
        String id = ctx.ID().getText();
        String label = stripQuotes(ctx.STRING().getText());
        String type = ctx.GatewayType().getText();
        String currentProcessId = processContextStack.peek();
        Integer line = ctx.ID().getSymbol().getLine();
        elements.add(new ElementInfo(id, ElementType.GATEWAY, label, type, currentProcessId, line));
    }

    @Override
    public void exitExclusiveScope(ProcessParser.ExclusiveScopeContext ctx) {
        var yesBranch = ctx.yesBranch();
        var noBranch = ctx.noBranch();

        String yesContent = extractBranchContent(yesBranch);
        String noContent = extractBranchContent(noBranch);

        System.out.println("YES:\n" + yesContent);
        System.out.println("NO:\n" + noContent);
    }

    private String extractBranchContent(ParserRuleContext branchCtx) {
        List<ProcessParser.FlowElementContext> elements = branchCtx.getRuleContexts(ProcessParser.FlowElementContext.class);

        if (elements.isEmpty()) {
            ProcessParser.FlowElementContext single = branchCtx.getRuleContext(ProcessParser.FlowElementContext.class, 0);
            if (single != null)
                return single.getText();
            else
                return "";
        }

        return elements.stream()
                .map(RuleContext::getText)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public void exitParallelScope(ProcessParser.ParallelScopeContext ctx) {
        List<ProcessParser.FlowElementContext> elements = ctx.flowElement();

        String scopeContent = elements.stream()
                .map(RuleContext::getText)
                .collect(Collectors.joining("\n"));

        System.out.println("PARALLEL SCOPE:\n" + scopeContent);
    }

    @Override
    public void exitStartRule(ProcessParser.StartRuleContext ctx) {
        String id = ctx.ID().getText();
        String currentProcessId = processContextStack.peek();
        Integer line = ctx.ID().getSymbol().getLine();
        elements.add(new ElementInfo(id, ElementType.START_EVENT, currentProcessId, line));
    }

    @Override
    public void exitEndRule(ProcessParser.EndRuleContext ctx) {
        String id = ctx.ID().getText();
        String currentProcessId = processContextStack.peek();
        Integer line = ctx.ID().getSymbol().getLine();
        elements.add(new ElementInfo(id, ElementType.END_EVENT, currentProcessId, line));
    }

    @Override
    public void exitMessageRef(ProcessParser.MessageRefContext ctx) {
        String id = ctx.ID().getText();
        Integer line = ctx.ID().getSymbol().getLine();
        elements.add(new ElementInfo(id, ElementType.MESSAGE, (String) null, line));
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

    private void handleGatewayRuleByType() {

    }
}
