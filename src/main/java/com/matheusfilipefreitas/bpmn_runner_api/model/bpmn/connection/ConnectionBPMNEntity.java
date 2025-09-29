package com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.connection;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

@Getter

public class ConnectionBPMNEntity {
    private String entityId1;
    private String entityId2;

    public ConnectionBPMNEntity findConnectionById(String id) {
        return entityId1.equals(id) || entityId2.equals(id) ? this : null;
    }

    public List<String> getConnectionEntityIdsAsList() {
        return new ArrayList<>(List.of(this.entityId1, this.entityId2));
    }

    @Override
    public String toString() {
        return "Connection{" +
                "entityId1='" + entityId1 + '\'' +
                ", entityId2='" + entityId2 + '\'' +
                '}';
    }
}
