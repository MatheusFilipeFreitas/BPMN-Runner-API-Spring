package com.matheusfilipefreitas.bpmn_runner_api.service.bpmn.implementation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.matheusfilipefreitas.bpmn_runner_api.common.exception.interpreter.InterpreterException;
import com.matheusfilipefreitas.bpmn_runner_api.common.exception.notfound.NotFoundException;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.common.CommonBPMNIdEntity;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.connection.ConnectionBPMNEntity;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.types.ConnectionType;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.ElementInfo;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.types.ElementType;
import com.matheusfilipefreitas.bpmn_runner_api.repository.bpmn.BPMNEntitiesRepository;
import com.matheusfilipefreitas.bpmn_runner_api.service.bpmn.BPMNConnectionService;

import lombok.AllArgsConstructor;

@AllArgsConstructor

@Service
public class BPMNConnectionServiceImpl implements BPMNConnectionService {
    private final BPMNEntitiesRepository repository;

    @Override
    public List<ConnectionBPMNEntity> findAll() {
        return this.repository.getAllConnections();
    }

    @Override
    public ConnectionBPMNEntity findById(String id) {
        Optional<ConnectionBPMNEntity> connection = this.repository.getConnectionByEntityId(id);
        if (connection.isEmpty()) {
            throw new NotFoundException("Could not find connection entity with id: " + id);
        }
        return connection.get();
    }

    @Override
    public void save(ConnectionBPMNEntity entity) {
        repository.addConnection(entity);
    }

    @Override
    public void saveConnectionsFromElementInfo(List<ElementInfo> elementsInfo) {
        List<ConnectionBPMNEntity> connections = getConnectionEntityFromElementInfoList(elementsInfo);
        for (ConnectionBPMNEntity connection : connections) {
            throwIfEntitiesWereNotCreated(connection.getConnectionEntityIdsAsList());
            save(connection);
        }
    }

    @Override
    public void resetConnections() {
        repository.resetConnections();
    }

    private List<ConnectionBPMNEntity> getConnectionEntityFromElementInfoList(List<ElementInfo> elementsInfo) {
        var orderedElementsInfo = elementsInfo.stream()
            .sorted(Comparator.comparing(ElementInfo::getIndex))
            .filter(element -> element.getElementType() != ElementType.POOL &&
                element.getElementType() != ElementType.PROCESS).toList();

        return buildConnectionsFromFlow(orderedElementsInfo);
    }

    private List<ConnectionBPMNEntity> buildConnectionsFromFlow(List<ElementInfo> elements) {
        List<ConnectionBPMNEntity> connections = new ArrayList<>();
        int indexDefinition = 0;

        int maxIterations = elements.size() * 5;
        int iterationCount = 0;

        Set<Integer> visitedIndexes = new HashSet<>();

        for (int i = 1; i < elements.size(); i++) {
            iterationCount++;
            if (iterationCount > maxIterations) {
                throw new InterpreterException("Possible circular flow detected in line: " + elements.get(i).getIndex());
            }

            var prev = elements.get(i - 1);
            var current = elements.get(i);

            if (!visitedIndexes.add(i)) {
                throw new InterpreterException("Possible circular flow detected in line: " + elements.get(i).getIndex());
            }

            if (current.getElementType() == ElementType.MESSAGE) {
                OptionalInt indexOpt = IntStream.range(0, elements.size())
                    .filter(t -> elements.get(t).getId().equals(current.getId())
                                && elements.get(t).getElementType() != ElementType.MESSAGE)
                    .findFirst();

                if (indexOpt.isPresent()) {
                    int targetIndex = indexOpt.getAsInt();
                    connections.add(new ConnectionBPMNEntity(
                        prev.getId(), current.getId(), ConnectionType.MESSAGE, null, indexDefinition++
                    ));

                    Optional<ConnectionBPMNEntity> registeredRecord = connections.stream()
                        .filter(r -> r.getSourceId().equals(current.getId()))
                        .findFirst();

                    if (!registeredRecord.isPresent()) {
                        visitedIndexes.clear();
                        i = targetIndex;
                    }
                }
            } else if (current.getElementType() != ElementType.END_EVENT) {
                connections.add(new ConnectionBPMNEntity(
                    prev.getId(), current.getId(), ConnectionType.SEQUENCE, null, indexDefinition++
                ));
            }
            // handle exclusive and parallel...
        }

        return connections.stream()
            .sorted(Comparator.comparing(ConnectionBPMNEntity::getIndex))
            .toList();
    }


    private void throwIfEntitiesWereNotCreated(List<String> ids) {
        for (String id : ids) {
            Optional<CommonBPMNIdEntity> entity = repository.getEntityById(id);
            if (entity.isEmpty()) {
                throw new NotFoundException("Could not find entity with id: " + id);
            }
        }
    }
}
