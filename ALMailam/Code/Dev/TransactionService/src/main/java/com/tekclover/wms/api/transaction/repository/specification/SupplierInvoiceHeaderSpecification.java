package com.tekclover.wms.api.transaction.repository.specification;

import com.tekclover.wms.api.transaction.model.inbound.v2.SearchSupplierInvoiceHeader;
import com.tekclover.wms.api.transaction.model.inbound.v2.SupplierInvoiceHeader;
import org.springframework.context.annotation.DeferredImportSelector.Group;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class SupplierInvoiceHeaderSpecification implements Specification<SupplierInvoiceHeader> {

    SearchSupplierInvoiceHeader searchSupplierInvoiceHeader;

    public SupplierInvoiceHeaderSpecification(SearchSupplierInvoiceHeader inputSearchParams) {
        this.searchSupplierInvoiceHeader = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<SupplierInvoiceHeader> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (searchSupplierInvoiceHeader.getCompanyCodeId() != null && !searchSupplierInvoiceHeader.getCompanyCodeId().isEmpty()) {
            final Path<Group> group = root.<Group>get("companyCode");
            predicates.add(group.in(searchSupplierInvoiceHeader.getCompanyCodeId()));
        }

        if (searchSupplierInvoiceHeader.getPlantId() != null && !searchSupplierInvoiceHeader.getPlantId().isEmpty()) {
            final Path<Group> group = root.<Group>get("plantId");
            predicates.add(group.in(searchSupplierInvoiceHeader.getPlantId()));
        }

        if (searchSupplierInvoiceHeader.getLanguageId() != null && !searchSupplierInvoiceHeader.getLanguageId().isEmpty()) {
            final Path<Group> group = root.<Group>get("languageId");
            predicates.add(group.in(searchSupplierInvoiceHeader.getLanguageId()));
        }

        if (searchSupplierInvoiceHeader.getWarehouseId() != null && !searchSupplierInvoiceHeader.getWarehouseId().isEmpty()) {
            final Path<Group> group = root.<Group>get("warehouseId");
            predicates.add(group.in(searchSupplierInvoiceHeader.getWarehouseId()));
        }

        if (searchSupplierInvoiceHeader.getRefDocNumber() != null && !searchSupplierInvoiceHeader.getRefDocNumber().isEmpty()) {
            final Path<Group> group = root.<Group>get("refDocNumber");
            predicates.add(group.in(searchSupplierInvoiceHeader.getRefDocNumber()));
        }

        if (searchSupplierInvoiceHeader.getInboundOrderTypeId() != null && !searchSupplierInvoiceHeader.getInboundOrderTypeId().isEmpty()) {
            final Path<Group> group = root.<Group>get("inboundOrderTypeId");
            predicates.add(group.in(searchSupplierInvoiceHeader.getInboundOrderTypeId()));
        }

        if (searchSupplierInvoiceHeader.getContainerNo() != null && !searchSupplierInvoiceHeader.getContainerNo().isEmpty()) {
            final Path<Group> group = root.<Group>get("containerNo");
            predicates.add(group.in(searchSupplierInvoiceHeader.getContainerNo()));
        }

        if (searchSupplierInvoiceHeader.getStatusId() != null && !searchSupplierInvoiceHeader.getStatusId().isEmpty()) {
            final Path<Group> group = root.<Group>get("statusId");
            predicates.add(group.in(searchSupplierInvoiceHeader.getStatusId()));
        }

        if (searchSupplierInvoiceHeader.getStartCreatedOn() != null && searchSupplierInvoiceHeader.getEndCreatedOn() != null) {
            predicates.add(cb.between(root.get("createdOn"), searchSupplierInvoiceHeader.getStartCreatedOn(), searchSupplierInvoiceHeader.getEndCreatedOn()));
        }

        if (searchSupplierInvoiceHeader.getStartConfirmedOn() != null && searchSupplierInvoiceHeader.getEndConfirmedOn() != null) {
            predicates.add(cb.between(root.get("confirmedOn"), searchSupplierInvoiceHeader.getStartConfirmedOn(), searchSupplierInvoiceHeader.getEndConfirmedOn()));
        }

        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));

        return cb.and(predicates.toArray(new Predicate[]{}));
    }
}
