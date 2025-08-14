package com.matheusfilipefreitas.bpmn_runner_api.repository.bpmn.implementation;

import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.common.CommonBPMNIdEntity;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.connection.ConnectionBPMNEntity;
import com.matheusfilipefreitas.bpmn_runner_api.repository.bpmn.BPMNEntitiesRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@NoArgsConstructor

@Repository
public class BPMNEntitiesRepositoryImpl implements BPMNEntitiesRepository {

    private ConcurrentHashMap<String, CommonBPMNIdEntity> entities = new ConcurrentHashMap<>();
    private ArrayList<ConnectionBPMNEntity> connectionsBetweenEntities = new ArrayList();

    public void addEntity(CommonBPMNIdEntity entity) {
        this.entities.put(entity.getId(), entity);
    }

    public void deleteEntity(String id) {
        this.entities.remove(id);
    }

    public void addConnection(String entityId1, String entityId2) {
        this.connectionsBetweenEntities.add(new ConnectionBPMNEntity(entityId1, entityId2));
    }

    public Optional<ConnectionBPMNEntity> getConnectionByEntityId(String id) {
        return this.connectionsBetweenEntities
                .stream()
                .filter(c -> c.findConnectionById(id) != null)
                .findFirst();
    }

    public void resetConnections() {
        this.connectionsBetweenEntities.clear();
    }
}
