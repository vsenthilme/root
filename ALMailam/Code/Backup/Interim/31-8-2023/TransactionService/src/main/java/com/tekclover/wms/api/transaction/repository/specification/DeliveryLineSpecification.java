package com.tekclover.wms.api.transaction.repository.specification;

import com.tekclover.wms.api.transaction.model.deliverymodule.deliveryline.DeliveryLine;
import com.tekclover.wms.api.transaction.model.deliverymodule.deliveryline.SearchDeliveryLine;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class DeliveryLineSpecification implements Specification<DeliveryLine> {

    SearchDeliveryLine searchDeliveryLine;

    public DeliveryLineSpecification(SearchDeliveryLine inputSearchParams) {
        this.searchDeliveryLine = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<DeliveryLine> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (searchDeliveryLine.getWarehouseId() != null && !searchDeliveryLine.getWarehouseId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group> get("warehouseId");
            predicates.add(group.in(searchDeliveryLine.getWarehouseId()));
        }

        if (searchDeliveryLine.getDeliveryNo() != null && !searchDeliveryLine.getDeliveryNo().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group> get("deliveryNo");
            predicates.add(group.in(searchDeliveryLine.getDeliveryNo()));
        }

        if (searchDeliveryLine.getPlantId() != null && !searchDeliveryLine.getPlantId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group> get("plantId");
            predicates.add(group.in(searchDeliveryLine.getPlantId()));
        }

        if (searchDeliveryLine.getCompanyCodeId() != null && !searchDeliveryLine.getCompanyCodeId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group> get("companyCodeId");
            predicates.add(group.in(searchDeliveryLine.getCompanyCodeId()));
        }

        if (searchDeliveryLine.getLanguageId() != null && !searchDeliveryLine.getLanguageId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group> get("languageId");
            predicates.add(group.in(searchDeliveryLine.getLanguageId()));
        }

        if (searchDeliveryLine.getLineNo() != null && !searchDeliveryLine.getLineNo().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group> get("lineNo");
            predicates.add(group.in(searchDeliveryLine.getLineNo()));
        }

        if (searchDeliveryLine.getItemCode() != null && !searchDeliveryLine.getItemCode().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group> get("itemCode");
            predicates.add(group.in(searchDeliveryLine.getItemCode()));
        }

        if (searchDeliveryLine.getInvoiceNumber() != null && !searchDeliveryLine.getInvoiceNumber().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group> get("invoiceNumber");
            predicates.add(group.in(searchDeliveryLine.getInvoiceNumber()));
        }

        if (searchDeliveryLine.getRefDocNumber() != null && !searchDeliveryLine.getRefDocNumber().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group> get("refDocNumber");
            predicates.add(group.in(searchDeliveryLine.getRefDocNumber()));
        }
        return cb.and(predicates.toArray(new Predicate[] {}));
    }
}
