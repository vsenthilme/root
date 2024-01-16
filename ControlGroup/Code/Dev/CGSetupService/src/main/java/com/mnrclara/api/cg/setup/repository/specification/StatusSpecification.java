package com.mnrclara.api.cg.setup.repository.specification;

import com.mnrclara.api.cg.setup.model.numberange.NumberRange;
import com.mnrclara.api.cg.setup.model.status.FindStatus;
import com.mnrclara.api.cg.setup.model.status.StatusId;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class StatusSpecification implements Specification<StatusId> {

    FindStatus findStatus;

    public StatusSpecification(FindStatus inputSearchParams){
        this.findStatus = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<StatusId> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (findStatus.getStatusId() != null && !findStatus.getStatusId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("statusId");
            predicates.add(group.in(findStatus.getStatusId()));
        }
        if (findStatus.getLanguageId() != null && !findStatus.getLanguageId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("languageId");
            predicates.add(group.in(findStatus.getLanguageId()));
        }
        if (findStatus.getFromDate() != null && findStatus.getToDate() != null) {
            predicates.add(cb.between(root.get("createdOn"), findStatus.getFromDate(), findStatus.getToDate()));
        }

        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
        return cb.and(predicates.toArray(new Predicate[]{}));
    }
}
