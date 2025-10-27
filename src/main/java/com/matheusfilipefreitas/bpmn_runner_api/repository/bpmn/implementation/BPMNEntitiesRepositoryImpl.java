package com.matheusfilipefreitas.bpmn_runner_api.repository.bpmn.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.Gateway;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.common.CommonBPMNIdEntity;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.connection.ConnectionBPMNEntity;
import com.matheusfilipefreitas.bpmn_runner_api.repository.bpmn.BPMNEntitiesRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

@Repository
public class BPMNEntitiesRepositoryImpl implements BPMNEntitiesRepository {
    private ConcurrentHashMap<String, CommonBPMNIdEntity> entities = new ConcurrentHashMap<>();
    private List<ConnectionBPMNEntity> connectionsBetweenEntities = new ArrayList<>();

    @Override
    public void addEntity(CommonBPMNIdEntity entity) {
        this.entities.put(entity.getId(), entity);
    }

    @Override
    public void addGatewayClosingEntity(Gateway closingGateway, String lastBranchElementId) {
        List<CommonBPMNIdEntity> entitiesList = new ArrayList<>(this.entities.values());
        Collections.sort(entitiesList, (e1, e2) -> Integer.compare(e1.getIndex(), e2.getIndex()));
        int index = entitiesList.stream().map(e -> e.getId()).toList().indexOf(lastBranchElementId);
        closingGateway.setIndex(index);
        for (int i = index; i < entitiesList.size() - 1; i++) {
            this.entities.replace(entitiesList.get(i).getId(), entitiesList.get(i));
        }
        this.entities.put(closingGateway.getId(), closingGateway);
    }

    @Override
    public void deleteEntity(String id) {
        this.entities.remove(id);
    }

    @Override
    public void addConnection(ConnectionBPMNEntity connection) {
        this.connectionsBetweenEntities.add(connection);
    }

    @Override
    public Optional<ConnectionBPMNEntity> getConnectionByEntityId(String id) {
        return this.connectionsBetweenEntities
                .stream()
                .filter(c -> c.findConnectionById(id) != null)
                .findFirst();
    }

    @Override
    public void resetConnections() {
        this.connectionsBetweenEntities.clear();
        this.connectionsBetweenEntities = new ArrayList<>();
        this.entities.clear();
        this.entities = new ConcurrentHashMap<>();
    }

    @Override
    public void resetEntities() {
        this.entities.clear();
    }

    @Override
    public List<CommonBPMNIdEntity> getAllEntities() {
        return this.entities.values()
            .stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<CommonBPMNIdEntity> getEntityById(String id) {
        return Optional.ofNullable(this.entities.get(id));
    }

    @Override
    public List<ConnectionBPMNEntity> getAllConnections() {
        return this.connectionsBetweenEntities;
    }
}
