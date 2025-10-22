package com.matheusfilipefreitas.bpmn_runner_api.helper.validators.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import org.springframework.stereotype.Component;

import com.matheusfilipefreitas.bpmn_runner_api.common.exception.interpreter.InterpreterException;
import com.matheusfilipefreitas.bpmn_runner_api.helper.validators.ElementIdsValidator;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.connection.ConnectionBPMNEntity;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.ElementInfo;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.types.ElementType;

@Component
public class ElementIdsValidatorImpl implements ElementIdsValidator {
    final List<String> errorMessages = new ArrayList<>();

    @Override
    public void validateAllElementsInConnections(List<String> elementsIds, List<ConnectionBPMNEntity> connections) {
        final List<String> allErrors = new ArrayList<>();

        List<String> uniqueIds = new ArrayList<>(new LinkedHashSet<>(elementsIds));
        for (String id : uniqueIds) {
            String idNotInConnection = findIdInConnections(id, connections);
            if (idNotInConnection != null) {
                allErrors.add(idNotInConnection);
            }
        }

        if (!allErrors.isEmpty()) {
            throw new InterpreterException(allErrors);
        }
    }

    @Override
    public void validate(List<ElementInfo> rawElements) {
        final List<String> allErrors = new ArrayList<>();

        Map<Boolean, List<ElementInfo>> partitionedElements = rawElements.stream()
            .collect(partitioningBy(element -> element.getElementType() == ElementType.MESSAGE));
        
        List<ElementInfo> creationElements = partitionedElements.get(false);
        List<ElementInfo> pointerElements = partitionedElements.get(true);

        allErrors.addAll(findDuplicatedIds(creationElements));
        allErrors.addAll(findUnresolvedMessageIds(creationElements, pointerElements));
        if (!allErrors.isEmpty()) {
            throw new InterpreterException(allErrors);
        }
    }

    private List<String> findUnresolvedMessageIds(List<ElementInfo> creationElements, List<ElementInfo> pointerElements) {
        Set<String> createdIds = creationElements.stream()
            .map(ElementInfo::getId)
            .collect(toSet());
            
        List<String> notFoundIds = pointerElements.stream()
            .map(ElementInfo::getId)
            .filter(id -> !createdIds.contains(id))
            .collect(toList());

        if (!notFoundIds.isEmpty()) {
            String error = "Não foi possível encontrar o elemento de destino para as mensagens com os seguintes IDs: " + notFoundIds;
            return Collections.singletonList(error);
        }
        return Collections.emptyList();
    }

    private List<String> findDuplicatedIds(List<ElementInfo> creationElements) {
        Set<String> uniqueIds = new HashSet<>();
        List<String> duplicatedIds = creationElements.stream()
            .map(ElementInfo::getId)
            .filter(id -> !uniqueIds.add(id))
            .collect(toList());

        if (!duplicatedIds.isEmpty()) {
            return Collections.singletonList("IDs duplicados encontrados: " + duplicatedIds);
        }
        return Collections.emptyList();
    }

    private String findIdInConnections(String id, List<ConnectionBPMNEntity> connections) {
        Optional<ConnectionBPMNEntity> connection = connections.stream().filter(c -> c.getSourceId().equals(id) || c.getTargetId().equals(id)).findAny();
        if (connection.isEmpty()) {
            return "Element with id: '" + id + "' does not have any connection flow, problably caused by message flow.";
        }
        return null;
    }
}
