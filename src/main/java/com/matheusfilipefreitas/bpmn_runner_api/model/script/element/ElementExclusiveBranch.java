package com.matheusfilipefreitas.bpmn_runner_api.model.script.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.types.BranchOrder;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.types.BranchType;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.types.ElementType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ElementExclusiveBranch extends ElementBranch {
    private final BranchOrder firstOrderBranch;
    private final LinkedHashMap<String, ElementType> yesChildrenIdsMap;
    private final LinkedHashMap<String, ElementType> noChildrenIdsMap;

    public ElementExclusiveBranch(String gatewayId, LinkedHashMap<String, ElementType> yesChildrenIdsMap, LinkedHashMap<String, ElementType> noChildrenIdsMap, BranchOrder order) {
        super(gatewayId, BranchType.EXCLUSIVE);
        this.yesChildrenIdsMap = yesChildrenIdsMap;
        this.noChildrenIdsMap = noChildrenIdsMap;
        this.firstOrderBranch = order;
    }
    
    public BranchOrder getIdInsideBranches(String id) {
        ElementType yesElement = yesChildrenIdsMap.getOrDefault(id, null);
        ElementType noElement = noChildrenIdsMap.getOrDefault(id, null);
        if (yesElement != null)
            return BranchOrder.YES;
        if (noElement != null)
            return BranchOrder.NO;
        return null;
    }

    public String getLastElementFromBranchs() {
        if (firstOrderBranch == BranchOrder.YES) {
            return getLastNoElement();
        } else if (firstOrderBranch == BranchOrder.NO) {
            return getLastYesElement();
        }
        return null;
    }

    public String getLastYesElement() {
        return findElementInIndex(yesChildrenIdsMap, yesChildrenIdsMap.size() - 1);
    }

    public String getLastNoElement() {
        return findElementInIndex(noChildrenIdsMap, noChildrenIdsMap.size() - 1);
    }

    public String getFirstYesElement() {
        return findElementInIndex(yesChildrenIdsMap, 0);
    }

    public String getFirstNoElement() {
        return findElementInIndex(noChildrenIdsMap, 0);
    }

    public boolean hasYesBranchMessageElements() {
        return hasMessageElementInBranch(yesChildrenIdsMap);
    }

    public boolean hasNoBranchMessageElements() {
        return hasMessageElementInBranch(noChildrenIdsMap);
    }

    private String findElementInIndex(LinkedHashMap<String, ElementType> branchs, int index) {
        if (branchs.isEmpty()) return null;
        List<String> keys = new ArrayList<>(branchs.keySet());
        return keys.get(index);
    }

    private boolean hasMessageElementInBranch(LinkedHashMap<String, ElementType> branchs) {
        if (branchs.isEmpty()) return false;
        Collection<ElementType> types = branchs.values();
        List<ElementType> messageElements = types.stream().filter(e -> e == ElementType.MESSAGE).toList();
        return messageElements.size() > 0;
    }
}
