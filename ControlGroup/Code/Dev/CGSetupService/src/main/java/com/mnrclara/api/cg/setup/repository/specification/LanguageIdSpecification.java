package com.mnrclara.api.cg.setup.repository.specification;

import com.mnrclara.api.cg.setup.model.languageid.FindLanguageId;
import com.mnrclara.api.cg.setup.model.languageid.LanguageId;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class LanguageIdSpecification implements Specification<LanguageId> {
    FindLanguageId findLanguageId;

    public LanguageIdSpecification(FindLanguageId inputSearchParams) {
        this.findLanguageId = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<LanguageId> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (findLanguageId.getLanguageId() != null && !findLanguageId.getLanguageId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("languageId");
            predicates.add(group.in(findLanguageId.getLanguageId()));
        }
        if (findLanguageId.getFromDate() != null && findLanguageId.getToDate() != null) {
            predicates.add(cb.between(root.get("createdOn"), findLanguageId.getFromDate(), findLanguageId.getToDate()));
        }

        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
        return cb.and(predicates.toArray(new Predicate[]{}));
    }
}
