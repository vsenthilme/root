package com.iweb2b.api.integration.repository.Specification;

import com.iweb2b.api.integration.model.consignment.dto.FindConsignment;
import com.iweb2b.api.integration.model.consignment.entity.ConsignmentEntity;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ConsignmentSpecification implements Specification<ConsignmentEntity> {

    FindConsignment findConsignment;

    public ConsignmentSpecification(FindConsignment inputSearchParams) {
        this.findConsignment = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<ConsignmentEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (findConsignment.getConsignmentId() != null && !findConsignment.getConsignmentId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("consignmentId");
            predicates.add(group.in(findConsignment.getConsignmentId()));
        }
        if (findConsignment.getReference_number() != null && !findConsignment.getReference_number().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("reference_number");
            predicates.add(group.in(findConsignment.getReference_number()));
        }
        if (findConsignment.getCustomer_code() != null && !findConsignment.getCustomer_code().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("customer_code");
            predicates.add(group.in(findConsignment.getCustomer_code()));
        }
        if (findConsignment.getService_type_id() != null && !findConsignment.getService_type_id().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("service_type_id");
            predicates.add(group.in(findConsignment.getService_type_id()));
        }
        if (findConsignment.getConsignment_type() != null && !findConsignment.getConsignment_type().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("consignment_type");
            predicates.add(group.in(findConsignment.getConsignment_type()));
        }
        if (findConsignment.getCustomer_reference_number() != null && !findConsignment.getCustomer_reference_number().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("customer_reference_number");
            predicates.add(group.in(findConsignment.getCustomer_reference_number()));
        }
        if (findConsignment.getCustomer_civil_id() != null && !findConsignment.getCustomer_civil_id().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("customer_civil_id");
            predicates.add(group.in(findConsignment.getCustomer_civil_id()));
        }
        if (findConsignment.getReceiver_civil_id() != null && !findConsignment.getReceiver_civil_id().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("receiver_civil_id");
            predicates.add(group.in(findConsignment.getReceiver_civil_id()));
        }
        if (findConsignment.getAwb_3rd_Party() != null && !findConsignment.getAwb_3rd_Party().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("awb_3rd_Party");
            predicates.add(group.in(findConsignment.getAwb_3rd_Party()));
        }
        if (findConsignment.getScanType_3rd_Party() != null && !findConsignment.getScanType_3rd_Party().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("scanType_3rd_Party");
            predicates.add(group.in(findConsignment.getScanType_3rd_Party()));
        }
        if (findConsignment.getHubCode_3rd_Party() != null && !findConsignment.getHubCode_3rd_Party().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("hubCode_3rd_Party");
            predicates.add(group.in(findConsignment.getHubCode_3rd_Party()));
        }
        if (findConsignment.getOrderType() != null && !findConsignment.getOrderType().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("orderType");
            predicates.add(group.in(findConsignment.getOrderType()));
        }
        if (findConsignment.getJntPushStatus() != null && !findConsignment.getJntPushStatus().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("jntPushStatus");
            predicates.add(group.in(findConsignment.getJntPushStatus()));
        }
        if (findConsignment.getBoutiqaatPushStatus() != null && !findConsignment.getBoutiqaatPushStatus().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("boutiqaatPushStatus");
            predicates.add(group.in(findConsignment.getBoutiqaatPushStatus()));
        }
        if (findConsignment.getStartDate() != null && findConsignment.getEndDate() != null) {
            predicates.add(cb.between(root.get("created_at"), findConsignment.getStartDate(), findConsignment.getEndDate()));
        }

        return cb.and(predicates.toArray(new Predicate[] {}));
    }
}
