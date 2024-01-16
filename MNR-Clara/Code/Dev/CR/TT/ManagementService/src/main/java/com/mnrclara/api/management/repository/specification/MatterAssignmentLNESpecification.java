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
import com.mnrclara.api.management.model.matterassignment.SearchMatterAssignmentLNEReport;

@SuppressWarnings("serial")
public class MatterAssignmentLNESpecification implements Specification<MatterAssignment> {
	
	SearchMatterAssignmentLNEReport searchMatterAssignment;
	
	public MatterAssignmentLNESpecification(SearchMatterAssignmentLNEReport inputSearchParams) {
		this.searchMatterAssignment = inputSearchParams;
	}
	 
	@Override
     public Predicate toPredicate(Root<MatterAssignment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

         List<Predicate> predicates = new ArrayList<Predicate>();
         
         if (searchMatterAssignment.getMatterNumber() != null && !searchMatterAssignment.getMatterNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("matterNumber");
        	 predicates.add(group.in(searchMatterAssignment.getMatterNumber()));
         }
         
         if (searchMatterAssignment.getOriginatingTimeKeeper() != null && !searchMatterAssignment.getOriginatingTimeKeeper().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("originatingTimeKeeper");
        	 predicates.add(group.in(searchMatterAssignment.getOriginatingTimeKeeper()));
         }
         
         if (searchMatterAssignment.getPartner() != null && !searchMatterAssignment.getPartner().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("partner");
        	 predicates.add(group.in(searchMatterAssignment.getPartner()));
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
         
         // Law clerks - REF_FIELD_1
         if (searchMatterAssignment.getLawClerks() != null && !searchMatterAssignment.getLawClerks().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("referenceField1");
        	 predicates.add(group.in(searchMatterAssignment.getLawClerks()));
         }
		 predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
