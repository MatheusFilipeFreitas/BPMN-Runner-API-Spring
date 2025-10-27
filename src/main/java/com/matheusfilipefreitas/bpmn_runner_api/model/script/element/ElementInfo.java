package com.matheusfilipefreitas.bpmn_runner_api.model.script.element;

import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.types.ElementType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ElementInfo {
    private final String id;
    private final ElementType elementType;
    private final String label;
    private final String type;
    private final String processId;
    private Integer index;

    public ElementInfo(String id, ElementType elementType, String label, String type, String processId, Integer index) {
        this.id = id;
        this.elementType = elementType;
        this.label = label;
        this.type = type;
        this.processId = processId;
        this.index = index;
    }

    public ElementInfo(String id, ElementType elementType, String processId, Integer index) {
        this(id, elementType, null, null, processId, index);
    }

    public ElementInfo(String id, ElementType elementType, String label, String processId, Integer index) {
        this(id, elementType, label, null, processId, index);
    }

    @Override
    public String toString() {
        return "ElementInfo{" +
                "id='" + id + '\'' +
                ", elementType='" + elementType + '\'' +
                (label != null ? ", label='" + label + '\'' : "") +
                (type != null ? ", type='" + type + '\'' : "") +
                (processId != null ? "/ processId='" + processId + '\'' : "") +
                (index != null ? "/ index='" + index + '\'' : "") +
                '}';
    }
}

