package com.mnrclara.api.cg.setup.repository.specification;

import com.mnrclara.api.cg.setup.model.store.StoreId;
import com.mnrclara.api.cg.setup.model.store.FindStoreId;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class StoreSpecification implements Specification<StoreId> {

    FindStoreId findStoreId;

    public StoreSpecification(FindStoreId inputSearchParams) {
        this.findStoreId = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<StoreId> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (findStoreId.getStoreId() != null && !findStoreId.getStoreId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("storeId");
            predicates.add(group.in(findStoreId.getStoreId()));
        }
        if (findStoreId.getCompanyId() != null && !findStoreId.getCompanyId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("companyId");
            predicates.add(group.in(findStoreId.getCompanyId()));
        }
        if (findStoreId.getLanguageId() != null && !findStoreId.getLanguageId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("languageId");
            predicates.add(group.in(findStoreId.getLanguageId()));
        }
        if (findStoreId.getCity() != null && !findStoreId.getCity().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("city");
            predicates.add(group.in(findStoreId.getCity()));
        }
        if (findStoreId.getState() != null && !findStoreId.getState().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("state");
            predicates.add(group.in(findStoreId.getState()));
        }
        if (findStoreId.getSubGroupTypeId() != null && !findStoreId.getSubGroupTypeId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("subGroupTypeId");
            predicates.add(group.in(findStoreId.getSubGroupTypeId()));
        }
        if (findStoreId.getGroupTypeId() != null && !findStoreId.getGroupTypeId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("groupTypeId");
            predicates.add(group.in(findStoreId.getGroupTypeId()));
        }
        if (findStoreId.getGroupTypeName() != null && !findStoreId.getGroupTypeName().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("groupTypeName");
            predicates.add(group.in(findStoreId.getGroupTypeName()));
        }
        if (findStoreId.getSubGroupTypeName() != null && !findStoreId.getSubGroupTypeName().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("subGroupTypeName");
            predicates.add(group.in(findStoreId.getSubGroupTypeName()));
        }
        if (findStoreId.getCountry() != null && !findStoreId.getCountry().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("country");
            predicates.add(group.in(findStoreId.getCountry()));
        }
        if (findStoreId.getStatus() != null && !findStoreId.getStatus().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("status");
            predicates.add(group.in(findStoreId.getStatus()));
        }
        if (findStoreId.getFromDate() != null && findStoreId.getToDate() != null) {
            predicates.add(cb.between(root.get("createdOn"), findStoreId.getFromDate(), findStoreId.getToDate()));
        }
        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
        return cb.and(predicates.toArray(new Predicate[]{}));
    }

}
