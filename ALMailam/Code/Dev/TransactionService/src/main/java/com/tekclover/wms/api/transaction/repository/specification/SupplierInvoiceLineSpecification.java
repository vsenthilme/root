package com.tekclover.wms.api.transaction.repository.specification;

import com.tekclover.wms.api.transaction.model.inbound.v2.SearchSupplierInvoiceLine;
import com.tekclover.wms.api.transaction.model.inbound.v2.SupplierInvoiceLine;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class SupplierInvoiceLineSpecification implements Specification<SupplierInvoiceLine> {

    SearchSupplierInvoiceLine searchSupplierInvoiceLine;

    public SupplierInvoiceLineSpecification(SearchSupplierInvoiceLine inputSearchParams) {
        this.searchSupplierInvoiceLine = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<SupplierInvoiceLine> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<Predicate>();

        if (searchSupplierInvoiceLine.getWarehouseId() != null && !searchSupplierInvoiceLine.getWarehouseId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("warehouseId");
            predicates.add(group.in(searchSupplierInvoiceLine.getWarehouseId()));
        }

        if (searchSupplierInvoiceLine.getStartConfirmedOn() != null && searchSupplierInvoiceLine.getEndConfirmedOn() != null) {
            predicates.add(cb.between(root.get("confirmedOn"), searchSupplierInvoiceLine.getStartConfirmedOn(),
                    searchSupplierInvoiceLine.getEndConfirmedOn()));
        }

        if (searchSupplierInvoiceLine.getStatusId() != null && !searchSupplierInvoiceLine.getStatusId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("statusId");
            predicates.add(group.in(searchSupplierInvoiceLine.getStatusId()));
        }

        if (searchSupplierInvoiceLine.getCompanyCodeId() != null && !searchSupplierInvoiceLine.getCompanyCodeId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("companyCode");
            predicates.add(group.in(searchSupplierInvoiceLine.getCompanyCodeId()));
        }

        if (searchSupplierInvoiceLine.getItemCode() != null && !searchSupplierInvoiceLine.getItemCode().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("itemCode");
            predicates.add(group.in(searchSupplierInvoiceLine.getItemCode()));
        }

        if (searchSupplierInvoiceLine.getRefDocNumber() != null && !searchSupplierInvoiceLine.getRefDocNumber().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("refDocNumber");
            predicates.add(group.in(searchSupplierInvoiceLine.getRefDocNumber()));
        }

        if (searchSupplierInvoiceLine.getPlantId() != null && !searchSupplierInvoiceLine.getPlantId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("plantId");
            predicates.add(group.in(searchSupplierInvoiceLine.getPlantId()));
        }

        if (searchSupplierInvoiceLine.getLanguageId() != null && !searchSupplierInvoiceLine.getLanguageId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("languageId");
            predicates.add(group.in(searchSupplierInvoiceLine.getLanguageId()));
        }

        if (searchSupplierInvoiceLine.getReferenceField1() != null) {
            predicates.add(cb.equal(root.get("referenceField1"), searchSupplierInvoiceLine.getReferenceField1()));
        }
        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
        return cb.and(predicates.toArray(new Predicate[]{}));
    }
}
