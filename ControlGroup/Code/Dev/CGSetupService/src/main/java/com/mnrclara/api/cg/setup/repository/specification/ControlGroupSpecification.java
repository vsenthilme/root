package com.mnrclara.api.cg.setup.repository.specification;

import com.mnrclara.api.cg.setup.model.controlgroup.ControlGroup;
import com.mnrclara.api.cg.setup.model.controlgroup.FindControlGroup;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ControlGroupSpecification implements Specification<ControlGroup> {

    FindControlGroup findControlGroup;

    public ControlGroupSpecification(FindControlGroup inputSearchParams) {
        this.findControlGroup = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<ControlGroup> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (findControlGroup.getCompanyId() != null && !findControlGroup.getCompanyId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("companyId");
            predicates.add(group.in(findControlGroup.getCompanyId()));
        }
        if (findControlGroup.getLanguageId() != null && !findControlGroup.getLanguageId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("languageId");
            predicates.add(group.in(findControlGroup.getLanguageId()));
        }
        if (findControlGroup.getGroupId() != null && !findControlGroup.getGroupId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("groupId");
            predicates.add(group.in(findControlGroup.getGroupId()));
        }
        if (findControlGroup.getGroupTypeId() != null && !findControlGroup.getGroupTypeId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("groupTypeId");
            predicates.add(group.in(findControlGroup.getGroupTypeId()));
        }
        if (findControlGroup.getVersionNumber() != null && !findControlGroup.getVersionNumber().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("versionNumber");
            predicates.add(group.in(findControlGroup.getVersionNumber()));
        }
        if (findControlGroup.getStatusId() != null && !findControlGroup.getStatusId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("statusId");
            predicates.add(group.in(findControlGroup.getStatusId()));
        }
        if (findControlGroup.getFromDate() != null && findControlGroup.getToDate() != null) {
            predicates.add(cb.between(root.get("createdOn"), findControlGroup.getFromDate(), findControlGroup.getToDate()));
        }
        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
        return cb.and(predicates.toArray(new Predicate[]{}));
    }
}
