package com.mnrclara.api.accounting.repository.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.context.annotation.DeferredImportSelector.Group;
import org.springframework.data.jpa.domain.Specification;

import com.mnrclara.api.accounting.model.invoice.PaymentUpdate;
import com.mnrclara.api.accounting.model.invoice.SearchPaymentUpdate;

@SuppressWarnings("serial")
public class PaymentUpdateSpecification implements Specification<PaymentUpdate> {

	SearchPaymentUpdate searchPaymentUpdate;

	/**
	 * 
	 * @param inputSearchParams
	 */
	public PaymentUpdateSpecification(SearchPaymentUpdate inputSearchParams) {
		this.searchPaymentUpdate = inputSearchParams;
	}

	@Override
	public Predicate toPredicate(Root<PaymentUpdate> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (searchPaymentUpdate.getClientId() != null && !searchPaymentUpdate.getClientId().isEmpty()) {
			final Path<Group> group = root.<Group>get("clientId");
			predicates.add(group.in(searchPaymentUpdate.getClientId()));
		}
		
		if (searchPaymentUpdate.getPaymentId() != null && !searchPaymentUpdate.getPaymentId().isEmpty()) {
			final Path<Group> group = root.<Group>get("paymentId");
			predicates.add(group.in(searchPaymentUpdate.getPaymentId()));
		}

		if (searchPaymentUpdate.getMatterNumber() != null && !searchPaymentUpdate.getMatterNumber().isEmpty()) {
			final Path<Group> group = root.<Group>get("matterNumber");
			predicates.add(group.in(searchPaymentUpdate.getMatterNumber()));
		}

		if (searchPaymentUpdate.getInvoiceNumber() != null && !searchPaymentUpdate.getInvoiceNumber().isEmpty()) {
			final Path<Group> group = root.<Group>get("invoiceNumber");
			predicates.add(group.in(searchPaymentUpdate.getInvoiceNumber()));
		}

		if (searchPaymentUpdate.getTransactionType() != null && !searchPaymentUpdate.getTransactionType().isEmpty()) {
			final Path<Group> group = root.<Group>get("transactionType");
			predicates.add(group.in(searchPaymentUpdate.getTransactionType()));
		}

		if (searchPaymentUpdate.getPaymentNumber() != null && !searchPaymentUpdate.getPaymentNumber().isEmpty()) {
			final Path<Group> group = root.<Group>get("paymentNumber");
			predicates.add(group.in(searchPaymentUpdate.getPaymentNumber()));
		}

		if (searchPaymentUpdate.getFromPaymentDate() != null && searchPaymentUpdate.getToPaymentDate() != null) {
			predicates.add(cb.between(root.get("paymentDate"), searchPaymentUpdate.getFromPaymentDate(),
					searchPaymentUpdate.getToPaymentDate()));
		}

		if (searchPaymentUpdate.getFromPostingDate() != null && searchPaymentUpdate.getToPostingDate() != null) {
			predicates.add(cb.between(root.get("postingDate"), searchPaymentUpdate.getFromPostingDate(),
					searchPaymentUpdate.getToPostingDate()));
		}
		
		predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
		
		return cb.and(predicates.toArray(new Predicate[] {}));
	}
}
