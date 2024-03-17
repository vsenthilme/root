package com.mnrclara.api.management.repository.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.mnrclara.api.management.model.mattergeneral.MatterGenAcc;
import com.mnrclara.api.management.model.mattergeneral.SearchWIPAgedPBReport;

@SuppressWarnings("serial")
public class MatterGeneralWIPAgedPBReportSpecification implements Specification<MatterGenAcc> {
	
	SearchWIPAgedPBReport searchMatterGeneral;
	
	public MatterGeneralWIPAgedPBReportSpecification(SearchWIPAgedPBReport inputSearchParams) {
		this.searchMatterGeneral = inputSearchParams;
	}
	 
	@Override
     public Predicate toPredicate(Root<MatterGenAcc> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

         List<Predicate> predicates = new ArrayList<Predicate>();
         
         if (searchMatterGeneral.getClassId() != null) {
        	 predicates.add(cb.equal(root.get("classId"), searchMatterGeneral.getClassId()));
         }
         
         if (searchMatterGeneral.getCaseCategoryId() != null) {
        	 predicates.add(cb.equal(root.get("caseCategoryId"), searchMatterGeneral.getCaseCategoryId()));
         }
         
         if (searchMatterGeneral.getCaseSubCategoryId() != null) {
        	 predicates.add(cb.equal(root.get("caseSubCategoryId"), searchMatterGeneral.getCaseSubCategoryId()));
         }
         
         if (searchMatterGeneral.getClientId() != null) {
        	 predicates.add(cb.equal(root.get("clientId"), searchMatterGeneral.getClientId()));
         }
        
         if (searchMatterGeneral.getMatterNumber() != null) {
        	 predicates.add(cb.equal(root.get("matterNumber"), searchMatterGeneral.getMatterNumber()));
         }
         
         if (searchMatterGeneral.getStatusId() != null) {
        	 predicates.add(cb.equal(root.get("statusId"), searchMatterGeneral.getStatusId()));
         }
         
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
