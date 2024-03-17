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

import com.mnrclara.api.management.model.mattergeneral.MatterGenAcc;
import com.mnrclara.api.management.model.mattergeneral.SearchMatterLNEReport;

@SuppressWarnings("serial")
public class MatterGeneralLNESpecification implements Specification<MatterGenAcc> {
	
	SearchMatterLNEReport searchMatterGeneral;
	
	public MatterGeneralLNESpecification(SearchMatterLNEReport inputSearchParams) {
		this.searchMatterGeneral = inputSearchParams;
	}
	 
	@Override
     public Predicate toPredicate(Root<MatterGenAcc> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

         List<Predicate> predicates = new ArrayList<Predicate>();
         
         if (searchMatterGeneral.getClassId() != null) {
        	 predicates.add(cb.equal(root.get("classId"), searchMatterGeneral.getClassId()));
         }
         
         if (searchMatterGeneral.getCaseCategoryId() != null && searchMatterGeneral.getCaseCategoryId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("caseCategoryId");
        	 predicates.add(group.in(searchMatterGeneral.getCaseCategoryId()));
         }
         
         if (searchMatterGeneral.getCaseSubCategoryId() != null && searchMatterGeneral.getCaseSubCategoryId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("caseSubCategoryId");
        	 predicates.add(group.in(searchMatterGeneral.getCaseSubCategoryId()));
         }

         if (searchMatterGeneral.getBillingModeId() != null && !searchMatterGeneral.getBillingModeId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("billingModeId");
        	 predicates.add(group.in(searchMatterGeneral.getBillingModeId()));
         }
         
         if (searchMatterGeneral.getFromCaseOpenDate() != null && searchMatterGeneral.getToCaseOpenDate() != null) {
             predicates.add(cb.between(root.get("caseOpenedDate"), 
            		 searchMatterGeneral.getFromCaseOpenDate(), searchMatterGeneral.getToCaseOpenDate()));
         }
         
         if (searchMatterGeneral.getFromCaseClosedDate() != null && searchMatterGeneral.getToCaseClosedDate() != null) {
             predicates.add(cb.between(root.get("caseClosedDate"), 
            		 searchMatterGeneral.getFromCaseClosedDate(), searchMatterGeneral.getToCaseClosedDate()));
         }
         
         if (searchMatterGeneral.getStatusId() != null && searchMatterGeneral.getStatusId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchMatterGeneral.getStatusId()));
         }
         
         if (searchMatterGeneral.getRefferedBy() != null && !searchMatterGeneral.getRefferedBy().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("referenceField12");	//REF_FIELD_12
        	 predicates.add(group.in(searchMatterGeneral.getRefferedBy()));
         }
         
         predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
         
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
