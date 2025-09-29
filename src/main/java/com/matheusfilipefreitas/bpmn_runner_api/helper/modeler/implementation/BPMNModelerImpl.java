package com.matheusfilipefreitas.bpmn_runner_api.helper.modeler.implementation;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Collaboration;
import org.camunda.bpm.model.bpmn.instance.Participant;
import org.camunda.bpm.model.bpmn.instance.SubProcess;
import org.springframework.stereotype.Component;

import com.matheusfilipefreitas.bpmn_runner_api.helper.modeler.BPMNModeler;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.EndEvent;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.Gateway;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.Pool;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.Process;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.StartEvent;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.Task;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.common.CommonBPMNIdEntity;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.connection.ConnectionBPMNEntity;

@Component
public class BPMNModelerImpl implements BPMNModeler {
    private final Map<Class<?>, BiConsumer<CommonBPMNIdEntity, BpmnModelInstance>> handlers = Map.of(
        Pool.class, this::handlePool,
        Process.class, this::handleProcess,
        StartEvent.class, this::handleStartEvent,
        Task.class, this::handleTask,
        Gateway.class, this::handleGateway,
        EndEvent.class, this::handleEndEvent
    );

    @Override
    public void modelEntitiesIntoModel(BpmnModelInstance model, List<CommonBPMNIdEntity> entities) {
        throwIfCannotGetModel(model);

        entities.stream()
                .filter(e -> e instanceof Process)
                .forEach(entity -> handleProcess(entity, model));

        entities.stream()
                .filter(e -> e instanceof Pool)
                .forEach(entity -> handlePool(entity, model));

        entities.stream()
                .filter(e -> !(e instanceof Process || e instanceof Pool))
                .forEach(entity -> handlers.getOrDefault(entity.getClass(), this::handleDefault)
                                        .accept(entity, model));
    }

