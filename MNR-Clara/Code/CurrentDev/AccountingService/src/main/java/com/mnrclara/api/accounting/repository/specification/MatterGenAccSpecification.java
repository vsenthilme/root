package com.mnrclara.api.accounting.repository.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.context.annotation.DeferredImportSelector.Group;
import org.springframework.data.jpa.domain.Specification;

import com.mnrclara.api.accounting.model.management.SearchMatterGeneral;
import com.mnrclara.api.accounting.model.prebill.MatterGenAcc;

@SuppressWarnings("serial")
public class MatterGenAccSpecification implements Specification<MatterGenAcc> {
	
	SearchMatterGeneral searchMatterGeneral;
	
	public MatterGenAccSpecification(SearchMatterGeneral inputSearchParams) {
		this.searchMatterGeneral = inputSearchParams;
	}
	 
	@Override
     public Predicate toPredicate(Root<MatterGenAcc> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

         List<Predicate> predicates = new ArrayList<Predicate>();

         if (searchMatterGeneral.getClassId() != null && searchMatterGeneral.getClassId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("classId");
        	 predicates.add(group.in(searchMatterGeneral.getClassId()));
         }
         
         if (searchMatterGeneral.getMatterNumber() != null && !searchMatterGeneral.getMatterNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("matterNumber");
        	 predicates.add(group.in(searchMatterGeneral.getMatterNumber()));
         }
         
         if (searchMatterGeneral.getClientId() != null && !searchMatterGeneral.getClientId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("clientId");
        	 predicates.add(group.in(searchMatterGeneral.getClientId()));
         }
         
         if (searchMatterGeneral.getCaseCategory() != null && searchMatterGeneral.getCaseCategory().size() > 0) {
        	 final Path<Group> group = root.<Group> get("caseCategoryId");
        	 predicates.add(group.in(searchMatterGeneral.getCaseCategory()));
         }
         
         if (searchMatterGeneral.getCaseSubCategory() != null && searchMatterGeneral.getCaseSubCategory().size() > 0) {
        	 final Path<Group> group = root.<Group> get("caseSubCategoryId");
        	 predicates.add(group.in(searchMatterGeneral.getCaseSubCategory()));
         }
         
         if (searchMatterGeneral.getBillingMode() != null && !searchMatterGeneral.getBillingMode().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("billingMode");
        	 predicates.add(group.in(searchMatterGeneral.getBillingMode()));
         }
         
         if (searchMatterGeneral.getBillingFrequency() != null && searchMatterGeneral.getBillingFrequency().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("billingFrequency");
        	 predicates.add(group.in(searchMatterGeneral.getBillingFrequency()));
         }
      
         if (searchMatterGeneral.getBillingFormatCode() != null && searchMatterGeneral.getBillingFormatCode().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("billingFormatCode");
        	 predicates.add(group.in(searchMatterGeneral.getBillingFormatCode()));
         }
         
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
