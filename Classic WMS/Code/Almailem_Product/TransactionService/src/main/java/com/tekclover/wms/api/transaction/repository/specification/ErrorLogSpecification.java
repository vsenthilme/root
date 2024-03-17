package com.tekclover.wms.api.transaction.repository.specification;

import com.tekclover.wms.api.transaction.model.errorlog.ErrorLog;
import com.tekclover.wms.api.transaction.model.errorlog.FindErrorLog;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ErrorLogSpecification implements Specification<ErrorLog> {

    FindErrorLog findErrorLog;

    public ErrorLogSpecification(FindErrorLog inputSearchParams) {
        this.findErrorLog = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<ErrorLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (findErrorLog.getOrderTypeId() != null && !findErrorLog.getOrderTypeId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("orderTypeId");
            predicates.add(group.in(findErrorLog.getOrderTypeId()));
        }
        if (findErrorLog.getLanguageId() != null && !findErrorLog.getLanguageId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("languageId");
            predicates.add(group.in(findErrorLog.getLanguageId()));
        }
        if (findErrorLog.getCompanyCodeId() != null && !findErrorLog.getCompanyCodeId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("companyCodeId");
            predicates.add(group.in(findErrorLog.getCompanyCodeId()));
        }
        if (findErrorLog.getPlantId() != null && !findErrorLog.getPlantId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("plantId");
            predicates.add(group.in(findErrorLog.getPlantId()));
        }
        if (findErrorLog.getWarehouseId() != null && !findErrorLog.getWarehouseId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("warehouseId");
            predicates.add(group.in(findErrorLog.getWarehouseId()));
        }
        if (findErrorLog.getRefDocNumber() != null && !findErrorLog.getRefDocNumber().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("refDocNumber");
            predicates.add(group.in(findErrorLog.getRefDocNumber()));
        }
        if (findErrorLog.getFromOrderDate() != null && findErrorLog.getToOrderDate() != null) {
            predicates.add(cb.between(root.get("orderDate"), findErrorLog.getFromOrderDate(),
                    findErrorLog.getToOrderDate()));
        }
        return cb.and(predicates.toArray(new Predicate[]{}));
    }

}
