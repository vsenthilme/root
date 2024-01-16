package com.mnrclara.api.cg.transaction.repository.specification;


import com.mnrclara.api.cg.transaction.model.bscontrollinginterest.BSControllingInterest;
import com.mnrclara.api.cg.transaction.model.bscontrollinginterest.FindBSControllingInterest;
import com.mnrclara.api.cg.transaction.model.bseffectivecontrol.BSEffectiveControl;
import com.mnrclara.api.cg.transaction.model.bseffectivecontrol.FindBSEffectiveControl;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public class BSControllingInterestSpecification implements Specification<BSControllingInterest> {

    FindBSControllingInterest findBSControllingInterest;

    public BSControllingInterestSpecification(FindBSControllingInterest inputSearchParams) {
        this.findBSControllingInterest = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<BSControllingInterest> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (findBSControllingInterest.getValidationId() != null && !findBSControllingInterest.getValidationId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("validationId");
            predicates.add(group.in(findBSControllingInterest.getValidationId()));
        }

        if (findBSControllingInterest.getGroupId() != null && !findBSControllingInterest.getGroupId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("groupId");
            predicates.add(group.in(findBSControllingInterest.getGroupId()));
        }

        if (findBSControllingInterest.getSubGroupId() != null && !findBSControllingInterest.getSubGroupId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("subGroupId");
            predicates.add(group.in(findBSControllingInterest.getSubGroupId()));
        }

        if (findBSControllingInterest.getLanguageId() != null && !findBSControllingInterest.getLanguageId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("languageId");
            predicates.add(group.in(findBSControllingInterest.getLanguageId()));
        }
        if (findBSControllingInterest.getCompanyId() != null && !findBSControllingInterest.getCompanyId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("companyId");
            predicates.add(group.in(findBSControllingInterest.getCompanyId()));
        }
        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
        return cb.and(predicates.toArray(new Predicate[]{}));
    }
}
