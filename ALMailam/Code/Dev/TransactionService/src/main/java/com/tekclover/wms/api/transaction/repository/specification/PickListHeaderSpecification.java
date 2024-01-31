package com.tekclover.wms.api.transaction.repository.specification;

import com.tekclover.wms.api.transaction.model.outbound.v2.PickListHeader;
import com.tekclover.wms.api.transaction.model.outbound.v2.SearchPickListHeader;
import org.springframework.context.annotation.DeferredImportSelector.Group;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class PickListHeaderSpecification implements Specification<PickListHeader> {

    SearchPickListHeader searchPickListHeader;

    public PickListHeaderSpecification(SearchPickListHeader inputSearchParams) {
        this.searchPickListHeader = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<PickListHeader> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (searchPickListHeader.getCompanyCodeId() != null && !searchPickListHeader.getCompanyCodeId().isEmpty()) {
            final Path<Group> group = root.<Group>get("companyCodeId");
            predicates.add(group.in(searchPickListHeader.getCompanyCodeId()));
        }

        if (searchPickListHeader.getPlantId() != null && !searchPickListHeader.getPlantId().isEmpty()) {
            final Path<Group> group = root.<Group>get("plantId");
            predicates.add(group.in(searchPickListHeader.getPlantId()));
        }

        if (searchPickListHeader.getLanguageId() != null && !searchPickListHeader.getLanguageId().isEmpty()) {
            final Path<Group> group = root.<Group>get("languageId");
            predicates.add(group.in(searchPickListHeader.getLanguageId()));
        }

        if (searchPickListHeader.getWarehouseId() != null && !searchPickListHeader.getWarehouseId().isEmpty()) {
            final Path<Group> group = root.<Group>get("warehouseId");
            predicates.add(group.in(searchPickListHeader.getWarehouseId()));
        }

        if (searchPickListHeader.getRefDocNumber() != null && !searchPickListHeader.getRefDocNumber().isEmpty()) {
            final Path<Group> group = root.<Group>get("refDocNumber");
            predicates.add(group.in(searchPickListHeader.getRefDocNumber()));
        }

        if (searchPickListHeader.getPartnerCode() != null && !searchPickListHeader.getPartnerCode().isEmpty()) {
            final Path<Group> group = root.<Group>get("partnerCode");
            predicates.add(group.in(searchPickListHeader.getPartnerCode()));
        }

        if (searchPickListHeader.getOutboundOrderTypeId() != null && !searchPickListHeader.getOutboundOrderTypeId().isEmpty()) {
            final Path<Group> group = root.<Group>get("outboundOrderTypeId");
            predicates.add(group.in(searchPickListHeader.getOutboundOrderTypeId()));
        }

        if (searchPickListHeader.getStatusId() != null && !searchPickListHeader.getStatusId().isEmpty()) {
            final Path<Group> group = root.<Group>get("statusId");
            predicates.add(group.in(searchPickListHeader.getStatusId()));
        }

        if (searchPickListHeader.getSoType() != null && !searchPickListHeader.getSoType().isEmpty()) {
            final Path<Group> group = root.<Group>get("referenceField1");
            predicates.add(group.in(searchPickListHeader.getSoType()));
        }

        if (searchPickListHeader.getStartRequiredDeliveryDate() != null && searchPickListHeader.getEndRequiredDeliveryDate() != null) {
            predicates.add(cb.between(root.get("requiredDeliveryDate"), searchPickListHeader.getStartRequiredDeliveryDate(), searchPickListHeader.getEndRequiredDeliveryDate()));
        }

        if (searchPickListHeader.getStartDeliveryConfirmedOn() != null && searchPickListHeader.getEndDeliveryConfirmedOn() != null) {
            predicates.add(cb.between(root.get("deliveryConfirmedOn"), searchPickListHeader.getStartDeliveryConfirmedOn(), searchPickListHeader.getEndDeliveryConfirmedOn()));
        }

        if (searchPickListHeader.getStartOrderDate() != null && searchPickListHeader.getEndOrderDate() != null) {
            predicates.add(cb.between(root.get("createdOn"), searchPickListHeader.getStartOrderDate(), searchPickListHeader.getEndOrderDate()));
        }

        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
        return cb.and(predicates.toArray(new Predicate[]{}));
    }
}
