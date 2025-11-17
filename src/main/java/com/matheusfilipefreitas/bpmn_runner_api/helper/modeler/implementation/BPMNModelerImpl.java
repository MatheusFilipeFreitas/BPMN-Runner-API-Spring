package com.matheusfilipefreitas.bpmn_runner_api.helper.modeler.implementation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.TriConsumer;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Collaboration;
import org.camunda.bpm.model.bpmn.instance.ConditionExpression;
import org.camunda.bpm.model.bpmn.instance.Event;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.InteractionNode;
import org.camunda.bpm.model.bpmn.instance.MessageFlow;
import org.camunda.bpm.model.bpmn.instance.Participant;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnDiagram;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnEdge;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnPlane;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnShape;
import org.camunda.bpm.model.bpmn.instance.dc.Bounds;
import org.camunda.bpm.model.bpmn.instance.di.Waypoint;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
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
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.types.ConnectionType;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.ElementBranch;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.ElementExclusiveBranch;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.ElementParallelBranch;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.types.BranchOrder;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.types.ElementType;


@Component
public class BPMNModelerImpl implements BPMNModeler {
    private final Map<Class<?>, TriConsumer<CommonBPMNIdEntity, BpmnModelInstance, List<CommonBPMNIdEntity>>> handlers = Map.of(
        Pool.class, this::handlePool,
        StartEvent.class, this::handleStartEvent,
        Task.class, this::handleTask,
        Gateway.class, this::handleGateway,
        EndEvent.class, this::handleEndEvent
    );

    @Override
    public void modelEntitiesIntoModel(BpmnModelInstance model, List<CommonBPMNIdEntity> entities) {
        throwIfCannotGetModel(model);

        List<CommonBPMNIdEntity> sortedEntities = entities.stream()
            .sorted(Comparator.comparing(CommonBPMNIdEntity::getIndex)).toList();
        
        sortedEntities.forEach(entity -> {
                if (entity instanceof Pool) {
                    handlePool(entity, model, sortedEntities);
                } else if (!(entity instanceof Process)) {
                    handlers.getOrDefault(entity.getClass(), this::handleDefault)
                            .accept(entity, model, sortedEntities);
                }
            });
    }

    @Override
    public void modelConnectionsIntoModel(BpmnModelInstance model, List<ConnectionBPMNEntity> connections) {
        throwIfCannotGetModel(model);

        List<ConnectionBPMNEntity> orderedConnections = connections.stream()
            .sorted(Comparator.comparing(ConnectionBPMNEntity::getIndex)).toList();

        orderedConnections = handleSequentialAndMessageEntities(orderedConnections);

        orderedConnections.forEach(connection -> {
            throwIfCannotGetConnectionType(connection);
            
            switch (connection.getType()) {
                case EXCLUSIVE -> handleExclusive(connection, model);
                case PARALLEL -> handleParallel(connection, model);
                case MESSAGE -> handleMessage(connection, model);
                default -> handleSequencial(connection, model);
            }
        });
    }

