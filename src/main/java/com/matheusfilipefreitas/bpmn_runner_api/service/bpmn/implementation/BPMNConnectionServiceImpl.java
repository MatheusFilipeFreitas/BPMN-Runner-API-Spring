package com.matheusfilipefreitas.bpmn_runner_api.service.bpmn.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.matheusfilipefreitas.bpmn_runner_api.common.exception.notfound.NotFoundException;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.common.CommonBPMNIdEntity;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.connection.ConnectionBPMNEntity;
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

    private List<ConnectionBPMNEntity> getConnectionEntityFromElementInfoList(List<ElementInfo> elementsInfo) {
        List<ConnectionBPMNEntity> entities = new ArrayList<>();
        for (int i = 1; i < elementsInfo.size(); i++) {
            ElementInfo currentElement = elementsInfo.get(i);
            if (currentElement.getElementType() == ElementType.MESSAGE) {
                ElementInfo lastElement = elementsInfo.get(i - 1);
                entities.add(
                    new ConnectionBPMNEntity(lastElement.getId(), currentElement.getId())
                );
            }
        }
        return entities;
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
