package com.tekclover.wms.api.transaction.repository.specification;


import com.tekclover.wms.api.transaction.model.dto.FindTransactionError;
import com.tekclover.wms.api.transaction.model.dto.TransactionError;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class TransactionErrorSpecificationV2 implements Specification<TransactionError> {

    FindTransactionError findTransactionError;

    public TransactionErrorSpecificationV2(FindTransactionError inputSearchParams) {
        this.findTransactionError = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<TransactionError> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (findTransactionError.getErrorId() != null && !findTransactionError.getErrorId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("errorId");
            predicates.add(group.in(findTransactionError.getErrorId()));
        }

        if (findTransactionError.getTransaction() != null && !findTransactionError.getTransaction().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("transaction");
            predicates.add(group.in(findTransactionError.getTransaction()));
        }

        if (findTransactionError.getErrorType() != null && !findTransactionError.getErrorType().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("errorType");
            predicates.add(group.in(findTransactionError.getErrorType()));
        }

        if (findTransactionError.getCreatedBy() != null && !findTransactionError.getCreatedBy().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("createdBy");
            predicates.add(group.in(findTransactionError.getCreatedBy()));
        }

        if (findTransactionError.getTableName() != null && !findTransactionError.getTableName().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("tableName");
            predicates.add(group.in(findTransactionError.getTableName()));
        }

        if (findTransactionError.getCreatedBy() != null && !findTransactionError.getCreatedBy().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("caseCode");
            predicates.add(group.in(findTransactionError.getCreatedBy()));
        }

        if (findTransactionError.getStartCreatedOn() != null && findTransactionError.getEndCreatedOn() != null) {
            predicates.add(cb.between(root.get("createdOn"), findTransactionError.getStartCreatedOn(), findTransactionError.getEndCreatedOn()));
        }

        return cb.and(predicates.toArray(new Predicate[] {}));
    }

}