    @Override
    public void createDiagramElements(BpmnModelInstance model, List<CommonBPMNIdEntity> entities, List<ConnectionBPMNEntity> connections, List<ElementBranch> branchs) {
        Collaboration collaboration = model.getModelElementsByType(Collaboration.class)
            .stream().findFirst().orElse(null);

        if (collaboration == null) {
            collaboration = model.newInstance(Collaboration.class);
            collaboration.setId("collaboration_" + System.currentTimeMillis());
            model.getDefinitions().addChildElement(collaboration);
        }

        BpmnDiagram diagram = model.newInstance(BpmnDiagram.class);
        diagram.setId("BPMNDiagram_" + System.currentTimeMillis());

        BpmnPlane plane = model.newInstance(BpmnPlane.class);
        plane.setId("BPMNPlane_" + System.currentTimeMillis());
        plane.setBpmnElement(collaboration);
        diagram.addChildElement(plane);

        Map<String, List<CommonBPMNIdEntity>> groupedByProcess = entities.stream()
            .filter(e -> !(e instanceof Pool))
            .filter(e -> e.getProcessId() != null)
            .collect(Collectors.groupingBy(CommonBPMNIdEntity::getProcessId));

        int poolSpacing = 100;
        int elementSpacingX = 100;
        int poolPadding = 50;
        double currentY = 50;

        Map<String, Bounds> elementBoundsMap = new HashMap<>();

        for (Map.Entry<String, List<CommonBPMNIdEntity>> entry : groupedByProcess.entrySet()) {
            String processId = entry.getKey();
            List<CommonBPMNIdEntity> processEntities = entry.getValue();

            Participant participant = model.getModelElementsByType(Participant.class)
                .stream()
                .filter(p -> p.getProcess() != null && p.getProcess().getId().equals(processId))
                .findFirst()
                .orElse(null);

            String poolId = participant != null ? participant.getId() : "pool_" + processId;

            int x = 90;
            double maxX = 0;
            double maxY = currentY;
            int branchVerticalSpacing = 120;
            int branchIndex = 0;
            Map<String, Integer> branchYOffsetMap = new HashMap<>();

            for (ElementBranch branch : branchs) {
                branchYOffsetMap.put(branch.getGatewayId(), branchIndex++);
            }

            for (CommonBPMNIdEntity entity : processEntities) {
                ModelElementInstance el = model.getModelElementById(entity.getId());
                if (!(el instanceof FlowNode node)) continue;

                double width = 100;
                double height = 80;
                x += 60;

                if (node instanceof Event) {
                    width = 40;
                    height = 40;
                    x += 60;
                } else if (node instanceof org.camunda.bpm.model.bpmn.instance.Gateway) {
                    width = 60;
                    height = 60;
                    x += 60;
                }

                double branchYOffset = 0;
                for (ElementBranch branch : branchs) {
                    if (branch instanceof ElementParallelBranch parallelBranch) {
                        for (int i = 0; i < parallelBranch.getChildrenIdsMap().size(); i++) {
                            LinkedHashMap<String, ElementType> scope = parallelBranch.getChildrenIdsMap().get(i);
                            if (scope.containsKey(entity.getId())) {
                                branchYOffset = i * branchVerticalSpacing;
                                break;
                            }
                        }
                    } else if (branch instanceof ElementExclusiveBranch exclusiveBranch) {
                        BranchOrder order = exclusiveBranch.getIdInsideBranches(entity.getId());
                        if (order == BranchOrder.NO) {
                            branchYOffset = branchVerticalSpacing;
                        }
                    }
                }

                double elementCenterY = (currentY + poolPadding + branchYOffset + (80 / 2.0) - (height / 2.0)) + 20;

                BpmnShape shape = model.newInstance(BpmnShape.class);
                shape.setId("Shape_" + node.getId());
                shape.setBpmnElement(node);

                Bounds bounds = model.newInstance(Bounds.class);
                bounds.setX(x);
                bounds.setY(elementCenterY);
                bounds.setWidth(width);
                bounds.setHeight(height);

                shape.addChildElement(bounds);
                plane.addChildElement(shape);
                elementBoundsMap.put(node.getId(), bounds);

                x += elementSpacingX;
                maxX = Math.max(maxX, bounds.getX() + bounds.getWidth());
                maxY = Math.max(maxY, bounds.getY() + bounds.getHeight());
            }

            double poolWidth = maxX - 100 + poolPadding * 2;
            double poolHeight = (maxY - currentY) + poolPadding * 2;

            BpmnShape poolShape = model.newInstance(BpmnShape.class);
            poolShape.setId("Shape_" + poolId);
            poolShape.setBpmnElement(participant);
            Bounds poolBounds = model.newInstance(Bounds.class);
            poolBounds.setX(100);
            poolBounds.setY(currentY);
            poolBounds.setWidth(poolWidth);
            poolBounds.setHeight(poolHeight);
            poolShape.addChildElement(poolBounds);
            plane.addChildElement(poolShape);

            currentY += poolHeight + poolSpacing;
        }

        for (ConnectionBPMNEntity connection : connections) {
            if (connection.getType() == ConnectionType.MESSAGE) continue;

            ModelElementInstance el = model.getModelElementById(
                "flow_" + connection.getSourceId() + "_" + connection.getTargetId());
            if (!(el instanceof SequenceFlow flow)) continue;

            Bounds source = elementBoundsMap.get(connection.getSourceId());
            Bounds target = elementBoundsMap.get(connection.getTargetId());
            if (source == null || target == null) continue;

            double startX = source.getX() + source.getWidth();
            double startY = source.getY() + source.getHeight() / 2;
            double endX = target.getX();
            double endY = target.getY() + target.getHeight() / 2;

            BpmnEdge edge = model.newInstance(BpmnEdge.class);
            edge.setId("Edge_" + flow.getId());
            edge.setBpmnElement(flow);

            Waypoint wp1 = model.newInstance(Waypoint.class);
            wp1.setX(startX);
            wp1.setY(startY);
            Waypoint wp2 = model.newInstance(Waypoint.class);
            wp2.setX(endX);
            wp2.setY(endY);

            edge.addChildElement(wp1);
            edge.addChildElement(wp2);
            plane.addChildElement(edge);
        }

        for (ConnectionBPMNEntity connection : connections) {
            if (connection.getType() != ConnectionType.MESSAGE) continue;

            ModelElementInstance msgEl = model.getModelElementById(
                "msg_flow_" + connection.getSourceId() + "_" + connection.getTargetId());
            if (!(msgEl instanceof MessageFlow msgFlow)) continue;

            Bounds source = elementBoundsMap.get(connection.getSourceId());
            Bounds target = elementBoundsMap.get(connection.getTargetId());
            if (source == null || target == null) continue;

            double startX = source.getX() + source.getWidth();
            double startY = source.getY() + source.getHeight() / 2;
            double endX = target.getX();
            double endY = target.getY() + target.getHeight() / 2;

            BpmnEdge edge = model.newInstance(BpmnEdge.class);
            edge.setId("Edge_" + msgFlow.getId());
            edge.setBpmnElement(msgFlow);

            Waypoint wp1 = model.newInstance(Waypoint.class);
            wp1.setX(startX);
            wp1.setY(startY);
            Waypoint wp2 = model.newInstance(Waypoint.class);
            wp2.setX(endX);
            wp2.setY(endY);

            edge.addChildElement(wp1);
            edge.addChildElement(wp2);
            plane.addChildElement(edge);
        }

        model.getDefinitions().addChildElement(diagram);
    }

