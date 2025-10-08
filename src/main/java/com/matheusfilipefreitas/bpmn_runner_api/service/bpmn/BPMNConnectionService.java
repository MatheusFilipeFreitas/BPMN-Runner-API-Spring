package com.matheusfilipefreitas.bpmn_runner_api.service.bpmn;

import java.util.List;

import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.connection.ConnectionBPMNEntity;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.ElementInfo;
import com.matheusfilipefreitas.bpmn_runner_api.service.common.genericcrudservice.CommonCrudService;

public interface BPMNConnectionService extends CommonCrudService<ConnectionBPMNEntity, String>{
    void saveConnectionsFromElementInfo(List<ElementInfo> elementsInfo);
    void resetConnections();
}
