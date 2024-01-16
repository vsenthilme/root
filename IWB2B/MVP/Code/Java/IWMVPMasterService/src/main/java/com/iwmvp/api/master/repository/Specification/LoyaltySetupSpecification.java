package com.iwmvp.api.master.repository.Specification;

import com.iwmvp.api.master.model.loyaltysetup.*;
import org.springframework.context.annotation.DeferredImportSelector.Group;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class LoyaltySetupSpecification implements Specification<LoyaltySetup> {

	FindLoyaltySetup findLoyaltySetup;

	public LoyaltySetupSpecification(FindLoyaltySetup inputSearchParams) {
		this.findLoyaltySetup = inputSearchParams;
	}

	@Override
	public Predicate toPredicate(Root<LoyaltySetup> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (findLoyaltySetup.getCompanyId() != null && !findLoyaltySetup.getCompanyId().isEmpty()) {
			final Path<Group> group = root.<Group>get("companyId");
			predicates.add(group.in(findLoyaltySetup.getCompanyId()));
		}

		if (findLoyaltySetup.getCategoryId() != null && !findLoyaltySetup.getCategoryId().isEmpty()) {
			final Path<Group> group = root.<Group>get("categoryId");
			predicates.add(group.in(findLoyaltySetup.getCategoryId()));
		}

		if (findLoyaltySetup.getLoyaltyId() != null && !findLoyaltySetup.getLoyaltyId().isEmpty()) {
			final Path<Group> group = root.<Group>get("loyaltyId");
			predicates.add(group.in(findLoyaltySetup.getLoyaltyId()));
		}
		if (findLoyaltySetup.getStatus() != null && !findLoyaltySetup.getStatus().isEmpty()) {
			final Path<Group> group = root.<Group>get("status");
			predicates.add(group.in(findLoyaltySetup.getStatus()));
		}
		if (findLoyaltySetup.getCreatedBy() != null && !findLoyaltySetup.getCreatedBy().isEmpty()) {
			final Path<Group> group = root.<Group>get("createdBy");
			predicates.add(group.in(findLoyaltySetup.getCreatedBy()));
		}

		if (findLoyaltySetup.getStartDate() != null && findLoyaltySetup.getEndDate() != null) {
			predicates.add(cb.between(root.get("createdOn"),
					findLoyaltySetup.getStartDate(), findLoyaltySetup.getEndDate()));
		}

		predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
		return cb.and(predicates.toArray(new Predicate[] {}));
	}
}
