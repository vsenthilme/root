package com.mnrclara.api.cg.transaction.repository.specification;


import com.mnrclara.api.cg.transaction.model.bseffectivecontrol.BSEffectiveControl;
import com.mnrclara.api.cg.transaction.model.bseffectivecontrol.FindBSEffectiveControl;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class BSEffectiveControlSpecification implements Specification<BSEffectiveControl> {

    FindBSEffectiveControl findBSEffectiveControl;

    public BSEffectiveControlSpecification(FindBSEffectiveControl inputSearchParams) {
        this.findBSEffectiveControl = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<BSEffectiveControl> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (findBSEffectiveControl.getValidationId() != null && !findBSEffectiveControl.getValidationId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("validationId");
            predicates.add(group.in(findBSEffectiveControl.getValidationId()));
        }

        if (findBSEffectiveControl.getGroupId() != null && !findBSEffectiveControl.getGroupId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("groupId");
            predicates.add(group.in(findBSEffectiveControl.getGroupId()));
        }

        if (findBSEffectiveControl.getSubGroupId() != null && !findBSEffectiveControl.getSubGroupId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("subGroupId");
            predicates.add(group.in(findBSEffectiveControl.getSubGroupId()));
        }

        if (findBSEffectiveControl.getClientId() != null && !findBSEffectiveControl.getClientId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("clientId");
            predicates.add(group.in(findBSEffectiveControl.getClientId()));
        }
        if (findBSEffectiveControl.getLanguageId() != null && !findBSEffectiveControl.getLanguageId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("languageId");
            predicates.add(group.in(findBSEffectiveControl.getLanguageId()));
        }
        if (findBSEffectiveControl.getCompanyId() != null && !findBSEffectiveControl.getCompanyId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("companyId");
            predicates.add(group.in(findBSEffectiveControl.getCompanyId()));
        }
        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
        return cb.and(predicates.toArray(new Predicate[]{}));
    }

}
