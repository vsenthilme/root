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

import com.mnrclara.api.management.model.mattertask.MatterTask;
import com.mnrclara.api.management.model.mattertask.SearchMatterTask;

@SuppressWarnings("serial")
public class MatterTaskSpecification implements Specification<MatterTask> {
	
	SearchMatterTask searchMatterTask;
	
	public MatterTaskSpecification (SearchMatterTask inputSearchParams) {
		this.searchMatterTask = inputSearchParams;
	}
	 
	@Override
     public Predicate toPredicate(Root<MatterTask> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
         List<Predicate> predicates = new ArrayList<Predicate>();

         if (searchMatterTask.getTaskNumber() != null && !searchMatterTask.getTaskNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("taskNumber");
        	 predicates.add(group.in(searchMatterTask.getTaskNumber()));
         }
         
         if (searchMatterTask.getTaskTypeCode() != null && !searchMatterTask.getTaskTypeCode().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("taskTypeCode");
        	 predicates.add(group.in(searchMatterTask.getTaskTypeCode()));
         }
        if (searchMatterTask.getMatterNumber() != null && !searchMatterTask.getMatterNumber().isEmpty()) {
            final Path<Group> group = root.<Group> get("matterNumber");
            predicates.add(group.in(searchMatterTask.getMatterNumber()));
        }
        if (searchMatterTask.getClassId() != null && !searchMatterTask.getClassId().isEmpty()) {
            final Path<Group> group = root.<Group> get("classId");
            predicates.add(group.in(searchMatterTask.getClassId()));
        }
        if (searchMatterTask.getCreatedBy() != null && !searchMatterTask.getCreatedBy().isEmpty()) {
            final Path<Group> group = root.<Group> get("createdBy");
            predicates.add(group.in(searchMatterTask.getCreatedBy()));
        }
         
         if (searchMatterTask.getTaskName() != null && !searchMatterTask.getTaskName().isEmpty()) {
             predicates.add(cb.like(root.get("taskName"), "%" + searchMatterTask.getTaskName() + "%"));
         }
         
         if (searchMatterTask.getTaskAssignedTo() != null && !searchMatterTask.getTaskAssignedTo().isEmpty()) {
             predicates.add(cb.like(root.get("taskAssignedTo"), "%" + searchMatterTask.getTaskAssignedTo() + "%"));
         }
         
         if (searchMatterTask.getStatusId() != null && searchMatterTask.getStatusId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchMatterTask.getStatusId()));
         }
         
         if (searchMatterTask.getSCreatedOn() != null && searchMatterTask.getECreatedOn() != null) {
             predicates.add(cb.between(root.get("createdOn"), searchMatterTask.getSCreatedOn(), searchMatterTask.getECreatedOn()));
         }
         
         if (searchMatterTask.getSDeadlineDate() != null && searchMatterTask.getEDeadlineDate() != null) {
             predicates.add(cb.between(root.get("deadlineDate"), searchMatterTask.getSDeadlineDate(), searchMatterTask.getEDeadlineDate()));
         }
         
         if (searchMatterTask.getSReminderDate() != null && searchMatterTask.getEReminderDate() != null) {
             predicates.add(cb.between(root.get("reminderDate"), searchMatterTask.getSReminderDate(), searchMatterTask.getEReminderDate()));
         }

         predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
