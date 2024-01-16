package com.mnrclara.api.cg.transaction.repository.specification;

import com.mnrclara.api.cg.transaction.model.storepartnerlisting.FindStorePartnerListing;
import com.mnrclara.api.cg.transaction.model.storepartnerlisting.StorePartnerListing;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class StorePartnerListingSpecification implements Specification<StorePartnerListing> {

    FindStorePartnerListing findStorePartnerListing;

    public StorePartnerListingSpecification(FindStorePartnerListing inputSearchParams) {
        this.findStorePartnerListing = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<StorePartnerListing> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (findStorePartnerListing.getStoreId() != null && !findStorePartnerListing.getStoreId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("storeId");
            predicates.add(group.in(findStorePartnerListing.getStoreId()));
        }

        if (findStorePartnerListing.getGroupId() != null && !findStorePartnerListing.getGroupId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("groupId");
            predicates.add(group.in(findStorePartnerListing.getGroupId()));
        }

        if (findStorePartnerListing.getSubGroupId() != null && !findStorePartnerListing.getSubGroupId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("subGroupId");
            predicates.add(group.in(findStorePartnerListing.getSubGroupId()));
        }

        if (findStorePartnerListing.getGroupTypeId() != null && !findStorePartnerListing.getGroupTypeId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("groupTypeId");
            predicates.add(group.in(findStorePartnerListing.getGroupTypeId()));
        }

        if (findStorePartnerListing.getLanguageId() != null && !findStorePartnerListing.getLanguageId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("languageId");
            predicates.add(group.in(findStorePartnerListing.getLanguageId()));
        }

        if (findStorePartnerListing.getCompanyId() != null && !findStorePartnerListing.getCompanyId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("companyId");
            predicates.add(group.in(findStorePartnerListing.getCompanyId()));
        }

        if (findStorePartnerListing.getVersionNumber() != null && !findStorePartnerListing.getVersionNumber().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("versionNumber");
            predicates.add(group.in(findStorePartnerListing.getVersionNumber()));
        }

        if (findStorePartnerListing.getStatusId() != null && !findStorePartnerListing.getStatusId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("statusId");
            predicates.add(group.in(findStorePartnerListing.getStatusId()));
        }

        if (findStorePartnerListing.getFromDate() != null && findStorePartnerListing.getToDate() != null) {
            predicates.add(cb.between(root.get("createdOn"), findStorePartnerListing.getFromDate(), findStorePartnerListing.getToDate()));
        }

        predicates.add(cb.equal(root.get("statusId2"), 0L));
        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
        return cb.and(predicates.toArray(new Predicate[]{}));
    }
}
