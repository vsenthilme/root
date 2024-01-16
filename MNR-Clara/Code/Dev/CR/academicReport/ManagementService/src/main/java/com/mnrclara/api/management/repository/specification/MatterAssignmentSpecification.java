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

import com.mnrclara.api.management.model.matterassignment.MatterAssignment;
import com.mnrclara.api.management.model.matterassignment.SearchMatterAssignment;

@SuppressWarnings("serial")
public class MatterAssignmentSpecification implements Specification<MatterAssignment> {
	
	SearchMatterAssignment searchMatterAssignment;
	
	public MatterAssignmentSpecification(SearchMatterAssignment inputSearchParams) {
		this.searchMatterAssignment = inputSearchParams;
	}
	 
	@Override
     public Predicate toPredicate(Root<MatterAssignment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

         List<Predicate> predicates = new ArrayList<Predicate>();

         if (searchMatterAssignment.getMatterNumber() != null && !searchMatterAssignment.getMatterNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("matterNumber");
        	 predicates.add(group.in(searchMatterAssignment.getMatterNumber()));
         }
         
         if (searchMatterAssignment.getMatterDescription() != null && !searchMatterAssignment.getMatterDescription().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("matterDescription");
        	 predicates.add(group.in(searchMatterAssignment.getMatterDescription()));
         }
         
         if (searchMatterAssignment.getClientId() != null && !searchMatterAssignment.getClientId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("clientId");
        	 predicates.add(group.in(searchMatterAssignment.getClientId()));
         }
         
         if (searchMatterAssignment.getCaseCategoryId() != null && !searchMatterAssignment.getCaseCategoryId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("caseCategoryId");
        	 predicates.add(group.in(searchMatterAssignment.getCaseCategoryId()));
         }
         
         if (searchMatterAssignment.getCaseSubCategoryId() != null && !searchMatterAssignment.getCaseSubCategoryId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("caseSubCategoryId");
        	 predicates.add(group.in(searchMatterAssignment.getCaseSubCategoryId()));
         }
         
         if (searchMatterAssignment.getPartner() != null && !searchMatterAssignment.getPartner().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("partner");
        	 predicates.add(group.in(searchMatterAssignment.getPartner()));
         }
         
         if (searchMatterAssignment.getOriginatingTimeKeeper() != null && !searchMatterAssignment.getOriginatingTimeKeeper().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("originatingTimeKeeper");
        	 predicates.add(group.in(searchMatterAssignment.getOriginatingTimeKeeper()));
         }
         
         if (searchMatterAssignment.getResponsibleTimeKeeper() != null && !searchMatterAssignment.getResponsibleTimeKeeper().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("responsibleTimeKeeper");
        	 predicates.add(group.in(searchMatterAssignment.getResponsibleTimeKeeper()));
         }
         
         if (searchMatterAssignment.getAssignedTimeKeeper() != null && !searchMatterAssignment.getAssignedTimeKeeper().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("assignedTimeKeeper");
        	 predicates.add(group.in(searchMatterAssignment.getAssignedTimeKeeper()));
         }
         
         if (searchMatterAssignment.getLegalAssistant() != null && !searchMatterAssignment.getLegalAssistant().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("legalAssistant");
        	 predicates.add(group.in(searchMatterAssignment.getLegalAssistant()));
         }
         
         if (searchMatterAssignment.getStatusId() != null && searchMatterAssignment.getStatusId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchMatterAssignment.getStatusId()));
         }

		 predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
