package com.iwmvp.api.master.repository.Specification;

import com.iwmvp.api.master.model.loyaltycategory.*;
import org.springframework.context.annotation.DeferredImportSelector.Group;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class LoyaltyCategorySpecification implements Specification<LoyaltyCategory> {

	FindLoyaltyCategory findLoyaltyCategory;

	public LoyaltyCategorySpecification(FindLoyaltyCategory inputSearchParams) {
		this.findLoyaltyCategory = inputSearchParams;
	}

	@Override
	public Predicate toPredicate(Root<LoyaltyCategory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (findLoyaltyCategory.getCompanyId() != null && !findLoyaltyCategory.getCompanyId().isEmpty()) {
			final Path<Group> group = root.<Group>get("companyId");
			predicates.add(group.in(findLoyaltyCategory.getCompanyId()));
		}
		if (findLoyaltyCategory.getRangeId() != null && !findLoyaltyCategory.getRangeId().isEmpty()) {
			final Path<Group> group = root.<Group>get("rangeId");
			predicates.add(group.in(findLoyaltyCategory.getRangeId()));
		}

		if (findLoyaltyCategory.getCategoryId() != null && !findLoyaltyCategory.getCategoryId().isEmpty()) {
			final Path<Group> group = root.<Group>get("categoryId");
			predicates.add(group.in(findLoyaltyCategory.getCategoryId()));
		}

		if (findLoyaltyCategory.getCategory() != null && !findLoyaltyCategory.getCategory().isEmpty()) {
			final Path<Group> group = root.<Group>get("category");
			predicates.add(group.in(findLoyaltyCategory.getCategory()));
		}
		if (findLoyaltyCategory.getStatus() != null && !findLoyaltyCategory.getStatus().isEmpty()) {
			final Path<Group> group = root.<Group>get("status");
			predicates.add(group.in(findLoyaltyCategory.getStatus()));
		}
		if (findLoyaltyCategory.getCreatedBy() != null && !findLoyaltyCategory.getCreatedBy().isEmpty()) {
			final Path<Group> group = root.<Group>get("createdBy");
			predicates.add(group.in(findLoyaltyCategory.getCreatedBy()));
		}

		if (findLoyaltyCategory.getStartDate() != null && findLoyaltyCategory.getEndDate() != null) {
			predicates.add(cb.between(root.get("createdOn"),
					findLoyaltyCategory.getStartDate(), findLoyaltyCategory.getEndDate()));
		}

		predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
		return cb.and(predicates.toArray(new Predicate[] {}));
	}
}
