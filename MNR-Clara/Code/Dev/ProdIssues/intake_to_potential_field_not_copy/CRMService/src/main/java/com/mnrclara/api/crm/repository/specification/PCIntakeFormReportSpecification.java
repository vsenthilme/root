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
import com.mnrclara.api.crm.model.pcitform.SearchPCIntakeFormReport;

@SuppressWarnings("serial")
public class PCIntakeFormReportSpecification implements Specification<PCIntakeForm> {
	
	SearchPCIntakeFormReport searchPCIntakeForm;
	
	public PCIntakeFormReportSpecification (SearchPCIntakeFormReport inputSearchParams) {
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
         
    	 if (searchPCIntakeForm.getClassId() != null) {
             final Path<Group> group = root.<Group> get("classId");
        	 predicates.add(group.in(searchPCIntakeForm.getClassId()));
         }
         
         if (searchPCIntakeForm.getInquiryAssignedToRefField4() != null && 
        		 !searchPCIntakeForm.getInquiryAssignedToRefField4().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("referenceField4");
        	 predicates.add(group.in(searchPCIntakeForm.getInquiryAssignedToRefField4()));
         }
         
         if (searchPCIntakeForm.getConsultingAttorneyRefField2() != null && 
        		 searchPCIntakeForm.getConsultingAttorneyRefField2().size() > 0) {
        	 final Path<Group> group = root.<Group> get("referenceField2");
        	 predicates.add(group.in(searchPCIntakeForm.getConsultingAttorneyRefField2()));
         }
         
 		 if (searchPCIntakeForm.getStatusId() != null && searchPCIntakeForm.getStatusId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchPCIntakeForm.getStatusId()));
         }
         
	     if (searchPCIntakeForm.getFromCreatedOn() != null && searchPCIntakeForm.getToCreatedOn() != null) {
             predicates.add(cb.between(root.get("createdOn"), 
            		 searchPCIntakeForm.getFromCreatedOn(), searchPCIntakeForm.getToCreatedOn()));
         }

		predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
 		 
         return cb.and(predicates.toArray(new Predicate[] {}));
    }
}
