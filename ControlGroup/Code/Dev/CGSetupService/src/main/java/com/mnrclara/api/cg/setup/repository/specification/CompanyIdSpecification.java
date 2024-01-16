package com.mnrclara.api.cg.setup.repository.specification;


import com.mnrclara.api.cg.setup.model.companyid.CompanyId;
import com.mnrclara.api.cg.setup.model.companyid.FindCompanyId;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class CompanyIdSpecification implements Specification<CompanyId> {
    FindCompanyId findCompanyId;

    public CompanyIdSpecification(FindCompanyId inputSearchParams) {
        this.findCompanyId = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<CompanyId> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (findCompanyId.getCompanyId() != null && !findCompanyId.getCompanyId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("companyId");
            predicates.add(group.in(findCompanyId.getCompanyId()));
        }
        if (findCompanyId.getLanguageId() != null && !findCompanyId.getLanguageId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("languageId");
            predicates.add(group.in(findCompanyId.getLanguageId()));
        }
        if (findCompanyId.getFromDate() != null && findCompanyId.getToDate() != null) {
            predicates.add(cb.between(root.get("createdOn"), findCompanyId.getFromDate(), findCompanyId.getToDate()));
        }

        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
        return cb.and(predicates.toArray(new Predicate[]{}));
    }

}
