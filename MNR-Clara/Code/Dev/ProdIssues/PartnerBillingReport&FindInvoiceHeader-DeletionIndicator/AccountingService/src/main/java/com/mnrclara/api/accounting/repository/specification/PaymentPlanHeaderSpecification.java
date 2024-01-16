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

import com.mnrclara.api.accounting.model.paymentplan.PaymentPlanHeaderEntity;
import com.mnrclara.api.accounting.model.paymentplan.SearchPaymentPlanHeader;

@SuppressWarnings("serial")
public class PaymentPlanHeaderSpecification implements Specification<PaymentPlanHeaderEntity> {
	
	SearchPaymentPlanHeader searchPaymentPlanHeader;
	
	public PaymentPlanHeaderSpecification(SearchPaymentPlanHeader inputSearchParams) {
		this.searchPaymentPlanHeader = inputSearchParams;
	}
	 
	@Override
    public Predicate toPredicate(Root<PaymentPlanHeaderEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

         List<Predicate> predicates = new ArrayList<Predicate>();

         if (searchPaymentPlanHeader.getClientId() != null && !searchPaymentPlanHeader.getClientId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("clientId");
        	 predicates.add(group.in(searchPaymentPlanHeader.getClientId()));
         }
         
         if (searchPaymentPlanHeader.getMatterNumber() != null && !searchPaymentPlanHeader.getMatterNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("matterNumber");
        	 predicates.add(group.in(searchPaymentPlanHeader.getMatterNumber()));
         }
         
		 if (searchPaymentPlanHeader.getQuotationNo() != null && !searchPaymentPlanHeader.getQuotationNo().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("quotationNo");
        	 predicates.add(group.in(searchPaymentPlanHeader.getQuotationNo()));
         }
		 
		 if (searchPaymentPlanHeader.getPaymentPlanNumber() != null && !searchPaymentPlanHeader.getPaymentPlanNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("paymentPlanNumber");
        	 predicates.add(group.in(searchPaymentPlanHeader.getPaymentPlanNumber()));
         }
		 
		 if (searchPaymentPlanHeader.getStartPaymentPlanDate() != null && searchPaymentPlanHeader.getEndPaymentPlanDate() != null) {
        	 predicates.add(cb.between(root.get("paymentPlanDate"), searchPaymentPlanHeader.getStartPaymentPlanDate(), searchPaymentPlanHeader.getEndPaymentPlanDate()));
         }
		 
         if (searchPaymentPlanHeader.getStatusId() != null && !searchPaymentPlanHeader.getStatusId().isEmpty()) {	
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchPaymentPlanHeader.getStatusId()));
         }	
		 
         if (searchPaymentPlanHeader.getStartCreatedOn() != null && searchPaymentPlanHeader.getEndCreatedOn() != null) {
             predicates.add(cb.between(root.get("createdOn"), searchPaymentPlanHeader.getStartCreatedOn(), searchPaymentPlanHeader.getEndCreatedOn()));
         }
         
         if (searchPaymentPlanHeader.getCreatedBy() != null && !searchPaymentPlanHeader.getCreatedBy().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("createdBy");
        	 predicates.add(group.in(searchPaymentPlanHeader.getCreatedBy()));
         }
		                 
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
