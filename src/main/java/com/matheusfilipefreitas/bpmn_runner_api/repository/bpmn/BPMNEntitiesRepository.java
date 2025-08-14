package com.matheusfilipefreitas.bpmn_runner_api.repository.bpmn;

import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.common.CommonBPMNIdEntity;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.connection.ConnectionBPMNEntity;

import java.util.Optional;

public interface BPMNEntitiesRepository {
    void addEntity(CommonBPMNIdEntity entity);
    void deleteEntity(String id);
    void addConnection(String entityId1, String entityId2);
    Optional<ConnectionBPMNEntity> getConnectionByEntityId(String id);
    void resetConnections();
}
