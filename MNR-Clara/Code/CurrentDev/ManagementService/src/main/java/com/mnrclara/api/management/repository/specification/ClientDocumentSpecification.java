package com.mnrclara.api.management.repository.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.context.annotation.DeferredImportSelector.Group;
import org.springframework.data.jpa.domain.Specification;

import com.mnrclara.api.management.model.clientdocument.ClientDocument;
import com.mnrclara.api.management.model.clientdocument.SearchClientDocument;

@SuppressWarnings("serial")
public class ClientDocumentSpecification implements Specification<ClientDocument> {
	
	SearchClientDocument searchClientDocument;
	
	public ClientDocumentSpecification (SearchClientDocument inputSearchParams) {
		this.searchClientDocument = inputSearchParams;
	}
	 
	@Override
     public Predicate toPredicate(Root<ClientDocument> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
         List<Predicate> predicates = new ArrayList<Predicate>();

         if (searchClientDocument.getDocumentNo() != null && !searchClientDocument.getDocumentNo().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("documentNo");
        	 predicates.add(group.in(searchClientDocument.getDocumentNo()));
         }
         
         if (searchClientDocument.getSentBy() != null && !searchClientDocument.getSentBy().isEmpty()) {
             predicates.add(cb.like(root.get("sentBy"), "%" + searchClientDocument.getSentBy() + "%"));
         }
         
         if (searchClientDocument.getMatterNumber() != null && !searchClientDocument.getMatterNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("matterNumber");
        	 predicates.add(group.in(searchClientDocument.getMatterNumber()));
         }
         
         if (searchClientDocument.getStatusId() != null && searchClientDocument.getStatusId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchClientDocument.getStatusId()));
         }
         
         if (searchClientDocument.getSSentOn() != null && searchClientDocument.getESentOn() != null) {
             predicates.add(cb.between(root.get("sentOn"), searchClientDocument.getSSentOn(), searchClientDocument.getESentOn()));
         }
         
         if (searchClientDocument.getSReceivedOn() != null && searchClientDocument.getEReceivedOn() != null) {
             predicates.add(cb.between(root.get("receivedOn"), searchClientDocument.getSReceivedOn(), searchClientDocument.getEReceivedOn()));
         }
         predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
