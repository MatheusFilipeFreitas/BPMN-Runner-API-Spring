package com.matheusfilipefreitas.bpmn_runner_api.repository.bpmn;

import java.util.List;
import java.util.Optional;

import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.common.CommonBPMNIdEntity;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.connection.ConnectionBPMNEntity;

public interface BPMNEntitiesRepository {
    List<CommonBPMNIdEntity> getAllEntities();
    Optional<CommonBPMNIdEntity> getEntityById(String id);
    List<ConnectionBPMNEntity> getAllConnections();
    void addEntity(CommonBPMNIdEntity entity);
    void deleteEntity(String id);
    void addConnection(ConnectionBPMNEntity connection);
    Optional<ConnectionBPMNEntity> getConnectionByEntityId(String id);
    void resetConnections();
    void resetEntities();
}
