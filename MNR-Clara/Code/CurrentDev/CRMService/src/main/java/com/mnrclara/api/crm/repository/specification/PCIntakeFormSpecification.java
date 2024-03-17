package com.mnrclara.api.crm.repository.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.context.annotation.DeferredImportSelector.Group;
import org.springframework.data.jpa.domain.Specification;

import com.mnrclara.api.crm.model.pcitform.PCIntakeForm;
import com.mnrclara.api.crm.model.pcitform.SearchPCIntakeForm;

@SuppressWarnings("serial")
public class PCIntakeFormSpecification implements Specification<PCIntakeForm> {
	
	SearchPCIntakeForm searchPCIntakeForm;
	
	public PCIntakeFormSpecification (SearchPCIntakeForm inputSearchParams) {
		this.searchPCIntakeForm = inputSearchParams;
	}
	
	/**
	 * inquiryNumber
	 * intakeFormNumber
	 * intakeFormId
	 * emailId
	 * sentOn
	 * receivedOn
	 * approvedOn
	 * statusId
	 * @param searchPCIntakeForm
	 * @return
	 */
	@Override
    public Predicate toPredicate(Root<PCIntakeForm> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
         List<Predicate> predicates = new ArrayList<Predicate>();

         if (searchPCIntakeForm.getInquiryNumber() != null && !searchPCIntakeForm.getInquiryNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("inquiryNumber");
        	 predicates.add(group.in(searchPCIntakeForm.getInquiryNumber()));
         }
         
         if (searchPCIntakeForm.getIntakeFormNumber() != null && !searchPCIntakeForm.getIntakeFormNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("intakeFormNumber");
        	 predicates.add(group.in(searchPCIntakeForm.getIntakeFormNumber()));
         }
         
         if (searchPCIntakeForm.getIntakeFormId() != null && searchPCIntakeForm.getIntakeFormId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("intakeFormId");
        	 predicates.add(group.in(searchPCIntakeForm.getIntakeFormId()));
         }
         
         if (searchPCIntakeForm.getEmail() != null && !searchPCIntakeForm.getEmail().isEmpty()) {
             predicates.add(cb.like(root.get("email"), "%" + searchPCIntakeForm.getEmail() + "%"));
         }
         
         if (searchPCIntakeForm.getSSentOn() != null && searchPCIntakeForm.getESentOn() != null) {
             predicates.add(cb.between(root.get("sentOn"), searchPCIntakeForm.getSSentOn(), searchPCIntakeForm.getESentOn()));
         }
 		 
 		 if (searchPCIntakeForm.getSReceivedOn() != null && searchPCIntakeForm.getEReceivedOn() != null) {
             predicates.add(cb.between(root.get("receivedOn"), searchPCIntakeForm.getSReceivedOn(), searchPCIntakeForm.getEReceivedOn()));
         }
 		 
 		 if (searchPCIntakeForm.getSApprovedOn() != null && searchPCIntakeForm.getEApprovedOn() != null) {
             predicates.add(cb.between(root.get("approvedOn"), searchPCIntakeForm.getSApprovedOn(), searchPCIntakeForm.getEApprovedOn()));
         }
 		 
 		 if (searchPCIntakeForm.getStatusId() != null && searchPCIntakeForm.getStatusId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchPCIntakeForm.getStatusId()));
         }
         
         return cb.and(predicates.toArray(new Predicate[] {}));
    }
}
