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

import com.mnrclara.api.setup.model.docchecklist.DocCheckList;
import com.mnrclara.api.setup.model.docchecklist.SearchDocCheckList;

@SuppressWarnings("serial")
public class DocCheckListSpecification implements Specification<DocCheckList> {
	
	SearchDocCheckList searchDocCheckList;
	
	public DocCheckListSpecification(SearchDocCheckList inputSearchParams) {
		this.searchDocCheckList = inputSearchParams;
	}
	 
	@Override
    public Predicate toPredicate(Root<DocCheckList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

         List<Predicate> predicates = new ArrayList<Predicate>();
		
		 if (searchDocCheckList.getClassId() != null && !searchDocCheckList.getClassId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("classId");
        	 predicates.add(group.in(searchDocCheckList.getClassId()));
         }
		 
		 if (searchDocCheckList.getCheckListNo() != null && !searchDocCheckList.getCheckListNo().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("checkListNo");
        	 predicates.add(group.in(searchDocCheckList.getCheckListNo()));
         }
		 
         if (searchDocCheckList.getCaseCategoryId() != null && !searchDocCheckList.getCaseCategoryId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("caseCategoryId");
        	 predicates.add(group.in(searchDocCheckList.getCaseCategoryId()));
         }
		 
		 if (searchDocCheckList.getCaseSubCategoryId() != null && !searchDocCheckList.getCaseSubCategoryId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("caseSubCategoryId");
        	 predicates.add(group.in(searchDocCheckList.getCaseSubCategoryId()));
         }
		 
		 if (searchDocCheckList.getStatusId() != null && !searchDocCheckList.getStatusId().isEmpty()) {	
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchDocCheckList.getStatusId()));
         }	
		 
		 if (searchDocCheckList.getCreatedBy() != null && !searchDocCheckList.getCreatedBy().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("createdBy");
        	 predicates.add(group.in(searchDocCheckList.getCreatedBy()));
         }   		 
	
		  if (searchDocCheckList.getStartCreatedOn() != null && searchDocCheckList.getEndCreatedOn() != null) {
        	 predicates.add(cb.between(root.get("createdOn"), searchDocCheckList.getStartCreatedOn(), searchDocCheckList.getEndCreatedOn()));
         }
			
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
