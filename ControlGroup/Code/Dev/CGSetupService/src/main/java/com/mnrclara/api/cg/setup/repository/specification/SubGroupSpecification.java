package com.mnrclara.api.cg.setup.repository.specification;

import com.mnrclara.api.cg.setup.model.subgrouptype.FindSubGroupType;
import com.mnrclara.api.cg.setup.model.subgrouptype.SubGroupType;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class SubGroupSpecification implements Specification<SubGroupType> {

    FindSubGroupType findSubGroupType;

    public SubGroupSpecification(FindSubGroupType inputSearchParams) {
        this.findSubGroupType = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<SubGroupType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (findSubGroupType.getSubGroupTypeId() != null && !findSubGroupType.getSubGroupTypeId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("subGroupTypeId");
            predicates.add(group.in(findSubGroupType.getSubGroupTypeId()));
        }
        if (findSubGroupType.getCompanyId() != null && !findSubGroupType.getCompanyId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("companyId");
            predicates.add(group.in(findSubGroupType.getCompanyId()));
        }
        if (findSubGroupType.getLanguageId() != null && !findSubGroupType.getLanguageId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("languageId");
            predicates.add(group.in(findSubGroupType.getLanguageId()));
        }
        if (findSubGroupType.getGroupTypeId() != null && !findSubGroupType.getGroupTypeId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("groupTypeId");
            predicates.add(group.in(findSubGroupType.getGroupTypeId()));
        }
        if (findSubGroupType.getVersionNumber() != null && !findSubGroupType.getVersionNumber().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("versionNumber");
            predicates.add(group.in(findSubGroupType.getVersionNumber()));
        }
        if (findSubGroupType.getStatusId() != null && !findSubGroupType.getStatusId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("statusId");
            predicates.add(group.in(findSubGroupType.getStatusId()));
        }

        if (findSubGroupType.getFromDate() != null && findSubGroupType.getToDate() != null) {
            predicates.add(cb.between(root.get("createdOn"), findSubGroupType.getFromDate(), findSubGroupType.getToDate()));
        }

        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
        return cb.and(predicates.toArray(new Predicate[]{}));
    }
}
