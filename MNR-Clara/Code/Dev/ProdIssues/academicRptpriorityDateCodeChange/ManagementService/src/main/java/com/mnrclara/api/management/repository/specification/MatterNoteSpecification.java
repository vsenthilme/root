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

import com.mnrclara.api.management.model.matternote.MatterNote;
import com.mnrclara.api.management.model.matternote.SearchMatterNote;

@SuppressWarnings("serial")
public class MatterNoteSpecification implements Specification<MatterNote> {
	
	SearchMatterNote searchMatterNote;
	
	public MatterNoteSpecification(SearchMatterNote inputSearchParams) {
		this.searchMatterNote = inputSearchParams;
	}
	 
	@Override
     public Predicate toPredicate(Root<MatterNote> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

         List<Predicate> predicates = new ArrayList<Predicate>();

         if (searchMatterNote.getNotesNumber() != null && !searchMatterNote.getNotesNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("notesNumber");
        	 predicates.add(group.in(searchMatterNote.getNotesNumber()));
         }
         
         if (searchMatterNote.getNoteTypeId() != null && searchMatterNote.getNoteTypeId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("noteTypeId");
        	 predicates.add(group.in(searchMatterNote.getNoteTypeId()));
         }
         
         if (searchMatterNote.getStatusId() != null && searchMatterNote.getStatusId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchMatterNote.getStatusId()));
         }
         
         if (searchMatterNote.getStartCreatedOn() != null && searchMatterNote.getEndCreatedOn() != null) {
             predicates.add(cb.between(root.get("createdOn"), searchMatterNote.getStartCreatedOn(), searchMatterNote.getEndCreatedOn()));
         }
         predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