    @Override
    public void modelConnectionsIntoModel(BpmnModelInstance model, List<ConnectionBPMNEntity> connections) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'modelConnectionsIntoModel'");
    }

    private void handlePool(CommonBPMNIdEntity entity, BpmnModelInstance model) {
        throwIfCannotGetModel(model);
        Pool pool = (Pool) entity;

        Collaboration collaboration = model.getModelElementsByType(Collaboration.class).stream().findFirst().orElseGet(() -> {
            Collaboration collab = model.newInstance(Collaboration.class);
            collab.setId("collaboration_" + System.currentTimeMillis());
            model.getDefinitions().addChildElement(collab);
            return collab;
        });

        Participant participant = model.newInstance(Participant.class);
        participant.setId(pool.getId());
        participant.setName(pool.getLabel());

        org.camunda.bpm.model.bpmn.instance.Process processToRef = model.getModelElementById("mainProcess");
        if (processToRef != null) {
            participant.setProcess(processToRef);
        } else {
            throw new RuntimeException("Process with ID '" + pool.getProcessId() + "' not found for Pool '" + pool.getId() + "'.");
        }

        collaboration.addChildElement(participant);
    }

    private void handleProcess(CommonBPMNIdEntity entity, BpmnModelInstance model) {
        throwIfCannotGetModel(model);
        Process processEntity = (Process) entity;

        SubProcess subProcess = model.newInstance(SubProcess.class);
        subProcess.setId(processEntity.getId());
        subProcess.setName(processEntity.getLabel());

        org.camunda.bpm.model.bpmn.instance.Process mainProcess = 
            model.getModelElementById("mainProcess");

        mainProcess.addChildElement(subProcess);
    }

    private void handleStartEvent(CommonBPMNIdEntity entity, BpmnModelInstance model) { 
        throwIfCannotGetModel(model);
        StartEvent startEventEntity = (StartEvent) entity;

        org.camunda.bpm.model.bpmn.instance.StartEvent startEvent =
            model.newInstance(org.camunda.bpm.model.bpmn.instance.StartEvent.class);
        startEvent.setId(startEventEntity.getId());

        attachToProcess(model, startEvent, startEventEntity.getProcessId());
    }

    private void handleTask(CommonBPMNIdEntity entity, BpmnModelInstance model) {
        throwIfCannotGetModel(model);
        Task taskEntity = (Task) entity;
        
        org.camunda.bpm.model.bpmn.instance.Task bpmnTask = createBpmnTask(taskEntity, model);
        attachToProcess(model, bpmnTask, taskEntity.getProcessId());
    }

    private org.camunda.bpm.model.bpmn.instance.Task createBpmnTask(Task task, BpmnModelInstance model) {
        return switch (task.getType()) {
            case AUTOMATED -> createServiceTask(task, model);
            case USER      -> createUserTask(task, model);
            case MANUAL    -> createManualTask(task, model);
            default        -> throw new RuntimeException("Unsupported task type: " + task.getType());
        };
    }

    private org.camunda.bpm.model.bpmn.instance.ServiceTask createServiceTask(Task task, BpmnModelInstance model) {
        var serviceTask = model.newInstance(org.camunda.bpm.model.bpmn.instance.ServiceTask.class);
        serviceTask.setId(task.getId());
        serviceTask.setName(task.getLabel());
        return serviceTask;
    }

    private org.camunda.bpm.model.bpmn.instance.UserTask createUserTask(Task task, BpmnModelInstance model) {
        var userTask = model.newInstance(org.camunda.bpm.model.bpmn.instance.UserTask.class);
        userTask.setId(task.getId());
        userTask.setName(task.getLabel());
        return userTask;
    }

    private org.camunda.bpm.model.bpmn.instance.ManualTask createManualTask(Task task, BpmnModelInstance model) {
        var manualTask = model.newInstance(org.camunda.bpm.model.bpmn.instance.ManualTask.class);
        manualTask.setId(task.getId());
        manualTask.setName(task.getLabel());
        return manualTask;
    }


    private void handleGateway(CommonBPMNIdEntity entity, BpmnModelInstance model) {
        throwIfCannotGetModel(model);
        Gateway gatewayEntity = (Gateway) entity;
        org.camunda.bpm.model.bpmn.instance.Gateway bpmnGateway = createBpmnGateway(gatewayEntity, model);
        attachToProcess(model, bpmnGateway, gatewayEntity.getProcessId());
    }

    private org.camunda.bpm.model.bpmn.instance.Gateway createBpmnGateway(Gateway gateway, BpmnModelInstance model) {
        return switch (gateway.getType()) {
            case EXCLUSIVE -> createExclusiveGateway(gateway, model);
            case PARALLEL  -> createParallelGateway(gateway, model);
            default        -> throw new RuntimeException("Unsupported gateway type: " + gateway.getType());
        };
    }

    private org.camunda.bpm.model.bpmn.instance.ExclusiveGateway createExclusiveGateway(Gateway gateway, BpmnModelInstance model) {
        var exclusive = model.newInstance(org.camunda.bpm.model.bpmn.instance.ExclusiveGateway.class);
        exclusive.setId(gateway.getId());
        exclusive.setName(gateway.getLabel());
        return exclusive;
    }

    private org.camunda.bpm.model.bpmn.instance.ParallelGateway createParallelGateway(Gateway gateway, BpmnModelInstance model) {
        var parallel = model.newInstance(org.camunda.bpm.model.bpmn.instance.ParallelGateway.class);
        parallel.setId(gateway.getId());
        parallel.setName(gateway.getLabel());
        return parallel;
    }

    private void handleEndEvent(CommonBPMNIdEntity entity, BpmnModelInstance model) {
        throwIfCannotGetModel(model);
        EndEvent endEventEntity = (EndEvent) entity;

        org.camunda.bpm.model.bpmn.instance.EndEvent endEvent =
            model.newInstance(org.camunda.bpm.model.bpmn.instance.EndEvent.class);
        endEvent.setId(endEventEntity.getId());

        attachToProcess(model, endEvent, endEventEntity.getProcessId());
    }

    private void handleDefault(CommonBPMNIdEntity entity, BpmnModelInstance model) { 
        throw new RuntimeException(
            "Could not map entity because the type isn't specified in modeler: " + entity.getClass().getName()
        );
    }

    private void throwIfCannotGetModel(BpmnModelInstance model) {
        if (model == null) {
            throw new RuntimeException("Cannot get model while trying to model");
        }
    }
    
    private void attachToProcess(BpmnModelInstance model, org.camunda.bpm.model.bpmn.instance.FlowNode node, String processId) {
        org.camunda.bpm.model.bpmn.instance.SubProcess process = model.getModelElementById(processId);
        if (process != null) {
            process.addChildElement(node);
        } else {
            throw new RuntimeException("Process with ID '" + processId + "' not found for element '" + node.getId() + "'.");
        }
    }

}
