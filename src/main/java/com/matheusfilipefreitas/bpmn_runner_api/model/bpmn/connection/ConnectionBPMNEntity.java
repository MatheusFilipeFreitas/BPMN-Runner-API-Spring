package com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.connection;

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
}