    private void handleParallel(ConnectionBPMNEntity connection, BpmnModelInstance model) {
        FlowNode sourceNode = model.getModelElementById(connection.getSourceId());
        String gatewayId = sourceNode.getId();

        org.camunda.bpm.model.bpmn.instance.Gateway gateway = findGateway(model, gatewayId);
        FlowNode targetNode = model.getModelElementById(connection.getTargetId());
        createSequenceFlow(model, gateway, targetNode, null, null);
    }

    private void handleExclusive(ConnectionBPMNEntity connection, BpmnModelInstance model) {
        FlowNode sourceNode = model.getModelElementById(connection.getSourceId());
        String gatewayId = sourceNode.getId();

        org.camunda.bpm.model.bpmn.instance.Gateway gateway = findGateway(model, gatewayId);

        FlowNode targetNode = model.getModelElementById(connection.getTargetId());
        String conditionExpression = "${" + connection.getLabel() + "}";
        createSequenceFlow(model, gateway, targetNode, connection.getLabel(), conditionExpression);
    }

    public void createSequenceFlow(BpmnModelInstance model, FlowNode from, FlowNode to, String name, String conditionExpression) {
        String flowId = "flow_" + from.getId() + "_" + to.getId();
        SequenceFlow sequenceFlow = model.newInstance(SequenceFlow.class);
        sequenceFlow.setId(flowId);
        sequenceFlow.setSource(from);
        sequenceFlow.setTarget(to);
        
        if (name != null && !name.isBlank()) {
            sequenceFlow.setName(name);
        }
        
        if (conditionExpression != null && !conditionExpression.isBlank()) {
            ConditionExpression condition = model.newInstance(ConditionExpression.class);
            condition.setTextContent(conditionExpression);
            sequenceFlow.setConditionExpression(condition);
        }

        String processId = findProcessIdForNode(model, from);
        org.camunda.bpm.model.bpmn.instance.Process process = model.getModelElementById(processId);
        if (process == null) {
            throw new RuntimeException("Cannot find process for sequence flow between " + from.getId() + " and " + to.getId());
        }

        process.addChildElement(sequenceFlow);
    }

