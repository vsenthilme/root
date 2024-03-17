package com.mnrclara.api.management.repository.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.context.annotation.DeferredImportSelector.Group;
import org.springframework.data.jpa.domain.Specification;

import com.mnrclara.api.management.model.clientgeneral.ClientGeneral;
import com.mnrclara.api.management.model.clientgeneral.SearchClientGeneral;

@SuppressWarnings("serial")
public class ClientGeneralSpecification implements Specification<ClientGeneral> {
	
	SearchClientGeneral searchClientGeneral;
	
	public ClientGeneralSpecification (SearchClientGeneral inputSearchParams) {
		this.searchClientGeneral = inputSearchParams;
	}
	 
	@Override
	public Predicate toPredicate(Root<ClientGeneral> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
	     List<Predicate> predicates = new ArrayList<Predicate>();
	
	     if (searchClientGeneral.getClientId() != null && !searchClientGeneral.getClientId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("clientId");
        	 predicates.add(group.in(searchClientGeneral.getClientId()));
         }
	     
	     if (searchClientGeneral.getFirstNameLastName() != null && !searchClientGeneral.getFirstNameLastName().isEmpty()) {
	         predicates.add(cb.like(root.get("firstNameLastName"), "%" + searchClientGeneral.getFirstNameLastName() + "%"));
	     }
	     
	     if (searchClientGeneral.getEmailId() != null && !searchClientGeneral.getEmailId().isEmpty()) {
	         predicates.add(cb.like(root.get("emailId"), "%" + searchClientGeneral.getEmailId() + "%"));
	     }
	     
	     if (searchClientGeneral.getContactNumber() != null && !searchClientGeneral.getContactNumber().isEmpty()) {
	         predicates.add(cb.like(root.get("contactNumber"), "%" + searchClientGeneral.getContactNumber() + "%"));
	     }
	     
	     if (searchClientGeneral.getAddressLine1() != null && !searchClientGeneral.getAddressLine1().isEmpty()) {
	         predicates.add(cb.like(root.get("addressLine1"), "%" + searchClientGeneral.getAddressLine1() + "%"));
	     }
	     
	     if (searchClientGeneral.getIntakeFormNumber() != null && !searchClientGeneral.getIntakeFormNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("intakeFormNumber");
        	 predicates.add(group.in(searchClientGeneral.getIntakeFormNumber()));
         }
	     
	     if (searchClientGeneral.getStatusId() != null && searchClientGeneral.getStatusId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchClientGeneral.getStatusId()));
         }
	     
	     if (searchClientGeneral.getStartCreatedOn() != null && searchClientGeneral.getEndCreatedOn() != null) {
	         predicates.add(cb.between(root.get("createdOn"), searchClientGeneral.getStartCreatedOn(), searchClientGeneral.getEndCreatedOn()));
	     }

		if (searchClientGeneral.getClassId() != null && searchClientGeneral.getClassId().size() > 0) {
			final Path<Group> group = root.<Group> get("classId");
			predicates.add(group.in(searchClientGeneral.getClassId()));
		}
	     
	     predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
	     
	     return cb.and(predicates.toArray(new Predicate[] {}));
	 }
}
