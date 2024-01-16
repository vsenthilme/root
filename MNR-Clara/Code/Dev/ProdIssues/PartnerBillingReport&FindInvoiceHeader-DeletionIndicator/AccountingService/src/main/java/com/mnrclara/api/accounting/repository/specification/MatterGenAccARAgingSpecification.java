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

import com.mnrclara.api.accounting.model.invoice.report.SearchMatterGenAccReport;
import com.mnrclara.api.accounting.model.prebill.MatterGenAcc;

@SuppressWarnings("serial")
public class MatterGenAccARAgingSpecification implements Specification<MatterGenAcc> {
	
	SearchMatterGenAccReport searchMatterGeneral;
	
	public MatterGenAccARAgingSpecification(SearchMatterGenAccReport inputSearchParams) {
		this.searchMatterGeneral = inputSearchParams;
	}
	 
	@Override
     public Predicate toPredicate(Root<MatterGenAcc> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

         List<Predicate> predicates = new ArrayList<Predicate>();

         if (searchMatterGeneral.getMatterNumber() != null && !searchMatterGeneral.getMatterNumber().isEmpty()) {
        	 predicates.add(cb.equal(root.get("matterNumber"), searchMatterGeneral.getMatterNumber()));
         }
         
         if (searchMatterGeneral.getCaseCategoryId() != null && searchMatterGeneral.getCaseCategoryId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("caseCategoryId");
        	 predicates.add(group.in(searchMatterGeneral.getCaseCategoryId()));
         }
         
         if (searchMatterGeneral.getCaseSubCategoryId() != null && searchMatterGeneral.getCaseSubCategoryId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("caseSubCategoryId");
        	 predicates.add(group.in(searchMatterGeneral.getCaseSubCategoryId()));
         }
         
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
