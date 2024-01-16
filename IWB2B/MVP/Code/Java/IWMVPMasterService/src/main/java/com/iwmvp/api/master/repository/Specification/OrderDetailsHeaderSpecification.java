package com.iwmvp.api.master.repository.Specification;

import com.iwmvp.api.master.model.orderdetails.*;
import org.springframework.context.annotation.DeferredImportSelector.Group;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class OrderDetailsHeaderSpecification implements Specification<OrderDetailsHeader> {

	FindOrderDetailsHeader findOrderDetailsHeader;

	public OrderDetailsHeaderSpecification(FindOrderDetailsHeader inputSearchParams) {
		this.findOrderDetailsHeader = inputSearchParams;
	}

	@Override
	public Predicate toPredicate(Root<OrderDetailsHeader> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (findOrderDetailsHeader.getCompanyId() != null && !findOrderDetailsHeader.getCompanyId().isEmpty()) {
			final Path<Group> group = root.<Group>get("companyId");
			predicates.add(group.in(findOrderDetailsHeader.getCompanyId()));
		}

		if (findOrderDetailsHeader.getOrderId() != null && !findOrderDetailsHeader.getOrderId().isEmpty()) {
			final Path<Group> group = root.<Group>get("orderId");
			predicates.add(group.in(findOrderDetailsHeader.getOrderId()));
		}

		if (findOrderDetailsHeader.getReferenceNo() != null && !findOrderDetailsHeader.getReferenceNo().isEmpty()) {
			final Path<Group> group = root.<Group>get("referenceNo");
			predicates.add(group.in(findOrderDetailsHeader.getReferenceNo()));
		}

		if (findOrderDetailsHeader.getShipsyOrderNo() != null && !findOrderDetailsHeader.getShipsyOrderNo().isEmpty()) {
			final Path<Group> group = root.<Group>get("shipsyOrderNo");
			predicates.add(group.in(findOrderDetailsHeader.getShipsyOrderNo()));
		}

		if (findOrderDetailsHeader.getCustomerId() != null && !findOrderDetailsHeader.getCustomerId().isEmpty()) {
			final Path<Group> group = root.<Group>get("customerId");
			predicates.add(group.in(findOrderDetailsHeader.getCustomerId()));
		}

		if (findOrderDetailsHeader.getLoadType() != null && !findOrderDetailsHeader.getLoadType().isEmpty()) {
			final Path<Group> group = root.<Group>get("loadType");
			predicates.add(group.in(findOrderDetailsHeader.getLoadType()));
		}

		if (findOrderDetailsHeader.getTypeOfDelivery() != null && !findOrderDetailsHeader.getTypeOfDelivery().isEmpty()) {
			final Path<Group> group = root.<Group>get("typeOfDelivery");
			predicates.add(group.in(findOrderDetailsHeader.getTypeOfDelivery()));
		}

		if (findOrderDetailsHeader.getOriginDetailsName() != null && !findOrderDetailsHeader.getOriginDetailsName().isEmpty()) {
			final Path<Group> group = root.<Group>get("originDetailsName");
			predicates.add(group.in(findOrderDetailsHeader.getOriginDetailsName()));
		}

		if (findOrderDetailsHeader.getOriginDetailsPincode() != null && !findOrderDetailsHeader.getOriginDetailsPincode().isEmpty()) {
			final Path<Group> group = root.<Group>get("originDetailsPincode");
			predicates.add(group.in(findOrderDetailsHeader.getOriginDetailsPincode()));
		}
		if (findOrderDetailsHeader.getDestinationDetailsName() != null && !findOrderDetailsHeader.getDestinationDetailsName().isEmpty()) {
			final Path<Group> group = root.<Group>get("destinationDetailsName");
			predicates.add(group.in(findOrderDetailsHeader.getDestinationDetailsName()));
		}
		if (findOrderDetailsHeader.getDestinationDetailsPincode() != null && !findOrderDetailsHeader.getDestinationDetailsPincode().isEmpty()) {
			final Path<Group> group = root.<Group>get("destinationDetailsPincode");
			predicates.add(group.in(findOrderDetailsHeader.getDestinationDetailsPincode()));
		}
		if (findOrderDetailsHeader.getServiceTypeId() != null && !findOrderDetailsHeader.getServiceTypeId().isEmpty()) {
			final Path<Group> group = root.<Group>get("serviceTypeId");
			predicates.add(group.in(findOrderDetailsHeader.getServiceTypeId()));
		}
		if (findOrderDetailsHeader.getOriginCountry() != null && !findOrderDetailsHeader.getOriginCountry().isEmpty()) {
			final Path<Group> group = root.<Group>get("originCountry");
			predicates.add(group.in(findOrderDetailsHeader.getOriginCountry()));
		}
		if (findOrderDetailsHeader.getOriginState() != null && !findOrderDetailsHeader.getOriginState().isEmpty()) {
			final Path<Group> group = root.<Group>get("originState");
			predicates.add(group.in(findOrderDetailsHeader.getOriginState()));
		}
		if (findOrderDetailsHeader.getOriginCity() != null && !findOrderDetailsHeader.getOriginCity().isEmpty()) {
			final Path<Group> group = root.<Group>get("originCity");
			predicates.add(group.in(findOrderDetailsHeader.getOriginCity()));
		}
		if (findOrderDetailsHeader.getDestinationCountry() != null && !findOrderDetailsHeader.getDestinationCountry().isEmpty()) {
			final Path<Group> group = root.<Group>get("destinationCountry");
			predicates.add(group.in(findOrderDetailsHeader.getDestinationCountry()));
		}
		if (findOrderDetailsHeader.getDestinationState() != null && !findOrderDetailsHeader.getDestinationState().isEmpty()) {
			final Path<Group> group = root.<Group>get("destinationState");
			predicates.add(group.in(findOrderDetailsHeader.getDestinationState()));
		}
		if (findOrderDetailsHeader.getDestinationCity() != null && !findOrderDetailsHeader.getDestinationCity().isEmpty()) {
			final Path<Group> group = root.<Group>get("destinationCity");
			predicates.add(group.in(findOrderDetailsHeader.getDestinationCity()));
		}
		if (findOrderDetailsHeader.getStatus() != null && !findOrderDetailsHeader.getStatus().isEmpty()) {
			final Path<Group> group = root.<Group>get("status");
			predicates.add(group.in(findOrderDetailsHeader.getStatus()));
		}
		if (findOrderDetailsHeader.getCreatedBy() != null && !findOrderDetailsHeader.getCreatedBy().isEmpty()) {
			final Path<Group> group = root.<Group>get("createdBy");
			predicates.add(group.in(findOrderDetailsHeader.getCreatedBy()));
		}

		if (findOrderDetailsHeader.getStartDate() != null && findOrderDetailsHeader.getEndDate() != null) {
			predicates.add(cb.between(root.get("createdOn"),
					findOrderDetailsHeader.getStartDate(), findOrderDetailsHeader.getEndDate()));
		}

		predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
		return cb.and(predicates.toArray(new Predicate[] {}));
	}
}
