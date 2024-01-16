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
import com.mnrclara.api.management.model.mattergeneral.SearchMatterGeneral;

@SuppressWarnings("serial")
public class MatterGenAccSpecification implements Specification<MatterGenAcc> {
	
	SearchMatterGeneral searchMatterGeneral;
	
	public MatterGenAccSpecification(SearchMatterGeneral inputSearchParams) {
		this.searchMatterGeneral = inputSearchParams;
	}
	 
	@Override
     public Predicate toPredicate(Root<MatterGenAcc> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

         List<Predicate> predicates = new ArrayList<Predicate>();

         if (searchMatterGeneral.getMatterNumber() != null && !searchMatterGeneral.getMatterNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("matterNumber");
        	 predicates.add(group.in(searchMatterGeneral.getMatterNumber()));
         }
         
         if (searchMatterGeneral.getMatterDescription() != null && !searchMatterGeneral.getMatterDescription().isEmpty()) {
        	 predicates.add(cb.like(root.get("matterDescription"), "%" + searchMatterGeneral.getMatterDescription() + "%"));
         }
         
         if (searchMatterGeneral.getClientId() != null && !searchMatterGeneral.getClientId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("clientId");
        	 predicates.add(group.in(searchMatterGeneral.getClientId()));
         }
         
         if (searchMatterGeneral.getCaseInformationNo() != null && !searchMatterGeneral.getCaseInformationNo().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("caseInformationNo");
        	 predicates.add(group.in(searchMatterGeneral.getCaseInformationNo()));
         }
         
         if (searchMatterGeneral.getCaseCategoryId() != null && searchMatterGeneral.getCaseCategoryId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("caseCategoryId");
        	 predicates.add(group.in(searchMatterGeneral.getCaseCategoryId()));
         }
         
         if (searchMatterGeneral.getStatusId() != null && searchMatterGeneral.getStatusId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchMatterGeneral.getStatusId()));
         }
         
         if (searchMatterGeneral.getClassId() != null && searchMatterGeneral.getClassId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("classId");
        	 predicates.add(group.in(searchMatterGeneral.getClassId()));
         }
         
         if (searchMatterGeneral.getCaseOpenedDate1() != null && searchMatterGeneral.getCaseOpenedDate2() != null) {
             predicates.add(cb.between(root.get("caseOpenedDate"), searchMatterGeneral.getCaseOpenedDate1(), searchMatterGeneral.getCaseOpenedDate2()));
         }
         
         predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
         
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
