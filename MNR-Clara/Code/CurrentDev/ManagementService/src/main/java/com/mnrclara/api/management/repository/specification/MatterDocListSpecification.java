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

import com.mnrclara.api.management.model.matterdoclist.MatterDocList;
import com.mnrclara.api.management.model.matterdoclist.SearchMatterDocList;

@SuppressWarnings("serial")
public class MatterDocListSpecification implements Specification<MatterDocList> {
	
	SearchMatterDocList searchMatterDocList;
	
	public MatterDocListSpecification(SearchMatterDocList inputSearchParams) {
		this.searchMatterDocList = inputSearchParams;
	}
	 
	@Override
    public Predicate toPredicate(Root<MatterDocList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

         List<Predicate> predicates = new ArrayList<Predicate>();
		
		 if (searchMatterDocList.getClassId() != null && !searchMatterDocList.getClassId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("classId");
        	 predicates.add(group.in(searchMatterDocList.getClassId()));
         }
		 
		 if (searchMatterDocList.getCheckListNo() != null && !searchMatterDocList.getCheckListNo().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("checkListNo");
        	 predicates.add(group.in(searchMatterDocList.getCheckListNo()));
         }
		 
         if (searchMatterDocList.getMatterNumber() != null && !searchMatterDocList.getMatterNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("matterNumber");
        	 predicates.add(group.in(searchMatterDocList.getMatterNumber()));
         }
		 
		 if (searchMatterDocList.getClientId() != null && !searchMatterDocList.getClientId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("clientId");
        	 predicates.add(group.in(searchMatterDocList.getClientId()));
         }
		 
		 if (searchMatterDocList.getStatusId() != null && !searchMatterDocList.getStatusId().isEmpty()) {	
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchMatterDocList.getStatusId()));
         }	
		 
		 if (searchMatterDocList.getCreatedBy() != null && !searchMatterDocList.getCreatedBy().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("createdBy");
        	 predicates.add(group.in(searchMatterDocList.getCreatedBy()));
         }   		 
	
		 if (searchMatterDocList.getStartCreatedOn() != null && searchMatterDocList.getEndCreatedOn() != null) {
        	 predicates.add(cb.between(root.get("createdOn"), searchMatterDocList.getStartCreatedOn(), searchMatterDocList.getEndCreatedOn()));
         }
		
		 predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
