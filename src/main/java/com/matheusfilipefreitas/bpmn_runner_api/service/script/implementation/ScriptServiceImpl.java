package com.matheusfilipefreitas.bpmn_runner_api.service.script.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.springframework.stereotype.Service;

import com.matheusfilipefreitas.bpmn_runner_api.common.exception.interpreter.InterpreterException;
import com.matheusfilipefreitas.bpmn_runner_api.common.listener.SyntaxErrorListener;
import com.matheusfilipefreitas.bpmn_runner_api.helper.builder.BPMNBuilder;
import com.matheusfilipefreitas.bpmn_runner_api.helper.modeler.BPMNModeler;
import com.matheusfilipefreitas.bpmn_runner_api.helper.validators.ElementIdsValidator;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.Gateway;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.Pool;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.Process;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.common.CommonBPMNIdEntity;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.connection.ConnectionBPMNEntity;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.types.GatewayType;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.ElementBranch;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.ElementExclusiveBranch;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.ElementInfo;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.ElementParallelBranch;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.types.ElementType;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.grammar.ProcessLexer;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.grammar.ProcessParser;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.listener.IdCollectorListener;
import com.matheusfilipefreitas.bpmn_runner_api.service.bpmn.BPMNConnectionService;
import com.matheusfilipefreitas.bpmn_runner_api.service.bpmn.BPMNElementService;
import com.matheusfilipefreitas.bpmn_runner_api.service.script.ScriptService;

@Service
public class ScriptServiceImpl implements ScriptService {
    private final ElementIdsValidator idsValidator;
    private final BPMNElementService elementService;
    private final BPMNConnectionService connectionService;
    private final BPMNBuilder builder;
    private final BPMNModeler modeler;

    private ProcessParser.ModelContext context = null;
    private BpmnModelInstance model = null;
    private List<ElementInfo> elementsInfo = new ArrayList<>();
    private List<ElementBranch> elementsBranchInfo = new ArrayList<>();

    public ScriptServiceImpl(
        ElementIdsValidator idsValidator, 
        BPMNElementService elementService,
        BPMNConnectionService connectionService,
        BPMNBuilder builder,
        BPMNModeler modeler) {
            this.idsValidator = idsValidator;
            this.elementService = elementService;
            this.connectionService = connectionService;
            this.builder = builder;
            this.modeler = modeler;
    }

    @Override
    public String processScript(String script) {
        try {
            validateScriptSyntax(script);
            validateIdsCreation();
            
            if (elementsInfo.isEmpty()) {
                throw new InterpreterException("No entities found");
            }

            Collections.sort(elementsInfo, (e1, e2) -> Integer.compare(e1.getIndex(), e2.getIndex()));
            for (int i = 0; i < elementsInfo.size() - 1; i++) {
                elementsInfo.get(i).setIndex(i);
            }

            createEntitiesFromElementsInfo(elementsInfo);
            createConnectionsFromElementsInfo(elementsInfo, elementsBranchInfo);

            throwIfNotAllElementAreInConnections();

            handleModelCreation();

            throwIfCannotGetModeler();
            String result = Bpmn.convertToString(this.model);
            resetVariables();
            return result;
        } catch (InterpreterException ex) {
            resetVariables();
            throw ex;
        }
    }

    private void resetVariables() {
        this.model = null;
        this.context = null;
        this.elementService.clearEntities();
        this.connectionService.resetConnections();
    }
    
    private void validateScriptSyntax(String script) {
        try {
            CodePointCharStream charStream = CharStreams.fromString(script);

            ProcessLexer lexer = new ProcessLexer(charStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            ProcessParser parser = new ProcessParser(tokens);

            parser.addErrorListener(new SyntaxErrorListener());
            this.context = parser.model();
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void validateIdsCreation() {
        try {        
            if (this.context == null) {
                throw new RuntimeException("Context creation with error");
            }
            IdCollectorListener listener = new IdCollectorListener();
            ParseTreeWalker.DEFAULT.walk(listener, this.context);

            listener.validate();

            elementsInfo = listener.getElements();
            elementsBranchInfo = listener.getElementBranchs();

            idsValidator.validate(elementsInfo);
        } catch (Exception ex) {
            resetVariables();
            throw ex;
        }
    }

    private void throwIfNotAllElementAreInConnections() {
        try {
            List<CommonBPMNIdEntity> entities = this.elementService.findAll();
            List<ConnectionBPMNEntity> connections = this.connectionService.findAll();

            if (entities.isEmpty() || connections.isEmpty()) {
                throw new RuntimeException("No elements or flows created");
            }

            List<String> elementsIds = entities.stream().filter(e -> !(e instanceof Pool) && !(e instanceof Process)).map(e -> e.getId()).toList();
            idsValidator.validateAllElementsInConnections(elementsIds, connections);
        } catch(Exception ex) {
            resetVariables();
            throw ex;
        }
    }

    private void createEntitiesFromElementsInfo(List<ElementInfo> elementsInfo) {
        try {
            this.elementService.saveEntitiesFromElementInfoList(elementsInfo);
        } catch (Exception e) {
            resetVariables();
            throw new RuntimeException("Error while creating elements");
        }
    }

    private void createConnectionsFromElementsInfo(List<ElementInfo> elementsInfo, List<ElementBranch> branchesInfo) {
        try {
            this.connectionService.saveConnectionsFromElementInfo(elementsInfo, branchesInfo);
        } catch (Exception e) {
            resetVariables();
            throw new RuntimeException("Error while creating connections");
        }
    }

    private void handleModelCreation() {
        this.model = createModel();        
        if (this.model == null) {
            throw new InterpreterException("Could not start modeler");
        }

        List<CommonBPMNIdEntity> entities = elementService.findAll().stream()
            .sorted(Comparator.comparingInt(CommonBPMNIdEntity::getIndex))
            .collect(Collectors.toList());

        List<ConnectionBPMNEntity> connections = connectionService.findAll().stream()
            .sorted(Comparator.comparingInt(ConnectionBPMNEntity::getIndex))
            .collect(Collectors.toList());

        createEntitiesInModel(entities);
        createConnectionsInModel(connections);
        createGraphicElementsInModel(entities, connections);
    }

    private BpmnModelInstance createModel() {
        return builder.buildModel();
    }

    private void createEntitiesInModel(List<CommonBPMNIdEntity> entities) {
        throwIfCannotGetModeler();

        modeler.modelEntitiesIntoModel(this.model, entities);
    }

    private void createConnectionsInModel(List<ConnectionBPMNEntity> connections) {
        throwIfCannotGetModeler();

        modeler.modelConnectionsIntoModel(this.model, connections);
    }

    private void createGraphicElementsInModel(List<CommonBPMNIdEntity> entities, List<ConnectionBPMNEntity> connections) {
        throwIfCannotGetModeler();
        modeler.createDiagramElements(model, entities, connections, elementsBranchInfo);
    }

    private void throwIfCannotGetModeler() {
        if (this.model == null) {
            throw new InterpreterException("Could not get modeler");
        }
    }
}
