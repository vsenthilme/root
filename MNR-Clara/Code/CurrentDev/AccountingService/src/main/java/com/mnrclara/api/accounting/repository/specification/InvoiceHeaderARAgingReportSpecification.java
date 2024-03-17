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
import com.mnrclara.api.accounting.model.invoice.SearchInvoiceHeaderARAgingReport;
import com.mnrclara.api.accounting.model.invoice.report.ARAgingReport;

@SuppressWarnings("serial")
public class InvoiceHeaderARAgingReportSpecification implements Specification<ARAgingReport> {
	
	SearchInvoiceHeaderARAgingReport searchInvoiceHeader;
	
	/**
	 * 
	 * @param inputSearchParams
	 */
	public InvoiceHeaderARAgingReportSpecification(SearchInvoiceHeaderARAgingReport inputSearchParams) {
		this.searchInvoiceHeader = inputSearchParams;
	}
	 
	@Override
    public Predicate toPredicate(Root<ARAgingReport> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
         List<Predicate> predicates = new ArrayList<Predicate>();

         if (searchInvoiceHeader.getClassId() != null) {
        	 predicates.add(cb.equal(root.get("classId"), searchInvoiceHeader.getClassId()));
         }
         
         if (searchInvoiceHeader.getMatterNumber() != null && !searchInvoiceHeader.getMatterNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("matterNumber");
        	 predicates.add(group.in(searchInvoiceHeader.getMatterNumber()));
         }
         
         if (searchInvoiceHeader.getClientId() != null && !searchInvoiceHeader.getClientId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("clientId");
        	 predicates.add(group.in(searchInvoiceHeader.getClientId()));
         }
		 
		 if (searchInvoiceHeader.getStatusId() != null&& !searchInvoiceHeader.getStatusId().isEmpty()) {
			 final Path<Group> group = root.<Group> get("statusId");
			 predicates.add(group.in(searchInvoiceHeader.getStatusId()));
         }
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
