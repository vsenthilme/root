package com.mnrclara.api.cg.transaction.repository.specification;

import com.mnrclara.api.cg.transaction.model.ownershiprequest.FindOwnerShipRequest;
import com.mnrclara.api.cg.transaction.model.ownershiprequest.OwnerShipRequest;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public class OwnerShipRequestSpecification implements Specification<OwnerShipRequest> {

    FindOwnerShipRequest findOwnerShipRequest;

    public OwnerShipRequestSpecification(FindOwnerShipRequest inputSearchParams) {
        this.findOwnerShipRequest = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<OwnerShipRequest> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (findOwnerShipRequest.getRequestId() != null && !findOwnerShipRequest.getRequestId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("requestId");
            predicates.add(group.in(findOwnerShipRequest.getRequestId()));
        }

        if (findOwnerShipRequest.getGroupId() != null && !findOwnerShipRequest.getGroupId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("groupId");
            predicates.add(group.in(findOwnerShipRequest.getGroupId()));
        }

        if (findOwnerShipRequest.getSubGroupId() != null && !findOwnerShipRequest.getSubGroupId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("subGroupId");
            predicates.add(group.in(findOwnerShipRequest.getSubGroupId()));
        }

        if (findOwnerShipRequest.getGroupTypeId() != null && !findOwnerShipRequest.getGroupTypeId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("groupTypeId");
            predicates.add(group.in(findOwnerShipRequest.getGroupTypeId()));
        }

        if (findOwnerShipRequest.getStoreId() != null && !findOwnerShipRequest.getStoreId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("storeId");
            predicates.add(group.in(findOwnerShipRequest.getStoreId()));
        }
        if (findOwnerShipRequest.getLanguageId() != null && !findOwnerShipRequest.getLanguageId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("languageId");
            predicates.add(group.in(findOwnerShipRequest.getLanguageId()));
        }
        if (findOwnerShipRequest.getCompanyId() != null && !findOwnerShipRequest.getCompanyId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("companyId");
            predicates.add(group.in(findOwnerShipRequest.getCompanyId()));
        }
        if (findOwnerShipRequest.getStatusId() != null && !findOwnerShipRequest.getStatusId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("statusId");
            predicates.add(group.in(findOwnerShipRequest.getStatusId()));
        }
        if (findOwnerShipRequest.getCreatedBy() != null && !findOwnerShipRequest.getCreatedBy().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("createdBy");
            predicates.add(group.in(findOwnerShipRequest.getCreatedBy()));
        }

        if (findOwnerShipRequest.getFromDate() != null && findOwnerShipRequest.getToDate() != null) {
            predicates.add(cb.between(root.get("createdOn"), findOwnerShipRequest.getFromDate(), findOwnerShipRequest.getToDate()));
        }

        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
        return cb.and(predicates.toArray(new Predicate[]{}));
    }

}
