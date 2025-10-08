package com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.connection;

import java.util.ArrayList;
import java.util.List;

import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.types.ConnectionType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

@Getter

public class ConnectionBPMNEntity {
    private String sourceId;
    private String targetId;
    private ConnectionType type;
    private String label;
    private int index;

    public ConnectionBPMNEntity findConnectionById(String id) {
        return sourceId.equals(id) || targetId.equals(id) ? this : null;
    }

    public List<String> getConnectionEntityIdsAsList() {
        return new ArrayList<>(List.of(this.sourceId, this.targetId));
    }

    public boolean hasCondition() {
        return this.type == ConnectionType.EXCLUSIVE;
    } 

    @Override
    public String toString() {
        return "Connection{" +
                "sourceId='" + sourceId + '\'' +
                ", targetId='" + targetId + '\'' +
                ", type='" + type.name() + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