    private String findProcessIdForNode(BpmnModelInstance model, FlowNode node) {
        org.camunda.bpm.model.xml.instance.ModelElementInstance parent = node.getParentElement();
        while (parent != null && !(parent instanceof org.camunda.bpm.model.bpmn.instance.Process)) {
            parent = parent.getParentElement();
        }
        if (parent instanceof org.camunda.bpm.model.bpmn.instance.Process) {
            return ((org.camunda.bpm.model.bpmn.instance.Process) parent).getId();
        }
        throw new RuntimeException("Could not find parent wi ID " + node.getId() + ", " + node.getName());
    }

    private void handleMessage(ConnectionBPMNEntity connection, BpmnModelInstance model) {
        InteractionNode sourceNode = model.getModelElementById(connection.getSourceId());
        InteractionNode targetNode = model.getModelElementById(connection.getTargetId());

        if (sourceNode == null || targetNode == null) {
            throw new RuntimeException("Source or target for message flow not found.");
        }

        Collaboration collaboration = model.getModelElementsByType(Collaboration.class).iterator().next();
        if (collaboration == null) {
            throw new RuntimeException("Cannot create message flow: No <collaboration> element found in the model.");
        }

        MessageFlow messageFlow = model.newInstance(MessageFlow.class);
        messageFlow.setId("msg_flow_" + connection.getSourceId() + "_" + connection.getTargetId());
        messageFlow.setName(connection.getLabel());
        messageFlow.setSource(sourceNode);
        messageFlow.setTarget(targetNode);

        collaboration.addChildElement(messageFlow);
    }

    private void handleSequencial(ConnectionBPMNEntity connection, BpmnModelInstance model) {
        FlowNode sourceNode = model.getModelElementById(connection.getSourceId());
        FlowNode targetNode = model.getModelElementById(connection.getTargetId());

        if (sourceNode == null || targetNode == null) {
            throw new RuntimeException("Cannot create sequence flow. Node not found. From: "
                    + connection.getSourceId() + ", To: " + connection.getTargetId());
        }
        
        if (connection.getType() == ConnectionType.MESSAGE) {
            return;
        }
        createSequenceFlow(model, sourceNode, targetNode, connection.getLabel(), null);
    }

    private void handlePool(CommonBPMNIdEntity entity, BpmnModelInstance model, List<CommonBPMNIdEntity> entities) {
        throwIfCannotGetModel(model);
        Pool pool = (Pool) entity;
        String processId = getPoolProcess(pool.getId(), entities);
        pool.setProcessId(processId);

        handleProcess(new Process(processId, null, null), model);

        Collaboration collaboration = model.getModelElementsByType(Collaboration.class).stream().findFirst().orElseGet(() -> {
            Collaboration collab = model.newInstance(Collaboration.class);
            collab.setId("collaboration_" + System.currentTimeMillis());
            model.getDefinitions().addChildElement(collab);
            return collab;
        });

        Participant participant = model.newInstance(Participant.class);
        participant.setId(pool.getId());
        participant.setName(pool.getLabel());

        org.camunda.bpm.model.bpmn.instance.Process processToRef = model.getModelElementById(processId);
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

        org.camunda.bpm.model.bpmn.instance.Process process =
            model.newInstance(org.camunda.bpm.model.bpmn.instance.Process.class);
            
        process.setId(processEntity.getId());
        process.setName(processEntity.getLabel());
        process.setExecutable(true);

        model.getDefinitions().addChildElement(process);
    }

    private void handleStartEvent(CommonBPMNIdEntity entity, BpmnModelInstance model, List<CommonBPMNIdEntity> entities) { 
        throwIfCannotGetModel(model);
        StartEvent startEventEntity = (StartEvent) entity;

        org.camunda.bpm.model.bpmn.instance.StartEvent startEvent =
            model.newInstance(org.camunda.bpm.model.bpmn.instance.StartEvent.class);
        startEvent.setId(startEventEntity.getId());

        attachToProcess(model, startEvent, startEventEntity.getProcessId());
    }

