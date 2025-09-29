package com.matheusfilipefreitas.bpmn_runner_api.service.script.implementation;

import java.util.List;

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
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.common.CommonBPMNIdEntity;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.connection.ConnectionBPMNEntity;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.ElementInfo;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.grammar.ProcessLexer;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.grammar.ProcessParser;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.listener.IdCollectorListener;
import com.matheusfilipefreitas.bpmn_runner_api.service.bpmn.BPMNConnectionService;
import com.matheusfilipefreitas.bpmn_runner_api.service.bpmn.BPMNElementService;
import com.matheusfilipefreitas.bpmn_runner_api.service.script.ScriptService;

/** 
 * TODO: rever regra de taskRef como '-> id' para adicionar regras de validação:
 * - IdCollectorListener
 * - ElementIdsValidatorImpl
*/

@Service
public class ScriptServiceImpl implements ScriptService {
    private final ElementIdsValidator idsValidator;
    private final BPMNElementService elementService;
    private final BPMNConnectionService connectionService;
    private final BPMNBuilder builder;
    private final BPMNModeler modeler;

    private ProcessParser.ModelContext context = null;
    private BpmnModelInstance model = null;

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
            List<ElementInfo> elementsInfo = validateIdsCreation();
            createEntitiesFromElementsInfo(elementsInfo);
            createConnectionsFromElementsInfo(elementsInfo);

            handleModelCreation();

            throwIfCannotGetModeler();
            return Bpmn.convertToString(this.model);
        } catch (InterpreterException ex) {
            throw ex;
        }
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

    private List<ElementInfo> validateIdsCreation() {
        try {        
            if (this.context == null) {
                throw new RuntimeException("Context creation with error");
            }
            IdCollectorListener listener = new IdCollectorListener();
            ParseTreeWalker.DEFAULT.walk(listener, this.context);

            List<ElementInfo> elementsInfo = listener.getElements();
            idsValidator.validate(elementsInfo);
            return elementsInfo;
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void createEntitiesFromElementsInfo(List<ElementInfo> elementsInfo) {
        try {
            this.elementService.saveEntitiesFromElementInfoList(elementsInfo);
        } catch (Exception e) {
            // clean models in service
        }
    }

    private void createConnectionsFromElementsInfo(List<ElementInfo> elementsInfo) {
        try {
            this.connectionService.saveConnectionsFromElementInfo(elementsInfo);
        } catch (Exception e) {
            // clean models in service
        }
    }

    private void handleModelCreation() {
        this.model = createModel();        
        if (this.model == null) {
            throw new InterpreterException("Could not start modeler");
        }
        createEntitiesInModel();
        createConnectionsInModel();
        System.out.println(Bpmn.convertToString(this.model));
    }

    private BpmnModelInstance createModel() {
        return builder.buildModel();
    }

    private void createEntitiesInModel() {
        throwIfCannotGetModeler();

        List<CommonBPMNIdEntity> entities = elementService.findAll();
        modeler.modelEntitiesIntoModel(this.model, entities);
    }

    private void createConnectionsInModel() {
        throwIfCannotGetModeler();

        List<ConnectionBPMNEntity> connections = connectionService.findAll();
        modeler.modelConnectionsIntoModel(this.model, connections);
    }

    private void throwIfCannotGetModeler() {
        if (this.model == null) {
            throw new InterpreterException("Could not get modeler");
        }
    }
}
