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
import com.mnrclara.api.crm.model.potentialclient.SearchPotentialClient;

@SuppressWarnings("serial")
public class PotentialClientSpecification implements Specification<PotentialClient> {
	SearchPotentialClient searchPotentialClient;
	
	public PotentialClientSpecification (SearchPotentialClient inputSearchParams) {
		this.searchPotentialClient = inputSearchParams;
	}
	
	/**
	 * potentialClientId
	 * inquiryNumber
	 * intakeFormId
	 * firstNameLastName
	 * emailId
	 * contactNumber
	 * statusId
	 * createdBy
	 * createdOn
	 * REF_FIELD_1 -- Consultation date
	 * REF_FIELD_2 -- Assigned attorney
	 * @param searchPotentialClient
	 * @return
	 */
	@Override
	public Predicate toPredicate(Root<PotentialClient> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
	     List<Predicate> predicates = new ArrayList<Predicate>();
	     
	     if (searchPotentialClient.getPotentialClientId() != null && !searchPotentialClient.getPotentialClientId().isEmpty()) {
             final Path<Group> group = root.<Group> get("potentialClientId");
        	 predicates.add(group.in(searchPotentialClient.getPotentialClientId()));
         }
	     
	     if (searchPotentialClient.getInquiryNumber() != null && !searchPotentialClient.getInquiryNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("inquiryNumber");
        	 predicates.add(group.in(searchPotentialClient.getInquiryNumber()));
         }
	     
	     if (searchPotentialClient.getIntakeFormId() != null && searchPotentialClient.getIntakeFormId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("intakeFormId");
        	 predicates.add(group.in(searchPotentialClient.getIntakeFormId()));
         }
	     
	     if (searchPotentialClient.getFirstNameLastName() != null && !searchPotentialClient.getFirstNameLastName().isEmpty()) {
             predicates.add(cb.like(root.get("firstNameLastName"), "%" + searchPotentialClient.getFirstNameLastName() + "%"));
         }
	     
	     if (searchPotentialClient.getEmailId() != null && !searchPotentialClient.getEmailId().isEmpty()) {
             predicates.add(cb.like(root.get("emailId"), "%" + searchPotentialClient.getEmailId() + "%"));
         }
	     
	     if (searchPotentialClient.getContactNumber() != null && !searchPotentialClient.getContactNumber().isEmpty()) {
             predicates.add(cb.like(root.get("contactNumber"), "%" + searchPotentialClient.getContactNumber() + "%"));
         }
	     
	     if (searchPotentialClient.getSCreatedOn() != null && searchPotentialClient.getECreatedOn() != null) {
             predicates.add(cb.between(root.get("createdOn"), searchPotentialClient.getSCreatedOn(), searchPotentialClient.getECreatedOn()));
         }
	     
	     if (searchPotentialClient.getCreatedBy() != null && !searchPotentialClient.getCreatedBy().isEmpty()) {
             final Path<Group> group = root.<Group> get("createdBy");
        	 predicates.add(group.in(searchPotentialClient.getCreatedBy()));
         }
	     
	     if (searchPotentialClient.getSConsultationDate() != null && searchPotentialClient.getEConsultationDate() != null) {
             predicates.add(cb.between(root.get("referenceField1"), searchPotentialClient.getSConsultationDate(), searchPotentialClient.getEConsultationDate()));
         }
	     
	     if (searchPotentialClient.getAssignedAttorney() != null && !searchPotentialClient.getAssignedAttorney().isEmpty()) {
             predicates.add(cb.like(root.get("referenceField2"), "%" + searchPotentialClient.getAssignedAttorney() + "%"));
         }
	     
	     if (searchPotentialClient.getStatusId() != null && searchPotentialClient.getStatusId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchPotentialClient.getStatusId()));
         }
	     
	     return cb.and(predicates.toArray(new Predicate[] {}));
	}
}
