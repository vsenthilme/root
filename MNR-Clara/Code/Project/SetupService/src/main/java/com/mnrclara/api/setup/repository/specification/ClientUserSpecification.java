package com.mnrclara.api.setup.repository.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.context.annotation.DeferredImportSelector.Group;
import org.springframework.data.jpa.domain.Specification;

import com.mnrclara.api.setup.model.clientuser.ClientUser;
import com.mnrclara.api.setup.model.clientuser.SearchClientUser;

@SuppressWarnings("serial")
public class ClientUserSpecification implements Specification<ClientUser> {
	
	SearchClientUser searchClientUser;
	
	public ClientUserSpecification(SearchClientUser inputSearchParams) {
		this.searchClientUser = inputSearchParams;
	}
	 
	@Override
    public Predicate toPredicate(Root<ClientUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

         List<Predicate> predicates = new ArrayList<Predicate>();
		
		 if (searchClientUser.getClassId() != null && !searchClientUser.getClassId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("classId");
        	 predicates.add(group.in(searchClientUser.getClassId()));
         }
		 
		 if (searchClientUser.getClientUserId() != null && !searchClientUser.getClientUserId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("clientUserId");
        	 predicates.add(group.in(searchClientUser.getClientUserId()));
         }
		 
         if (searchClientUser.getClientId() != null && !searchClientUser.getClientId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("clientId");
        	 predicates.add(group.in(searchClientUser.getClientId()));
         }
		 
		 if (searchClientUser.getContactNumber() != null && !searchClientUser.getContactNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("contactNumber");
        	 predicates.add(group.in(searchClientUser.getContactNumber()));
         }
		 
		 if (searchClientUser.getFullName() != null && !searchClientUser.getFullName().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("fullName");
        	 predicates.add(group.in(searchClientUser.getFullName()));
         }
		 
		 if (searchClientUser.getStatusId() != null && !searchClientUser.getStatusId().isEmpty()) {	
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchClientUser.getStatusId()));
         }	
		 
		 if (searchClientUser.getEmailId() != null && !searchClientUser.getEmailId().isEmpty()) {	
        	 final Path<Group> group = root.<Group> get("emailId");
        	 predicates.add(group.in(searchClientUser.getEmailId()));
         }	
			
		 if (searchClientUser.getStartCreatedOn() != null && searchClientUser.getEndCreatedOn() != null) {
        	 predicates.add(cb.between(root.get("createdOn"), searchClientUser.getStartCreatedOn(), searchClientUser.getEndCreatedOn()));
         }
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
