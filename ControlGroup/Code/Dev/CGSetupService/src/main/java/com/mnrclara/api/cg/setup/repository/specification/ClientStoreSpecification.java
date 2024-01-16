package com.mnrclara.api.cg.setup.repository.specification;

import com.mnrclara.api.cg.setup.model.clientstore.ClientStore;
import com.mnrclara.api.cg.setup.model.clientstore.FindClientStore;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")

public class ClientStoreSpecification implements Specification<ClientStore> {

    FindClientStore findClientStore;

    public ClientStoreSpecification(FindClientStore inputSearchParams) {
        this.findClientStore = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<ClientStore> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (findClientStore.getClientId() != null && !findClientStore.getClientId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("clientId");
            predicates.add(group.in(findClientStore.getClientId()));
        }
        if (findClientStore.getStoreId() != null && !findClientStore.getStoreId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("storeId");
            predicates.add(group.in(findClientStore.getStoreId()));
        }
        if (findClientStore.getCompanyId() != null && !findClientStore.getCompanyId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("companyId");
            predicates.add(group.in(findClientStore.getCompanyId()));
        }
        if (findClientStore.getLanguageId() != null && !findClientStore.getLanguageId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("languageId");
            predicates.add(group.in(findClientStore.getLanguageId()));
        }
        if (findClientStore.getVersionNumber() != null && !findClientStore.getVersionNumber().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("versionNumber");
            predicates.add(group.in(findClientStore.getVersionNumber()));
        }
        if (findClientStore.getStatusId() != null && !findClientStore.getStatusId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("statusId");
            predicates.add(group.in(findClientStore.getStatusId()));
        }
        if (findClientStore.getFromDate() != null && findClientStore.getToDate() != null) {
            predicates.add(cb.between(root.get("createdOn"), findClientStore.getFromDate(), findClientStore.getToDate()));
        }

        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
        return cb.and(predicates.toArray(new Predicate[]{}));
    }
}