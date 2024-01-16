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

import com.mnrclara.api.management.model.clientnote.ClientNote;
import com.mnrclara.api.management.model.clientnote.SearchClientNote;

@SuppressWarnings("serial")
public class ClientNoteSpecification implements Specification<ClientNote> {
	
	SearchClientNote searchClientNote;
	
	public ClientNoteSpecification (SearchClientNote inputSearchParams) {
		this.searchClientNote = inputSearchParams;
	}
	 
	@Override
	public Predicate toPredicate(Root<ClientNote> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
	     List<Predicate> predicates = new ArrayList<Predicate>();
	
	     if (searchClientNote.getNotesNumber() != null && !searchClientNote.getNotesNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("notesNumber");
        	 predicates.add(group.in(searchClientNote.getNotesNumber()));
         }
	     
	     if (searchClientNote.getNoteText() != null && !searchClientNote.getNoteText().isEmpty()) {
	         predicates.add(cb.like(root.get("noteText"), "%" + searchClientNote.getNoteText() + "%"));
	     }
	     
	     if (searchClientNote.getMatterNumber() != null && !searchClientNote.getMatterNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("matterNumber");
        	 predicates.add(group.in(searchClientNote.getMatterNumber()));
         }
	     
	     if (searchClientNote.getNoteTypeId() != null && searchClientNote.getNoteTypeId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("noteTypeId");
        	 predicates.add(group.in(searchClientNote.getNoteTypeId()));
         }
	     
	     if (searchClientNote.getStatusId() != null && searchClientNote.getStatusId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchClientNote.getStatusId()));
         }
	     
	     if (searchClientNote.getStartCreatedOn() != null && searchClientNote.getEndCreatedOn() != null) {
	         predicates.add(cb.between(root.get("createdOn"), searchClientNote.getStartCreatedOn(), searchClientNote.getEndCreatedOn()));
	     }
	     
	     if (searchClientNote.getDeletionIndicator().longValue() == 0) {
	         predicates.add(cb.equal(root.get("deletionIndicator"), searchClientNote.getDeletionIndicator()));
	     }

		 predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
	     return cb.and(predicates.toArray(new Predicate[] {}));
	 }
}
