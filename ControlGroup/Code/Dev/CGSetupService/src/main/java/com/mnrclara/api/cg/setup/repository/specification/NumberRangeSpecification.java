package com.mnrclara.api.cg.setup.repository.specification;


import com.mnrclara.api.cg.setup.model.numberange.FindNumberRange;
import com.mnrclara.api.cg.setup.model.numberange.NumberRange;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class NumberRangeSpecification implements Specification<NumberRange> {

    FindNumberRange findNumberRange;

    public NumberRangeSpecification(FindNumberRange inputSearchParameter){
        this.findNumberRange = inputSearchParameter;
    }
    @Override
    public Predicate toPredicate(Root<NumberRange> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (findNumberRange.getNumberRangeCode() != null && !findNumberRange.getNumberRangeCode().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("numberRangeCode");
            predicates.add(group.in(findNumberRange.getNumberRangeCode()));
        }
        if (findNumberRange.getNumberRangeObject() != null && !findNumberRange.getNumberRangeObject().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("numberRangeObject");
            predicates.add(group.in(findNumberRange.getNumberRangeObject()));
        }
        if (findNumberRange.getLanguageId() != null && !findNumberRange.getLanguageId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("languageId");
            predicates.add(group.in(findNumberRange.getLanguageId()));
        }
        if (findNumberRange.getCompanyId() != null && !findNumberRange.getCompanyId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("companyId");
            predicates.add(group.in(findNumberRange.getCompanyId()));
        }
        if (findNumberRange.getNumberRangeStatus() != null && !findNumberRange.getNumberRangeStatus().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("numberRangeStatus");
            predicates.add(group.in(findNumberRange.getNumberRangeStatus()));
        }

        if (findNumberRange.getFromDate() != null && findNumberRange.getToDate() != null) {
            predicates.add(cb.between(root.get("createdOn"), findNumberRange.getFromDate(), findNumberRange.getToDate()));
        }

        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
        return cb.and(predicates.toArray(new Predicate[]{}));
    }
}
