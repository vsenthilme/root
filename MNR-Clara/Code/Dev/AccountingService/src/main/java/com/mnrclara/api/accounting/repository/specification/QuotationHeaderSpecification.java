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

import com.mnrclara.api.accounting.model.quotation.QuotationHeaderEntity;
import com.mnrclara.api.accounting.model.quotation.SearchQuotationHeader;

@SuppressWarnings("serial")
public class QuotationHeaderSpecification implements Specification<QuotationHeaderEntity> {
	
	SearchQuotationHeader searchQuotationHeader;
	
	public QuotationHeaderSpecification(SearchQuotationHeader inputSearchParams) {
		this.searchQuotationHeader = inputSearchParams;
	}
	 
	@Override
     public Predicate toPredicate(Root<QuotationHeaderEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

         List<Predicate> predicates = new ArrayList<Predicate>();

         if (searchQuotationHeader.getClientId() != null && !searchQuotationHeader.getClientId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("clientId");
        	 predicates.add(group.in(searchQuotationHeader.getClientId()));
         }
         
         if (searchQuotationHeader.getFirstNameLastName() != null && !searchQuotationHeader.getFirstNameLastName().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("firstNameLastName");
        	 predicates.add(group.in(searchQuotationHeader.getFirstNameLastName()));
         }
         
         if (searchQuotationHeader.getCaseCategoryId() != null && !searchQuotationHeader.getCaseCategoryId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("caseCategoryId");
        	 predicates.add(group.in(searchQuotationHeader.getCaseCategoryId()));
         }
         
         if (searchQuotationHeader.getMatterNumber() != null && !searchQuotationHeader.getMatterNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("matterNumber");
        	 predicates.add(group.in(searchQuotationHeader.getMatterNumber()));
         }
         
         if (searchQuotationHeader.getQuotationNo() != null && !searchQuotationHeader.getQuotationNo().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("quotationNo");
        	 predicates.add(group.in(searchQuotationHeader.getQuotationNo()));
         }
         
         if (searchQuotationHeader.getStartQuotationDate() != null && searchQuotationHeader.getEndQuotationDate() != null) {
             predicates.add(cb.between(root.get("quotationDate"), searchQuotationHeader.getStartQuotationDate(), searchQuotationHeader.getEndQuotationDate()));
         }
         
         if (searchQuotationHeader.getCreatedBy() != null && !searchQuotationHeader.getCreatedBy().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("createdBy");
        	 predicates.add(group.in(searchQuotationHeader.getCreatedBy()));
         }
		 
		 if (searchQuotationHeader.getStatusId() != null && !searchQuotationHeader.getStatusId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchQuotationHeader.getStatusId()));
         }
                 
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
