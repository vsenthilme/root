package com.mnrclara.api.cg.transaction.repository.specification;


import com.mnrclara.api.cg.transaction.model.Requestid.FindRequestId;
import com.mnrclara.api.cg.transaction.model.Requestid.RequestId;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class RequestIdSpecification implements Specification<RequestId> {

    FindRequestId findRequestId;

    public RequestIdSpecification(FindRequestId inputSearchParams) {
        this.findRequestId = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<RequestId> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (findRequestId.getRequestId() != null && !findRequestId.getRequestId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("requestId");
            predicates.add(group.in(findRequestId.getRequestId()));
        }

        if (findRequestId.getFileName() != null && !findRequestId.getFileName().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("fileName");
            predicates.add(group.in(findRequestId.getFileName()));
        }

        if (findRequestId.getId() != null && !findRequestId.getId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("id");
            predicates.add(group.in(findRequestId.getId()));
        }
        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
        return cb.and(predicates.toArray(new Predicate[]{}));
    }
}
