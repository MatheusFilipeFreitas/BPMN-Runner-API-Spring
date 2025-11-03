package com.matheusfilipefreitas.bpmn_runner_api.model.script.element;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.matheusfilipefreitas.bpmn_runner_api.common.exception.interpreter.InterpreterException;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.types.BranchType;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.types.ElementType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ElementParallelBranch extends ElementBranch {
    private final List<LinkedHashMap<String, ElementType>> childrenIdsMap;

    public ElementParallelBranch(String gatewayId, List<LinkedHashMap<String, ElementType>> childrenIdsMap) {
        super(gatewayId, BranchType.PARALLEL);
        this.childrenIdsMap = childrenIdsMap;
    }

    public void throwIfHasEndEvent() {
        this.childrenIdsMap.forEach(scope -> {
            var elementTypes = scope.values();
            List<ElementType> invalidTypes = elementTypes.stream().filter(t -> t.equals(ElementType.END_EVENT)).toList();
            if (!invalidTypes.isEmpty()) {
                throw new InterpreterException("Cannot use parallel gateway with an end element inside one of the scopes in gateway: " + this.gatewayId);
            }
        });
    }

    public boolean areIdsFromDifferentBranches(String currentId, String nextId) {
        int currentIndex = -1;
        int nextIndex = -1;

        for (int i = 0; i < this.childrenIdsMap.size(); i++) {
            LinkedHashMap<String, ElementType> scope = this.childrenIdsMap.get(i);
            if (scope.containsKey(currentId)) {
                currentIndex = i;
            }
            if (scope.containsKey(nextId)) {
                nextIndex = i;
            }
        }

        if (currentIndex == -1 || nextIndex == -1) {
            return true;
        }
        return currentIndex != nextIndex;
    }

    public String findFirstElementInScope(LinkedHashMap<String, ElementType> scope) {
        return findElementInIndex(scope, 0);
    }

    public String findLastElementinScope(LinkedHashMap<String, ElementType> scope) {
        return findElementInIndex(scope, scope.size() - 1);
    }

    public LinkedHashMap<String, ElementType> getScopeFromElementId(String elementId) {
        for (var scope : this.childrenIdsMap) {
            ElementType elementType = scope.get(elementId);
            if (elementType != null) {
                return scope;
            }
        }
        return null;
    }

    public void throwIfBranchHasEndElement() {
        for (var scope : this.childrenIdsMap) {
            List<ElementType> types = scope.values().stream().filter(e -> e.equals(ElementType.END_EVENT)).toList();
            if (!types.isEmpty()) {
                throw new InterpreterException("Invalid end element inside parallel gateway with id: " + this.gatewayId);
            }
        }
    }

    public boolean hasScopeMessageElements(LinkedHashMap<String, ElementType> scope) {
        List<ElementType> types = scope.values().stream().filter(t -> t.equals(ElementType.MESSAGE)).toList();
        return types != null && !types.isEmpty();
    }

    public String findLastElementInBranch() {
        int lastIndex = this.childrenIdsMap.size() - 1;
        LinkedHashMap<String, ElementType> lastScope = this.childrenIdsMap.get(lastIndex);
        return findElementInIndex(lastScope, lastScope.size() - 1);
    }

    private String findElementInIndex(LinkedHashMap<String, ElementType> scope, int index) {
        if (scope == null || scope.isEmpty()) return null;

        List<String> keys = scope.entrySet().stream()
            .filter(entry -> entry.getValue() != ElementType.MESSAGE)
            .map(Map.Entry::getKey)
            .toList();

        if (index != 0)
        {
            index = keys.size() - 1;
        }

        if (index < 0 || index >= keys.size()) return null;
        return keys.get(index);
    }
}
