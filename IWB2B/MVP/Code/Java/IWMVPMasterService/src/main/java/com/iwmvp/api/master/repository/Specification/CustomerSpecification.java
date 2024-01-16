package com.iwmvp.api.master.repository.Specification;

import com.iwmvp.api.master.model.customer.*;
import org.springframework.context.annotation.DeferredImportSelector.Group;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class CustomerSpecification implements Specification<Customer> {

	FindCustomer findCustomer;

	public CustomerSpecification(FindCustomer inputSearchParams) {
		this.findCustomer = inputSearchParams;
	}

	@Override
	public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (findCustomer.getCustomerId() != null && !findCustomer.getCustomerId().isEmpty()) {
			final Path<Group> group = root.<Group>get("customerId");
			predicates.add(group.in(findCustomer.getCustomerId()));
		}

		if (findCustomer.getCompanyId() != null && !findCustomer.getCompanyId().isEmpty()) {
			final Path<Group> group = root.<Group>get("companyId");
			predicates.add(group.in(findCustomer.getCompanyId()));
		}

		if (findCustomer.getCustomerType() != null && !findCustomer.getCustomerType().isEmpty()) {
			final Path<Group> group = root.<Group>get("customerType");
			predicates.add(group.in(findCustomer.getCustomerType()));
		}

		if (findCustomer.getCustomerName() != null && !findCustomer.getCustomerName().isEmpty()) {
			final Path<Group> group = root.<Group>get("customerName");
			predicates.add(group.in(findCustomer.getCustomerName()));
		}

		if (findCustomer.getCustomerCategory() != null && !findCustomer.getCustomerCategory().isEmpty()) {
			final Path<Group> group = root.<Group>get("customerCategory");
			predicates.add(group.in(findCustomer.getCustomerCategory()));
		}

		if (findCustomer.getPhoneNo() != null && !findCustomer.getPhoneNo().isEmpty()) {
			final Path<Group> group = root.<Group>get("phoneNo");
			predicates.add(group.in(findCustomer.getPhoneNo()));
		}

		if (findCustomer.getAlternatePhoneNo() != null && !findCustomer.getAlternatePhoneNo().isEmpty()) {
			final Path<Group> group = root.<Group>get("alternatePhoneNo");
			predicates.add(group.in(findCustomer.getAlternatePhoneNo()));
		}

		if (findCustomer.getCivilId() != null && !findCustomer.getCivilId().isEmpty()) {
			final Path<Group> group = root.<Group>get("civilId");
			predicates.add(group.in(findCustomer.getCivilId()));
		}

		if (findCustomer.getEmailId() != null && !findCustomer.getEmailId().isEmpty()) {
			final Path<Group> group = root.<Group>get("emailId");
			predicates.add(group.in(findCustomer.getEmailId()));
		}
		if (findCustomer.getCity() != null && !findCustomer.getCity().isEmpty()) {
			final Path<Group> group = root.<Group>get("city");
			predicates.add(group.in(findCustomer.getCity()));
		}
		if (findCustomer.getCountry() != null && !findCustomer.getCountry().isEmpty()) {
			final Path<Group> group = root.<Group>get("country");
			predicates.add(group.in(findCustomer.getCountry()));
		}
		if (findCustomer.getUserName() != null && !findCustomer.getUserName().isEmpty()) {
			final Path<Group> group = root.<Group>get("userName");
			predicates.add(group.in(findCustomer.getUserName()));
		}
		if (findCustomer.getStatus() != null && !findCustomer.getStatus().isEmpty()) {
			final Path<Group> group = root.<Group>get("status");
			predicates.add(group.in(findCustomer.getStatus()));
		}
		if (findCustomer.getCreatedBy() != null && !findCustomer.getCreatedBy().isEmpty()) {
			final Path<Group> group = root.<Group>get("createdBy");
			predicates.add(group.in(findCustomer.getCreatedBy()));
		}

		if (findCustomer.getStartDate() != null && findCustomer.getEndDate() != null) {
			predicates.add(cb.between(root.get("createdOn"),
					findCustomer.getStartDate(), findCustomer.getEndDate()));
		}

		predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
		return cb.and(predicates.toArray(new Predicate[] {}));
	}
}
