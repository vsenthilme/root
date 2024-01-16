package com.mnrclara.api.management.repository.specification;

import com.mnrclara.api.management.model.matterdoclist.*;
import org.springframework.context.annotation.DeferredImportSelector.Group;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class MatterDocListHeaderSpecification implements Specification<MatterDocListHeader> {

	FindMatterDocListHeader findMatterDocListHeader;

	public MatterDocListHeaderSpecification(FindMatterDocListHeader inputSearchParams) {
		this.findMatterDocListHeader = inputSearchParams;
	}
	 
	@Override
    public Predicate toPredicate(Root<MatterDocListHeader> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

         List<Predicate> predicates = new ArrayList<Predicate>();
		
		 if (findMatterDocListHeader.getClassId() != null && !findMatterDocListHeader.getClassId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("classId");
        	 predicates.add(group.in(findMatterDocListHeader.getClassId()));
         }
		 
		 if (findMatterDocListHeader.getCheckListNo() != null && !findMatterDocListHeader.getCheckListNo().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("checkListNo");
        	 predicates.add(group.in(findMatterDocListHeader.getCheckListNo()));
         }
		 
         if (findMatterDocListHeader.getMatterNumber() != null && !findMatterDocListHeader.getMatterNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("matterNumber");
        	 predicates.add(group.in(findMatterDocListHeader.getMatterNumber()));
         }
		 
		 if (findMatterDocListHeader.getClientId() != null && !findMatterDocListHeader.getClientId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("clientId");
        	 predicates.add(group.in(findMatterDocListHeader.getClientId()));
         }
		 
		 if (findMatterDocListHeader.getStatusId() != null && !findMatterDocListHeader.getStatusId().isEmpty()) {	
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(findMatterDocListHeader.getStatusId()));
         }
		if (findMatterDocListHeader.getMatterHeaderId() != null && !findMatterDocListHeader.getMatterHeaderId().isEmpty()) {
			final Path<Group> group = root.<Group> get("matterHeaderId");
			predicates.add(group.in(findMatterDocListHeader.getMatterHeaderId()));
		}
		if (findMatterDocListHeader.getCaseCategoryId() != null && !findMatterDocListHeader.getCaseCategoryId().isEmpty()) {
			final Path<Group> group = root.<Group> get("caseCategoryId");
			predicates.add(group.in(findMatterDocListHeader.getCaseCategoryId()));
		}
		if (findMatterDocListHeader.getCaseSubCategoryId() != null && !findMatterDocListHeader.getCaseSubCategoryId().isEmpty()) {
			final Path<Group> group = root.<Group> get("caseSubCategoryId");
			predicates.add(group.in(findMatterDocListHeader.getCaseSubCategoryId()));
		}
		 if (findMatterDocListHeader.getCreatedBy() != null && !findMatterDocListHeader.getCreatedBy().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("createdBy");
        	 predicates.add(group.in(findMatterDocListHeader.getCreatedBy()));
         }   		 
	
		 if (findMatterDocListHeader.getStartCreatedOn() != null && findMatterDocListHeader.getEndCreatedOn() != null) {
        	 predicates.add(cb.between(root.get("createdOn"), findMatterDocListHeader.getStartCreatedOn(), findMatterDocListHeader.getEndCreatedOn()));
         }
		
		 predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
