package com.matheusfilipefreitas.bpmn_runner_api.model.script.listener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;

import com.matheusfilipefreitas.bpmn_runner_api.common.exception.interpreter.InterpreterException;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.ElementBranch;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.ElementExclusiveBranch;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.ElementInfo;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.ElementParallelBranch;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.types.BranchOrder;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.types.ElementType;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.grammar.ProcessParser;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.grammar.ProcessParserBaseListener;

public class IdCollectorListener extends ProcessParserBaseListener {
    private ProcessParser.ProcessContext firstProcess;
    private boolean firstProcessCaptured = false;
    private ProcessParser.StartRuleContext startRule;
    private int endCount = 0;

    private final List<ElementInfo> elements = new ArrayList<>();
    private final List<ElementBranch> branches = new ArrayList<>();
    private final Stack<String> processContextStack = new Stack<>();

    @Override
    public void enterProcess(ProcessParser.ProcessContext ctx) {
        String processId = ctx.ID().getText();
        processContextStack.push(processId);
        if (!firstProcessCaptured) {
            firstProcess = ctx;
            firstProcessCaptured = true;
        }
    }

    @Override
    public void exitPool(ProcessParser.PoolContext ctx) {
        String id = ctx.ID().getText();
        String label = stripQuotes(ctx.STRING().getText());
        Integer line = ctx.ID().getSymbol().getLine();
        elements.add(new ElementInfo(id, ElementType.POOL, label, (String) null, line));
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
    public void enterStartRule(ProcessParser.StartRuleContext ctx) {
        if (startRule != null) {
            throw new InterpreterException("Only one start event is allowed in the script.");
        }
        startRule = ctx;
    }

    @Override
    public void enterEndRule(ProcessParser.EndRuleContext ctx) {
        endCount++;
    }

    @Override
    public void exitGatewayRule(ProcessParser.GatewayRuleContext ctx) {
        String id = ctx.ID().getText();
        String label = stripQuotes(ctx.STRING().getText());
        String type = ctx.GatewayType().getText();
        String currentProcessId = processContextStack.peek();
        Integer line = ctx.ID().getSymbol().getLine();
        elements.add(new ElementInfo(id, ElementType.GATEWAY, label, type, currentProcessId, line));
        if (ctx.GatewayType().getText().equals("PARALLEL")) {
            for (var scopeCtx : ctx.parallelScope()) {
                var branchElements = new ArrayList<LinkedHashMap<String, ElementType>>();
                for (var flow : scopeCtx.flowElement()) {
                    branchElements.add(extractIdsFromFlowElement(flow));
                }
                String gatewayId = ctx.ID().getText();
                branches.add(new ElementParallelBranch(gatewayId, branchElements));
            }
        }
    }

    @Override
    public void exitExclusiveScope(ProcessParser.ExclusiveScopeContext ctx) {
        var yesBranch = ctx.yesBranch();
        var noBranch = ctx.noBranch();

        LinkedHashMap<String, ElementType> yesContent = extractBranchContent(yesBranch);
        LinkedHashMap<String, ElementType> noContent = extractBranchContent(noBranch);

        ProcessParser.GatewayRuleContext gatewayCtx = null;
        ParserRuleContext parent = ctx.getParent();
        while (parent != null) {
            if (parent instanceof ProcessParser.GatewayRuleContext gw) {
                gatewayCtx = gw;
                break;
            }
            parent = parent.getParent();
        }

        if (gatewayCtx != null) {
            String gatewayId = gatewayCtx.ID().getText();
            int yesIndex = yesBranch.getStart().getStartIndex();
            int noIndex = noBranch.getStart().getStartIndex();

            BranchOrder order = BranchOrder.YES;
            if (noIndex < yesIndex) {
                order = BranchOrder.NO;
            }

            branches.add(new ElementExclusiveBranch(gatewayId, yesContent, noContent, order));
        } else {
            throw new InterpreterException("yes or no branch without gateway as parent detected " + ctx.getRuleIndex());
        }
    }

    private LinkedHashMap<String, ElementType> extractBranchContent(ParserRuleContext branchCtx) {
        if (branchCtx == null) return new LinkedHashMap<>();

        List<ProcessParser.FlowElementContext> flows = new ArrayList<>();
        if (!(branchCtx instanceof ProcessParser.YesBranchContext yesCtx)) {
            if (branchCtx instanceof ProcessParser.NoBranchContext noCtx) {
                flows = noCtx.flowElement();
            }
        } else {
            flows = yesCtx.flowElement();
        }

        LinkedHashMap<String, ElementType> result = new LinkedHashMap<>();
        for (var flow : flows) {
            result.putAll(extractIdsFromFlowElement(flow));
        }
        return result;
    }

    private LinkedHashMap<String, ElementType> extractIdsFromFlowElement(ProcessParser.FlowElementContext ctx) {
        if (ctx == null) return new LinkedHashMap<>();

        LinkedHashMap<String, ElementType> ids = new LinkedHashMap<>();
        var taskRef = ctx.taskRef();

        if (taskRef == null) {
            if(!ctx.children.isEmpty()) {
                    if (ctx.children.get(0) instanceof ProcessParser.EndRuleContext endRule) {
                    String id = endRule.ID().getText();
                    ids.put(id, ElementType.END_EVENT);
                }
            }else{
                return new LinkedHashMap<>();
            }
        }

        if (taskRef != null && taskRef.taskRule() != null) {
            ids.put(taskRef.taskRule().ID().getText(), ElementType.TASK);
        }

        if (taskRef != null && taskRef.messageRef() != null) {
            ids.put(taskRef.messageRef().ID().getText(), ElementType.MESSAGE);
        }

        else if (taskRef != null && taskRef.gatewayRule() != null) {
            var gwCtx = taskRef.gatewayRule();
            ids.put(gwCtx.ID().getText(), ElementType.GATEWAY);

            if (gwCtx.exclusiveScope() != null) {
                var exScope = gwCtx.exclusiveScope();
                var yes = exScope.yesBranch();
                if (yes != null) {
                    for (var flow : yes.flowElement()) {
                        ids.putAll(extractIdsFromFlowElement(flow));
                    }
                }
                var no = exScope.noBranch();
                if (no != null) {
                    for (var flow : no.flowElement()) {
                        ids.putAll(extractIdsFromFlowElement(flow));
                    }
                }
            } else {
                for (var ps : gwCtx.parallelScope()) {
                    for (var flow : ps.flowElement()) {
                        ids.putAll(extractIdsFromFlowElement(flow));
                    }
                }
            }
        } else if (taskRef != null && taskRef.ID() != null) {
            ids.put(taskRef.ID().getText(), ElementType.TASK);

            if (taskRef.messageRef() != null) {
                ids.put(taskRef.messageRef().ID().getText(), ElementType.MESSAGE);
            }
        }

        return ids;
    }

    @Override
    public void exitStartRule(ProcessParser.StartRuleContext ctx) {
        String id = ctx.ID().getText();
        String currentProcessId = processContextStack.peek();
        Integer line = ctx.ID().getSymbol().getLine();

        List<ElementInfo> startEventList = elements.stream().filter(e -> e.getElementType() == ElementType.START_EVENT && e.getProcessId().equals(currentProcessId)).toList();

        if (!startEventList.isEmpty()) {
            throw new InterpreterException("More than one start event found in same process");
        }

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

    public void validate() {
        if (startRule == null) {
            throw new InterpreterException("Must contain exactly one start event.");
        }
        if (endCount < 1) {
            throw new InterpreterException("Must contain at least one end event.");
        }

        ParserRuleContext parent = startRule.getParent();
        boolean insideFirstProcess = false;
        while (parent != null) {
            if (parent == firstProcess) {
                insideFirstProcess = true;
                break;
            }
            parent = parent.getParent();
        }

        if (!insideFirstProcess) {
            throw new InterpreterException("Start event must be inside the first process of the script.");
        }
    }

    public List<ElementInfo> getElements() {
        return elements;
    }

    public List<ElementBranch> getElementBranchs() {
        return branches;
    }

    private String stripQuotes(String text) {
        if (text != null && text.length() >= 2 && text.startsWith("\"") && text.endsWith("\"")) {
            return text.substring(1, text.length() - 1);
        }
        return text;
    }
}
