package com.mnrclara.api.cg.setup.repository.specification;

import com.mnrclara.api.cg.setup.model.relationshipid.FindRelationShipId;
import com.mnrclara.api.cg.setup.model.relationshipid.RelationShipId;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class RelationShipIdSpecification implements Specification<RelationShipId> {

    FindRelationShipId findRelationShipId;

    public RelationShipIdSpecification(FindRelationShipId inputSearchParams) {
        this.findRelationShipId = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<RelationShipId> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (findRelationShipId.getRelationShipId() != null && !findRelationShipId.getRelationShipId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("relationShipId");
            predicates.add(group.in(findRelationShipId.getRelationShipId()));
        }
        if (findRelationShipId.getLanguageId() != null && !findRelationShipId.getLanguageId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("languageId");
            predicates.add(group.in(findRelationShipId.getLanguageId()));
        }
        if (findRelationShipId.getCompanyId() != null && !findRelationShipId.getCompanyId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("companyId");
            predicates.add(group.in(findRelationShipId.getCompanyId()));
        }
        if (findRelationShipId.getStatusId() != null && !findRelationShipId.getStatusId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("statusId");
            predicates.add(group.in(findRelationShipId.getStatusId()));
        }
        if (findRelationShipId.getFromDate() != null && findRelationShipId.getToDate() != null) {
            predicates.add(cb.between(root.get("createdOn"), findRelationShipId.getFromDate(), findRelationShipId.getToDate()));
        }
        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
        return cb.and(predicates.toArray(new Predicate[]{}));
    }
}
