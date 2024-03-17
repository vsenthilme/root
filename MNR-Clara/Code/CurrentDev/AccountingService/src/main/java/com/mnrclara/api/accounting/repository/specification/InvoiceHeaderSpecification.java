package com.mnrclara.api.accounting.repository.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.context.annotation.DeferredImportSelector.Group;
import org.springframework.data.jpa.domain.Specification;

import com.mnrclara.api.accounting.model.invoice.InvoiceHeader;
import com.mnrclara.api.accounting.model.invoice.SearchInvoiceHeader;

@SuppressWarnings("serial")
public class InvoiceHeaderSpecification implements Specification<InvoiceHeader> {

    SearchInvoiceHeader searchInvoiceHeader;

    /**
     * @param inputSearchParams
     */
    public InvoiceHeaderSpecification(SearchInvoiceHeader inputSearchParams) {
        this.searchInvoiceHeader = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<InvoiceHeader> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<Predicate>();

        if (searchInvoiceHeader.getClassId() != null && !searchInvoiceHeader.getClassId().isEmpty()) {
            final Path<Group> group = root.<Group>get("classId");
            predicates.add(group.in(searchInvoiceHeader.getClassId()));
        }

        if (searchInvoiceHeader.getClientId() != null && !searchInvoiceHeader.getClientId().isEmpty()) {
            final Path<Group> group = root.<Group>get("clientId");
            predicates.add(group.in(searchInvoiceHeader.getClientId()));
        }

        if (searchInvoiceHeader.getPartnerAssigned() != null && !searchInvoiceHeader.getPartnerAssigned().isEmpty()) {
            final Path<Group> group = root.<Group>get("partnerAssigned");
            predicates.add(group.in(searchInvoiceHeader.getPartnerAssigned()));
        }

        if (searchInvoiceHeader.getMatterNumber() != null && !searchInvoiceHeader.getMatterNumber().isEmpty()) {
            final Path<Group> group = root.<Group>get("matterNumber");
            predicates.add(group.in(searchInvoiceHeader.getMatterNumber()));
        }

        if (searchInvoiceHeader.getInvoiceNumber() != null && !searchInvoiceHeader.getInvoiceNumber().isEmpty()) {
            final Path<Group> group = root.<Group>get("invoiceNumber");
            predicates.add(group.in(searchInvoiceHeader.getInvoiceNumber()));
        }

        if (searchInvoiceHeader.getStatusId() != null && !searchInvoiceHeader.getStatusId().isEmpty()) {
            final Path<Group> group = root.<Group>get("statusId");
            predicates.add(group.in(searchInvoiceHeader.getStatusId()));
        }

        if (searchInvoiceHeader.getStartInvoiceDate() != null &&
                searchInvoiceHeader.getEndInvoiceDate() != null) {
            predicates.add(cb.between(root.get("postingDate"),
                    searchInvoiceHeader.getStartInvoiceDate(), searchInvoiceHeader.getEndInvoiceDate()));
        }

        if (searchInvoiceHeader.getCreatedBy() != null && !searchInvoiceHeader.getCreatedBy().isEmpty()) {
            final Path<Group> group = root.<Group>get("createdBy");
            predicates.add(group.in(searchInvoiceHeader.getCreatedBy()));
        }

        if (searchInvoiceHeader.getStatusId() != null && !searchInvoiceHeader.getStatusId().isEmpty()) {
            final Path<Group> group = root.<Group>get("statusId");
            predicates.add(group.in(searchInvoiceHeader.getStatusId()));
        }
        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
        return cb.and(predicates.toArray(new Predicate[]{}));
    }
}
