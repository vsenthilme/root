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

import com.mnrclara.api.management.model.matteritform.MatterITForm;
import com.mnrclara.api.management.model.matteritform.SearchMatterITForm;

@SuppressWarnings("serial")
public class MatterITFormSpecification implements Specification<MatterITForm> {
	
	SearchMatterITForm searchMatterITForm;
	
	public MatterITFormSpecification(SearchMatterITForm inputSearchParams) {
		this.searchMatterITForm = inputSearchParams;
	}
	 
	@Override
     public Predicate toPredicate(Root<MatterITForm> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

         List<Predicate> predicates = new ArrayList<Predicate>();

         if (searchMatterITForm.getIntakeFormNumber() != null && !searchMatterITForm.getIntakeFormNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("intakeFormNumber");
        	 predicates.add(group.in(searchMatterITForm.getIntakeFormNumber()));
         }
         
         if (searchMatterITForm.getIntakeFormId() != null && !searchMatterITForm.getIntakeFormId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("intakeFormId");
        	 predicates.add(group.in(searchMatterITForm.getIntakeFormId()));
         }
         
         if (searchMatterITForm.getMatterNumber() != null && !searchMatterITForm.getMatterNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("matterNumber");
        	 predicates.add(group.in(searchMatterITForm.getMatterNumber()));
         }
         
         if (searchMatterITForm.getStatusId() != null && searchMatterITForm.getStatusId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchMatterITForm.getStatusId()));
         }
         
         if (searchMatterITForm.getSSentOn() != null && searchMatterITForm.getESentOn() != null) {
             predicates.add(cb.between(root.get("sentOn"), searchMatterITForm.getSSentOn(), searchMatterITForm.getESentOn()));
         }
         
         if (searchMatterITForm.getSReceivedOn() != null && searchMatterITForm.getEReceivedOn() != null) {
             predicates.add(cb.between(root.get("receivedOn"), searchMatterITForm.getSReceivedOn(), searchMatterITForm.getEReceivedOn()));
         }
         
         if (searchMatterITForm.getSApprovedOn() != null && searchMatterITForm.getEApprovedOn() != null) {
             predicates.add(cb.between(root.get("approvedOn"), searchMatterITForm.getSApprovedOn(), searchMatterITForm.getEApprovedOn()));
         }
        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
