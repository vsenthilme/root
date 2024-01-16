package com.tekclover.wms.api.transaction.repository.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.context.annotation.DeferredImportSelector.Group;
import org.springframework.data.jpa.domain.Specification;

import com.tekclover.wms.api.transaction.model.outbound.pickup.PickupHeader;
import com.tekclover.wms.api.transaction.model.outbound.pickup.SearchPickupHeader;

@SuppressWarnings("serial")
public class PickupHeaderSpecification implements Specification<PickupHeader> {

    SearchPickupHeader searchPickupHeader;

    public PickupHeaderSpecification(SearchPickupHeader inputSearchParams) {
        this.searchPickupHeader = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<PickupHeader> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (searchPickupHeader.getWarehouseId() != null && !searchPickupHeader.getWarehouseId().isEmpty()) {
            final Path<Group> group = root.<Group>get("warehouseId");
            predicates.add(group.in(searchPickupHeader.getWarehouseId()));
        }

        if (searchPickupHeader.getCompanyCodeId() != null && !searchPickupHeader.getCompanyCodeId().isEmpty()) {
            final Path<Group> group = root.<Group>get("companyCodeId");
            predicates.add(group.in(searchPickupHeader.getCompanyCodeId()));
        }

        if (searchPickupHeader.getPlantId() != null && !searchPickupHeader.getPlantId().isEmpty()) {
            final Path<Group> group = root.<Group>get("plantId");
            predicates.add(group.in(searchPickupHeader.getPlantId()));
        }

        if (searchPickupHeader.getLanguageId() != null && !searchPickupHeader.getLanguageId().isEmpty()) {
            final Path<Group> group = root.<Group>get("languageId");
            predicates.add(group.in(searchPickupHeader.getLanguageId()));
        }

        if (searchPickupHeader.getRefDocNumber() != null && !searchPickupHeader.getRefDocNumber().isEmpty()) {
            final Path<Group> group = root.<Group>get("refDocNumber");
            predicates.add(group.in(searchPickupHeader.getRefDocNumber()));
        }

        if (searchPickupHeader.getPartnerCode() != null && !searchPickupHeader.getPartnerCode().isEmpty()) {
            final Path<Group> group = root.<Group>get("partnerCode");
            predicates.add(group.in(searchPickupHeader.getPartnerCode()));
        }

        if (searchPickupHeader.getPickupNumber() != null && !searchPickupHeader.getPickupNumber().isEmpty()) {
            final Path<Group> group = root.<Group>get("pickupNumber");
            predicates.add(group.in(searchPickupHeader.getPickupNumber()));
        }

        if (searchPickupHeader.getItemCode() != null && !searchPickupHeader.getItemCode().isEmpty()) {
            final Path<Group> group = root.<Group>get("itemCode");
            predicates.add(group.in(searchPickupHeader.getItemCode()));
        }

        if (searchPickupHeader.getProposedStorageBin() != null && !searchPickupHeader.getProposedStorageBin().isEmpty()) {
            final Path<Group> group = root.<Group>get("proposedStorageBin");
            predicates.add(group.in(searchPickupHeader.getProposedStorageBin()));
        }

        if (searchPickupHeader.getProposedPackCode() != null && !searchPickupHeader.getProposedPackCode().isEmpty()) {
            final Path<Group> group = root.<Group>get("proposedPackBarCode");
            predicates.add(group.in(searchPickupHeader.getProposedPackCode()));
        }

        if (searchPickupHeader.getOutboundOrderTypeId() != null && !searchPickupHeader.getOutboundOrderTypeId().isEmpty()) {
            final Path<Group> group = root.<Group>get("outboundOrderTypeId");
            predicates.add(group.in(searchPickupHeader.getOutboundOrderTypeId()));
        }

        if (searchPickupHeader.getStatusId() != null && !searchPickupHeader.getStatusId().isEmpty()) {
            final Path<Group> group = root.<Group>get("statusId");
            predicates.add(group.in(searchPickupHeader.getStatusId()));
        }

        if (searchPickupHeader.getSoType() != null && !searchPickupHeader.getSoType().isEmpty()) {
            final Path<Group> group = root.<Group>get("referenceField1");
            predicates.add(group.in(searchPickupHeader.getSoType()));
        }

        if (searchPickupHeader.getAssignedPickerId() != null && !searchPickupHeader.getAssignedPickerId().isEmpty()) {
            final Path<Group> group = root.<Group>get("assignedPickerId");
            predicates.add(group.in(searchPickupHeader.getAssignedPickerId()));
        }

        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));

        return cb.and(predicates.toArray(new Predicate[]{}));
    }
}
