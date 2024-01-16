package com.iwmvp.api.master.repository.Specification;

import com.iwmvp.api.master.model.numberrange.*;
import org.springframework.context.annotation.DeferredImportSelector.Group;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class NumberRangeSpecification implements Specification<NumberRange> {

	FindNumberRange findNumberRange;

	public NumberRangeSpecification(FindNumberRange inputSearchParams) {
		this.findNumberRange = inputSearchParams;
	}

	@Override
	public Predicate toPredicate(Root<NumberRange> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (findNumberRange.getCompanyId() != null && !findNumberRange.getCompanyId().isEmpty()) {
			final Path<Group> group = root.<Group>get("companyId");
			predicates.add(group.in(findNumberRange.getCompanyId()));
		}

		if (findNumberRange.getNumberRangeCode() != null && !findNumberRange.getNumberRangeCode().isEmpty()) {
			final Path<Group> group = root.<Group>get("numberRangeCode");
			predicates.add(group.in(findNumberRange.getNumberRangeCode()));
		}
		if (findNumberRange.getNumberRangeObject() != null && !findNumberRange.getNumberRangeObject().isEmpty()) {
			final Path<Group> group = root.<Group>get("numberRangeObject");
			predicates.add(group.in(findNumberRange.getNumberRangeObject()));
		}

		if (findNumberRange.getNumberRangeCurrent() != null && !findNumberRange.getNumberRangeCurrent().isEmpty()) {
			final Path<Group> group = root.<Group>get("numberRangeCurrent");
			predicates.add(group.in(findNumberRange.getNumberRangeCurrent()));
		}
		if (findNumberRange.getNumberRangeStatus() != null && !findNumberRange.getNumberRangeStatus().isEmpty()) {
			final Path<Group> group = root.<Group>get("numberRangeStatus");
			predicates.add(group.in(findNumberRange.getNumberRangeStatus()));
		}
		if (findNumberRange.getCreatedBy() != null && !findNumberRange.getCreatedBy().isEmpty()) {
			final Path<Group> group = root.<Group>get("createdBy");
			predicates.add(group.in(findNumberRange.getCreatedBy()));
		}

		if (findNumberRange.getStartDate() != null && findNumberRange.getEndDate() != null) {
			predicates.add(cb.between(root.get("createdOn"),
					findNumberRange.getStartDate(), findNumberRange.getEndDate()));
		}

		predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
		return cb.and(predicates.toArray(new Predicate[] {}));
	}
}
