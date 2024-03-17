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

import com.mnrclara.api.accounting.model.prebill.PreBillDetails;
import com.mnrclara.api.accounting.model.prebill.SearchPreBillDetails;

@SuppressWarnings("serial")
public class PreBillDetailsSpecification implements Specification<PreBillDetails> {
	
	SearchPreBillDetails searchPreBillDetails;
	
	public PreBillDetailsSpecification(SearchPreBillDetails inputSearchParams) {
		this.searchPreBillDetails = inputSearchParams;
	}
	 
	@Override
    public Predicate toPredicate(Root<PreBillDetails> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

         List<Predicate> predicates = new ArrayList<Predicate>();

         if (searchPreBillDetails.getClientId() != null && !searchPreBillDetails.getClientId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("clientId");
        	 predicates.add(group.in(searchPreBillDetails.getClientId()));
         }
		 
		 if (searchPreBillDetails.getClassId() != null && !searchPreBillDetails.getClassId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("classId");
        	 predicates.add(group.in(searchPreBillDetails.getClassId()));
         }
         
		 if (searchPreBillDetails.getPartnerAssigned() != null && !searchPreBillDetails.getPartnerAssigned().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("partnerAssigned");
        	 predicates.add(group.in(searchPreBillDetails.getPartnerAssigned()));
         }
		 
         if (searchPreBillDetails.getMatterNumber() != null && !searchPreBillDetails.getMatterNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("matterNumber");
        	 predicates.add(group.in(searchPreBillDetails.getMatterNumber()));
         }
         
		 if (searchPreBillDetails.getPreBillBatchNumber() != null && !searchPreBillDetails.getPreBillBatchNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("preBillBatchNumber");
        	 predicates.add(group.in(searchPreBillDetails.getPreBillBatchNumber()));
         }
		 
		 if (searchPreBillDetails.getPreBillNumber() != null && !searchPreBillDetails.getPreBillNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("preBillNumber");
        	 predicates.add(group.in(searchPreBillDetails.getPreBillNumber()));
         }
		 
		 if (searchPreBillDetails.getStartPreBillDate() != null && searchPreBillDetails.getEndPreBillDate() != null) {
        	 predicates.add(cb.between(root.get("preBillDate"), searchPreBillDetails.getStartPreBillDate(), searchPreBillDetails.getEndPreBillDate()));
         }
		 
         if (searchPreBillDetails.getStatusId() != null && !searchPreBillDetails.getStatusId().isEmpty()) {	
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchPreBillDetails.getStatusId()));
         }	
		
         if (searchPreBillDetails.getCreatedBy() != null && !searchPreBillDetails.getCreatedBy().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("createdBy");
        	 predicates.add(group.in(searchPreBillDetails.getCreatedBy()));
         }
         
         // Deletion Indicator
         predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
		                 
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
