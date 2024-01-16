package com.mnrclara.api.cg.setup.repository.specification;

import com.mnrclara.api.cg.setup.model.clientcontrolgroup.ClientControlGroup;
import com.mnrclara.api.cg.setup.model.clientcontrolgroup.FindClientControlGroup;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ClientControlGroupSpecification implements Specification<ClientControlGroup> {
    FindClientControlGroup findClientControlGroup;

    public ClientControlGroupSpecification(FindClientControlGroup inputSearchParams) {
        this.findClientControlGroup = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<ClientControlGroup> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (findClientControlGroup.getCompanyId() != null && !findClientControlGroup.getCompanyId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("companyId");
            predicates.add(group.in(findClientControlGroup.getCompanyId()));
        }
        if (findClientControlGroup.getLanguageId() != null && !findClientControlGroup.getLanguageId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("languageId");
            predicates.add(group.in(findClientControlGroup.getLanguageId()));
        }
        if (findClientControlGroup.getClientId() != null && !findClientControlGroup.getClientId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("clientId");
            predicates.add(group.in(findClientControlGroup.getClientId()));
        }
        if (findClientControlGroup.getSubGroupTypeId() != null && !findClientControlGroup.getSubGroupTypeId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("subGroupTypeId");
            predicates.add(group.in(findClientControlGroup.getSubGroupTypeId()));
        }
        if (findClientControlGroup.getGroupTypeId() != null && !findClientControlGroup.getGroupTypeId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("groupTypeId");
            predicates.add(group.in(findClientControlGroup.getGroupTypeId()));
        }
        if (findClientControlGroup.getVersionNumber() != null && !findClientControlGroup.getVersionNumber().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("versionNumber");
            predicates.add(group.in(findClientControlGroup.getVersionNumber()));
        }
        if (findClientControlGroup.getStatusId() != null && !findClientControlGroup.getStatusId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("statusId");
            predicates.add(group.in(findClientControlGroup.getStatusId()));
        }
        if (findClientControlGroup.getFromDate() != null && findClientControlGroup.getToDate() != null) {
            predicates.add(cb.between(root.get("createdOn"), findClientControlGroup.getFromDate(), findClientControlGroup.getToDate()));
        }

        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
        return cb.and(predicates.toArray(new Predicate[]{}));
    }
}
