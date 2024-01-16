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

import com.mnrclara.api.management.model.matterdocument.MatterDocument;
import com.mnrclara.api.management.model.matterdocument.SearchMatterDocument;

@SuppressWarnings("serial")
public class MatterDocumentSpecification implements Specification<MatterDocument> {
	
	SearchMatterDocument searchMatterDocument;
	
	public MatterDocumentSpecification(SearchMatterDocument inputSearchParams) {
		this.searchMatterDocument = inputSearchParams;
	}
	 
	@Override
     public Predicate toPredicate(Root<MatterDocument> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

         List<Predicate> predicates = new ArrayList<Predicate>();

         if (searchMatterDocument.getDocumentNo() != null && !searchMatterDocument.getDocumentNo().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("documentNo");
        	 predicates.add(group.in(searchMatterDocument.getDocumentNo()));
         }
         
         if (searchMatterDocument.getSentBy() != null && !searchMatterDocument.getSentBy().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("sentBy");
        	 predicates.add(group.in(searchMatterDocument.getSentBy()));
         }
         
         if (searchMatterDocument.getStatusId() != null && searchMatterDocument.getStatusId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchMatterDocument.getStatusId()));
         }
        if (searchMatterDocument.getClassId() != null && searchMatterDocument.getClassId().size() > 0) {
            final Path<Group> group = root.<Group> get("classId");
            predicates.add(group.in(searchMatterDocument.getClassId()));
        }
        if (searchMatterDocument.getClientId() != null && searchMatterDocument.getClientId().size() > 0) {
            final Path<Group> group = root.<Group> get("clientId");
            predicates.add(group.in(searchMatterDocument.getClientId()));
        }
        if (searchMatterDocument.getMatterNumber() != null && searchMatterDocument.getMatterNumber().size() > 0) {
            final Path<Group> group = root.<Group> get("matterNumber");
            predicates.add(group.in(searchMatterDocument.getMatterNumber()));
        }
         
         if (searchMatterDocument.getSSentOn() != null && searchMatterDocument.getESentOn() != null) {
             predicates.add(cb.between(root.get("sentOn"), searchMatterDocument.getSSentOn(), searchMatterDocument.getESentOn()));
         }
         
         if (searchMatterDocument.getSReceivedOn() != null && searchMatterDocument.getEReceivedOn() != null) {
             predicates.add(cb.between(root.get("receivedOn"), searchMatterDocument.getSReceivedOn(), searchMatterDocument.getEReceivedOn()));
         }

         predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
