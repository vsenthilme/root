package com.mnrclara.api.cg.setup.repository.specification;


import com.mnrclara.api.cg.setup.model.client.Client;
import com.mnrclara.api.cg.setup.model.client.FindClient;
import com.mnrclara.api.cg.setup.model.companyid.CompanyId;
import com.mnrclara.api.cg.setup.model.companyid.FindCompanyId;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ClientSpecification implements Specification<Client> {

    FindClient findClient;

    public ClientSpecification(FindClient inputSearchParams) {
        this.findClient = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (findClient.getCompanyId() != null && !findClient.getCompanyId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("companyId");
            predicates.add(group.in(findClient.getCompanyId()));
        }
        if (findClient.getLanguageId() != null && !findClient.getLanguageId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("languageId");
            predicates.add(group.in(findClient.getLanguageId()));
        }
        if (findClient.getClientId() != null && !findClient.getClientId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("clientId");
            predicates.add(group.in(findClient.getClientId()));
        }
        if (findClient.getStatusId() != null && !findClient.getStatusId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("statusId");
            predicates.add(group.in(findClient.getStatusId()));
        }
        if (findClient.getFromDate() != null && findClient.getToDate() != null) {
            predicates.add(cb.between(root.get("createdOn"), findClient.getFromDate(), findClient.getToDate()));
        }

        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
        return cb.and(predicates.toArray(new Predicate[]{}));
    }
}
