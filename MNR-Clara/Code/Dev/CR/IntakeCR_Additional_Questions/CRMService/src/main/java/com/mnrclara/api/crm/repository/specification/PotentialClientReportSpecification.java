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

import com.mnrclara.api.crm.model.potentialclient.PotentialClient;
import com.mnrclara.api.crm.model.potentialclient.SearchPotentialClientReport;

@SuppressWarnings("serial")
public class PotentialClientReportSpecification implements Specification<PotentialClient> {
	SearchPotentialClientReport searchPotentialClient;
	
	public PotentialClientReportSpecification (SearchPotentialClientReport inputSearchParams) {
		this.searchPotentialClient = inputSearchParams;
	}
	
	/**
	 * CLASS_ID
	 * REFERRAL_ID
	 * POT_CLIENT_ID
	 * INQ_NO
	 * STATUS_ID
	 * REF_FIELD_3
	 * REF_FIELD4
	 * REF_FIELD2
	 * CTD_ON
	 * @return
	 */
	@Override
	public Predicate toPredicate(Root<PotentialClient> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
	     List<Predicate> predicates = new ArrayList<Predicate>();
	     
	     if (searchPotentialClient.getClassId() != null) {
        	 predicates.add(cb.equal(root.get("classId"), searchPotentialClient.getClassId()));
         }
	     
	     if (searchPotentialClient.getReferralId() != null && !searchPotentialClient.getReferralId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("referralId");
        	 predicates.add(group.in(searchPotentialClient.getReferralId()));
         }
	     
	     if (searchPotentialClient.getPotentialClientId() != null && !searchPotentialClient.getPotentialClientId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("potentialClientId");
        	 predicates.add(group.in(searchPotentialClient.getPotentialClientId()));
         }
	     
	     if (searchPotentialClient.getInquiryNumber() != null && !searchPotentialClient.getInquiryNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("inquiryNumber");
        	 predicates.add(group.in(searchPotentialClient.getInquiryNumber()));
         }
	     
	     if (searchPotentialClient.getStatusId() != null && searchPotentialClient.getStatusId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchPotentialClient.getStatusId()));
         }
	     
	     if (searchPotentialClient.getInquiryNumber() != null && !searchPotentialClient.getInquiryNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("inquiryNumber");
        	 predicates.add(group.in(searchPotentialClient.getInquiryNumber()));
         }
	     
	     if (searchPotentialClient.getOnBoardingStatusRefField3() != null && 
	    		 !searchPotentialClient.getOnBoardingStatusRefField3().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("referenceField3");
        	 predicates.add(group.in(searchPotentialClient.getOnBoardingStatusRefField3()));
         }
	     
	     if (searchPotentialClient.getConsultingAttorneyRefField4() != null && 
	    		 !searchPotentialClient.getConsultingAttorneyRefField4().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("referenceField4");
        	 predicates.add(group.in(searchPotentialClient.getConsultingAttorneyRefField4()));
         }
	     
	     if (searchPotentialClient.getRetainedByRefField2() != null && 
	    		 !searchPotentialClient.getRetainedByRefField2().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("referenceField2");
        	 predicates.add(group.in(searchPotentialClient.getRetainedByRefField2()));
         }
	     
	     if (searchPotentialClient.getFromCreatedOn() != null && searchPotentialClient.getToCreatedOn() != null) {
             predicates.add(cb.between(root.get("createdOn"), 
            		 searchPotentialClient.getFromCreatedOn(), searchPotentialClient.getToCreatedOn()));
         }

		predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
	     
	     return cb.and(predicates.toArray(new Predicate[] {}));
	}
}
