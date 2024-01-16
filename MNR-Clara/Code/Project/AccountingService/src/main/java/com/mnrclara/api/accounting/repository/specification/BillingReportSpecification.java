package com.mnrclara.api.accounting.repository.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.context.annotation.DeferredImportSelector.Group;
import org.springframework.data.jpa.domain.Specification;

import com.mnrclara.api.accounting.model.invoice.InvoiceHeader;
import com.mnrclara.api.accounting.model.invoice.report.BillingReport;
import com.mnrclara.api.accounting.model.invoice.report.SearchInvoiceHeaderBillingReport;

@SuppressWarnings("serial")
public class BillingReportSpecification implements Specification<BillingReport> {
	
	SearchInvoiceHeaderBillingReport searchInvoiceHeader;
	
	/**
	 * 
	 * @param inputSearchParams
	 */
	public BillingReportSpecification(SearchInvoiceHeaderBillingReport inputSearchParams) {
		this.searchInvoiceHeader = inputSearchParams;
	}
	 
	@Override
    public Predicate toPredicate(Root<BillingReport> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
         List<Predicate> predicates = new ArrayList<Predicate>();

         if (searchInvoiceHeader.getClassId() != null && !searchInvoiceHeader.getClassId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("classId");
        	 predicates.add(group.in(searchInvoiceHeader.getClassId()));
         }
         
         if (searchInvoiceHeader.getFromPostingDate() != null && searchInvoiceHeader.getToPostingDate() != null) {
             predicates.add(cb.between(root.get("postingDate"), 
            		 searchInvoiceHeader.getFromPostingDate(), searchInvoiceHeader.getToPostingDate()));
         }
         
         if (searchInvoiceHeader.getMatterNumber() != null && !searchInvoiceHeader.getMatterNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("matterNumber");
        	 predicates.add(group.in(searchInvoiceHeader.getMatterNumber()));
         }
         
         if (searchInvoiceHeader.getClientId() != null && !searchInvoiceHeader.getClientId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("clientId");
        	 predicates.add(group.in(searchInvoiceHeader.getClientId()));
         }
		 
		 if (searchInvoiceHeader.getStatusId() != null && !searchInvoiceHeader.getStatusId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("statusId");
			 predicates.add(group.in(searchInvoiceHeader.getStatusId()));
         }
		 
		 if (searchInvoiceHeader.getCaseCategoryId() != null && !searchInvoiceHeader.getCaseCategoryId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("categoryId");
			 predicates.add(group.in(searchInvoiceHeader.getCaseCategoryId()));
         }
		 
		 if (searchInvoiceHeader.getCaseSubCategoryId() != null && !searchInvoiceHeader.getCaseSubCategoryId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("subCategoryId");
			 predicates.add(group.in(searchInvoiceHeader.getCaseSubCategoryId()));
         }
		if (searchInvoiceHeader.getTimeKeepers() != null && !searchInvoiceHeader.getTimeKeepers().isEmpty()) {
			final Path<Group> group = root.<Group> get("partnerAssigned");
			predicates.add(group.in(searchInvoiceHeader.getTimeKeepers()));
		}

         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
