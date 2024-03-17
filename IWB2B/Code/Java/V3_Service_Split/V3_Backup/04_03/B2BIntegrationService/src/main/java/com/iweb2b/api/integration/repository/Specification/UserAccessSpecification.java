package com.iweb2b.api.integration.repository.Specification;

import com.iweb2b.api.integration.model.usermanagement.*;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class UserAccessSpecification implements Specification<UserAccess> {

    FindUserAccess findUserAccess;

    public UserAccessSpecification(FindUserAccess inputSearchParams) {
        this.findUserAccess = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<UserAccess> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (findUserAccess.getCompanyCode() != null && !findUserAccess.getCompanyCode().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("companyCode");
            predicates.add(group.in(findUserAccess.getCompanyCode()));
        }
        if (findUserAccess.getUserTypeId() != null && !findUserAccess.getUserTypeId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("userTypeId");
            predicates.add(group.in(findUserAccess.getUserTypeId()));
        }
        if (findUserAccess.getLanguageId() != null && !findUserAccess.getLanguageId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("languageId");
            predicates.add(group.in(findUserAccess.getLanguageId()));
        }
        if (findUserAccess.getUserId() != null && !findUserAccess.getUserId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("userId");
            predicates.add(group.in(findUserAccess.getUserId()));
        }
        if (findUserAccess.getUserRoleId() != null && !findUserAccess.getUserRoleId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("userRoleId");
            predicates.add(group.in(findUserAccess.getUserRoleId()));
        }
        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
        return cb.and(predicates.toArray(new Predicate[] {}));
    }
}
