package com.mnrclara.api.cg.setup.repository.specification;


import com.mnrclara.api.cg.setup.model.controlgrouptype.ControlGroupType;
import com.mnrclara.api.cg.setup.model.controlgrouptype.FindControlGroupType;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ControlGroupTypeSpecification implements Specification<ControlGroupType> {

    FindControlGroupType findControlGroupType;

    public ControlGroupTypeSpecification(FindControlGroupType inputSearchParams) {
        this.findControlGroupType = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<ControlGroupType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (findControlGroupType.getCompanyId() != null && !findControlGroupType.getCompanyId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("companyId");
            predicates.add(group.in(findControlGroupType.getCompanyId()));
        }
        if (findControlGroupType.getLanguageId() != null && !findControlGroupType.getLanguageId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("languageId");
            predicates.add(group.in(findControlGroupType.getLanguageId()));
        }
        if (findControlGroupType.getGroupTypeId() != null && !findControlGroupType.getGroupTypeId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("groupTypeId");
            predicates.add(group.in(findControlGroupType.getGroupTypeId()));
        }
        if (findControlGroupType.getVersionNumber() != null && !findControlGroupType.getVersionNumber().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("versionNumber");
            predicates.add(group.in(findControlGroupType.getVersionNumber()));
        }
        if (findControlGroupType.getStatusId() != null && !findControlGroupType.getStatusId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("statusId");
            predicates.add(group.in(findControlGroupType.getStatusId()));
        }
        if (findControlGroupType.getFromDate() != null && findControlGroupType.getToDate() != null) {
            predicates.add(cb.between(root.get("createdOn"), findControlGroupType.getFromDate(), findControlGroupType.getToDate()));
        }
        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
        return cb.and(predicates.toArray(new Predicate[]{}));
    }
}