    private void handleTask(CommonBPMNIdEntity entity, BpmnModelInstance model, List<CommonBPMNIdEntity> entities) {
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


    private void handleGateway(CommonBPMNIdEntity entity, BpmnModelInstance model, List<CommonBPMNIdEntity> entities) {
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

    private void handleEndEvent(CommonBPMNIdEntity entity, BpmnModelInstance model, List<CommonBPMNIdEntity> entities) {
        throwIfCannotGetModel(model);
        EndEvent endEventEntity = (EndEvent) entity;

        org.camunda.bpm.model.bpmn.instance.EndEvent endEvent =
            model.newInstance(org.camunda.bpm.model.bpmn.instance.EndEvent.class);
        endEvent.setId(endEventEntity.getId());

        attachToProcess(model, endEvent, endEventEntity.getProcessId());
    }

    private void handleDefault(CommonBPMNIdEntity entity, BpmnModelInstance model, List<CommonBPMNIdEntity> entities) { 
        throw new RuntimeException(
            "Could not map entity because the type isn't specified in modeler: " + entity.getClass().getName()
        );
    }

    private void throwIfCannotGetModel(BpmnModelInstance model) {
        if (model == null) {
            throw new RuntimeException("Cannot get model while trying to model");
        }
    }

    private void throwIfCannotGetConnectionType(ConnectionBPMNEntity connection) {
        if (connection.getType() == null) {
        throw new RuntimeException("Connection type is null for connection between "
                + connection.getSourceId() + " and " + connection.getTargetId());
        }
    }
    
    private void attachToProcess(BpmnModelInstance model, org.camunda.bpm.model.bpmn.instance.FlowNode node, String processId) {
        try {
            org.camunda.bpm.model.bpmn.instance.SubProcess subProcess = model.getModelElementById(processId);
            if (subProcess != null) {
                subProcess.addChildElement(node);
            } else {
                org.camunda.bpm.model.bpmn.instance.Process process = model.getModelElementById(processId);
                if (process != null) {
                    process.addChildElement(node);
                } else {
                    throw new RuntimeException("Process with ID '" + processId + "' not found for element '" + node.getId() + "'.");
                }
            }
        } catch (Exception ex) {
            org.camunda.bpm.model.bpmn.instance.Process process = model.getModelElementById(processId);
            if (process != null) {
                process.addChildElement(node);
            } else {
                throw new RuntimeException("Process with ID '" + processId + "' not found for element '" + node.getId() + "'.");
            }
        }
    }

    private org.camunda.bpm.model.bpmn.instance.Gateway findGateway(BpmnModelInstance model, String gatewayId) {
        org.camunda.bpm.model.bpmn.instance.Gateway gateway = model.getModelElementById(gatewayId);

        if (gateway == null) {
            throw new RuntimeException("Could not find gateway with id: " + gatewayId);
        }
        return gateway;
    }

    private String getPoolProcess(String poolId, List<CommonBPMNIdEntity> entities) {
        Optional<CommonBPMNIdEntity> poolEntityOpt = entities.stream()
                .filter(e -> poolId.equals(e.getId()))
                .findFirst();

        if (poolEntityOpt.isEmpty()) {
            return poolId + "Process";
        }

        CommonBPMNIdEntity poolEntity = poolEntityOpt.get();
        int index = entities.indexOf(poolEntity);

        if (index + 1 < entities.size()) {
            CommonBPMNIdEntity processEntity = entities.get(index + 1);
            if (processEntity.getClass() == Process.class) {
                return processEntity.getId();
            }
            return poolId + "Process";
        }
        return poolId + "Process";
    }

    private List<ConnectionBPMNEntity> handleSequentialAndMessageEntities(List<ConnectionBPMNEntity> entities) {
        List<ConnectionBPMNEntity> mutableEntities = new ArrayList<>(entities);

        for (int i = 1; i < mutableEntities.size(); i++) {
            var prev = mutableEntities.get(i - 1);
            var current = mutableEntities.get(i);

            if (areTheSameConnectionEntity(prev, current)) {
                var clearIndex = (prev.getType() == ConnectionType.SEQUENCE) ? (i - 1) : i;
                mutableEntities.remove(clearIndex);
                if (i > 1) {
                  i--;  
                }
            }
        }
        return mutableEntities;
    }


    private boolean areTheSameConnectionEntity(ConnectionBPMNEntity prev, ConnectionBPMNEntity current) {
        return  (prev.getSourceId() == null ? current.getSourceId() == null : prev.getSourceId().equals(current.getSourceId()))
                && (prev.getTargetId() == null ? current.getTargetId() == null : prev.getTargetId().equals(current.getTargetId()));
    }
 }
