package com.tekclover.wms.api.transaction.repository.specification;

import com.tekclover.wms.api.transaction.model.outbound.pickup.v2.PickupLineV2;
import com.tekclover.wms.api.transaction.model.outbound.pickup.v2.SearchPickupLineV2;
import org.springframework.context.annotation.DeferredImportSelector.Group;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class PickupLineV2Specification implements Specification<PickupLineV2> {

    SearchPickupLineV2 searchPickupLine;

    public PickupLineV2Specification(SearchPickupLineV2 inputSearchParams) {
        this.searchPickupLine = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<PickupLineV2> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (searchPickupLine.getCompanyCodeId() != null && !searchPickupLine.getCompanyCodeId().isEmpty()) {
            final Path<Group> group = root.<Group>get("companyCodeId");
            predicates.add(group.in(searchPickupLine.getCompanyCodeId()));
        }

        if (searchPickupLine.getPlantId() != null && !searchPickupLine.getPlantId().isEmpty()) {
            final Path<Group> group = root.<Group>get("plantId");
            predicates.add(group.in(searchPickupLine.getPlantId()));
        }

        if (searchPickupLine.getLanguageId() != null && !searchPickupLine.getLanguageId().isEmpty()) {
            final Path<Group> group = root.<Group>get("languageId");
            predicates.add(group.in(searchPickupLine.getLanguageId()));
        }

        if (searchPickupLine.getWarehouseId() != null && !searchPickupLine.getWarehouseId().isEmpty()) {
            final Path<Group> group = root.<Group>get("warehouseId");
            predicates.add(group.in(searchPickupLine.getWarehouseId()));
        }

        if (searchPickupLine.getPreOutboundNo() != null && !searchPickupLine.getPreOutboundNo().isEmpty()) {
            final Path<Group> group = root.<Group>get("preOutboundNo");
            predicates.add(group.in(searchPickupLine.getPreOutboundNo()));
        }

        if (searchPickupLine.getRefDocNumber() != null && !searchPickupLine.getRefDocNumber().isEmpty()) {
            final Path<Group> group = root.<Group>get("refDocNumber");
            predicates.add(group.in(searchPickupLine.getRefDocNumber()));
        }

        if (searchPickupLine.getPartnerCode() != null && !searchPickupLine.getPartnerCode().isEmpty()) {
            final Path<Group> group = root.<Group>get("partnerCode");
            predicates.add(group.in(searchPickupLine.getPartnerCode()));
        }

        if (searchPickupLine.getLineNumber() != null && !searchPickupLine.getLineNumber().isEmpty()) {
            final Path<Group> group = root.<Group>get("lineNumber");
            predicates.add(group.in(searchPickupLine.getLineNumber()));
        }

        if (searchPickupLine.getPickupNumber() != null && !searchPickupLine.getPickupNumber().isEmpty()) {
            final Path<Group> group = root.<Group>get("pickupNumber");
            predicates.add(group.in(searchPickupLine.getPickupNumber()));
        }

        if (searchPickupLine.getItemCode() != null && !searchPickupLine.getItemCode().isEmpty()) {
            final Path<Group> group = root.<Group>get("itemCode");
            predicates.add(group.in(searchPickupLine.getItemCode()));
        }

        if (searchPickupLine.getActualHeNo() != null && !searchPickupLine.getActualHeNo().isEmpty()) {
            final Path<Group> group = root.<Group>get("actualHeNo");
            predicates.add(group.in(searchPickupLine.getActualHeNo()));
        }

        if (searchPickupLine.getPickedStorageBin() != null && !searchPickupLine.getPickedStorageBin().isEmpty()) {
            final Path<Group> group = root.<Group>get("pickedStorageBin");
            predicates.add(group.in(searchPickupLine.getPickedStorageBin()));
        }

        if (searchPickupLine.getPickedPackCode() != null && !searchPickupLine.getPickedPackCode().isEmpty()) {
            final Path<Group> group = root.<Group>get("pickedPackCode");
            predicates.add(group.in(searchPickupLine.getPickedPackCode()));
        }

        if (searchPickupLine.getStatusId() != null && !searchPickupLine.getStatusId().isEmpty()) {
            final Path<Group> group = root.<Group>get("statusId");
            predicates.add(group.in(searchPickupLine.getStatusId()));
        }

        if (searchPickupLine.getLevelId() != null && !searchPickupLine.getLevelId().isEmpty()) {
            final Path<Group> group = root.<Group>get("levelId");
            predicates.add(group.in(searchPickupLine.getLevelId()));
        }
        if (searchPickupLine.getOutboundOrderTypeId() != null && !searchPickupLine.getOutboundOrderTypeId().isEmpty()) {
            final Path<Group> group = root.<Group>get("outboundOrderTypeId");
            predicates.add(group.in(searchPickupLine.getOutboundOrderTypeId()));
        }

        if (searchPickupLine.getFromPickConfirmedOn() != null && searchPickupLine.getToPickConfirmedOn() != null) {
            predicates.add(cb.between(root.get("pickupConfirmedOn"), searchPickupLine.getFromPickConfirmedOn(), searchPickupLine.getToPickConfirmedOn()));
        }

        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));

        return cb.and(predicates.toArray(new Predicate[]{}));
    }
}
