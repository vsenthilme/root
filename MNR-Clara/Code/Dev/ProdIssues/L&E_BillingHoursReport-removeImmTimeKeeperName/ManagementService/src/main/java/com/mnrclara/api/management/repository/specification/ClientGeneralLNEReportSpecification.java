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
import com.mnrclara.api.management.model.clientgeneral.SearchClientGeneralLNEReport;

@SuppressWarnings("serial")
public class ClientGeneralLNEReportSpecification implements Specification<ClientGeneral> {
	
	SearchClientGeneralLNEReport searchClientGeneral;
	
	public ClientGeneralLNEReportSpecification (SearchClientGeneralLNEReport inputSearchParams) {
		this.searchClientGeneral = inputSearchParams;
	}
	 
	@Override
	public Predicate toPredicate(Root<ClientGeneral> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
	     List<Predicate> predicates = new ArrayList<Predicate>();
	     
	     if (searchClientGeneral.getClassId() != null) {
        	 predicates.add(cb.equal(root.get("classId"), searchClientGeneral.getClassId()));
         }
	     
	     if (searchClientGeneral.getClientId() != null && !searchClientGeneral.getClientId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("clientId");
        	 predicates.add(group.in(searchClientGeneral.getClientId()));
         }
	     
	     if (searchClientGeneral.getReferralId() != null && !searchClientGeneral.getReferralId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("referralId");
        	 predicates.add(group.in(searchClientGeneral.getReferralId()));
         }
	     
	     if (searchClientGeneral.getFromCreatedOn() != null && searchClientGeneral.getToCreatedOn() != null) {
             predicates.add(cb.between(root.get("createdOn"), 
            		 searchClientGeneral.getFromCreatedOn(), searchClientGeneral.getToCreatedOn()));
         }
	     
	     if (searchClientGeneral.getStatusId() != null && searchClientGeneral.getStatusId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchClientGeneral.getStatusId()));
         }
	     
	     if (searchClientGeneral.getClientCategoryId() != null && searchClientGeneral.getClientCategoryId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("clientCategoryId");
        	 predicates.add(group.in(searchClientGeneral.getClientCategoryId()));
         }
	     
	     if (searchClientGeneral.getFromDateClosed() != null && searchClientGeneral.getToDateClosed() != null) {
	         predicates.add(cb.between(root.get("referenceField4"), searchClientGeneral.getFromDateClosed(),
	        		 searchClientGeneral.getToDateClosed()));
	     }

		predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
	     
	     return cb.and(predicates.toArray(new Predicate[] {}));
	 }
}
