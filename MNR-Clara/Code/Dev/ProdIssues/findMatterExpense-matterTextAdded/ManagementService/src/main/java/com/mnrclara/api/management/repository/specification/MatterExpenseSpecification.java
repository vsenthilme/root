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

import com.mnrclara.api.management.model.matterexpense.MatterExpense;
import com.mnrclara.api.management.model.matterexpense.SearchMatterExpense;

@SuppressWarnings("serial")
public class MatterExpenseSpecification implements Specification<MatterExpense> {
	
	SearchMatterExpense searchMatterExpense;
	
	public MatterExpenseSpecification(SearchMatterExpense inputSearchParams) {
		this.searchMatterExpense = inputSearchParams;
	}
	 
	@Override
     public Predicate toPredicate(Root<MatterExpense> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

         List<Predicate> predicates = new ArrayList<Predicate>();

         if (searchMatterExpense.getMatterNumber() != null && !searchMatterExpense.getMatterNumber().isEmpty()) {
        	 predicates.add(cb.equal(root.get("matterNumber"), searchMatterExpense.getMatterNumber()));
         }
         
         if (searchMatterExpense.getExpenseCode() != null && searchMatterExpense.getExpenseCode().size() > 0) {
        	 final Path<Group> group = root.<Group> get("expenseCode");
        	 predicates.add(group.in(searchMatterExpense.getExpenseCode()));
         }
         
         if (searchMatterExpense.getExpenseType() != null && searchMatterExpense.getExpenseType().size() > 0) {
        	 final Path<Group> group = root.<Group> get("expenseType");
        	 predicates.add(group.in(searchMatterExpense.getExpenseType()));
         }
         
         if (searchMatterExpense.getCreatedBy() != null && searchMatterExpense.getCreatedBy().size() > 0) {
        	 final Path<Group> group = root.<Group> get("createdBy");
        	 predicates.add(group.in(searchMatterExpense.getCreatedBy()));
         }
         
         if (searchMatterExpense.getStatusId() != null && searchMatterExpense.getStatusId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchMatterExpense.getStatusId()));
         }
         
         if (searchMatterExpense.getStartCreatedOn() != null && searchMatterExpense.getEndCreatedOn() != null) {
             predicates.add(cb.between(root.get("referenceField2"), searchMatterExpense.getStartCreatedOn(), searchMatterExpense.getEndCreatedOn()));
         }
         predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
