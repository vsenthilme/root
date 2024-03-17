package com.mnrclara.api.management.repository.specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.context.annotation.DeferredImportSelector.Group;
import org.springframework.data.jpa.domain.Specification;

import com.mnrclara.api.management.model.mattergeneral.MatterLNEReport;
import com.mnrclara.api.management.model.mattergeneral.SearchMatterLNENAssignmentReport;

@SuppressWarnings("serial")
public class MatterLNEReportSpecification implements Specification<MatterLNEReport> {
	
	SearchMatterLNENAssignmentReport searchMatterLNENAssignmentReport;
	
	public MatterLNEReportSpecification(SearchMatterLNENAssignmentReport inputSearchParams) {
		this.searchMatterLNENAssignmentReport = inputSearchParams;
	}
	 
	@Override
     public Predicate toPredicate(Root<MatterLNEReport> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

         List<Predicate> predicates = new ArrayList<Predicate>();
         
         if (searchMatterLNENAssignmentReport.getClassId() != null) {
        	 predicates.add(cb.equal(root.get("classId"), searchMatterLNENAssignmentReport.getClassId()));
         }
         
         if (searchMatterLNENAssignmentReport.getCaseCategoryId() != null && searchMatterLNENAssignmentReport.getCaseCategoryId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("caseCategoryId");
        	 predicates.add(group.in(searchMatterLNENAssignmentReport.getCaseCategoryId()));
         }
         
         if (searchMatterLNENAssignmentReport.getCaseSubCategoryId() != null && searchMatterLNENAssignmentReport.getCaseSubCategoryId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("caseSubCategoryId");
        	 predicates.add(group.in(searchMatterLNENAssignmentReport.getCaseSubCategoryId()));
         }

         if (searchMatterLNENAssignmentReport.getBillModeId() != null && !searchMatterLNENAssignmentReport.getBillModeId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("billModeText");
        	 predicates.add(group.in(searchMatterLNENAssignmentReport.getBillModeId()));
         }
         
         if (searchMatterLNENAssignmentReport.getFromCaseOpenDate() != null && searchMatterLNENAssignmentReport.getToCaseOpenDate() != null) {
             predicates.add(cb.between(root.get("matterOpenedDate"), 
            		 searchMatterLNENAssignmentReport.getFromCaseOpenDate(), searchMatterLNENAssignmentReport.getToCaseOpenDate()));
         }
         
         if (searchMatterLNENAssignmentReport.getFromCaseClosedDate() != null && searchMatterLNENAssignmentReport.getToCaseClosedDate() != null) {
             predicates.add(cb.between(root.get("matterClosedDate"), 
            		 searchMatterLNENAssignmentReport.getFromCaseClosedDate(), searchMatterLNENAssignmentReport.getToCaseClosedDate()));
         }
         
         if (searchMatterLNENAssignmentReport.getStatusId() != null && searchMatterLNENAssignmentReport.getStatusId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchMatterLNENAssignmentReport.getStatusId()));
         }
         
         if (searchMatterLNENAssignmentReport.getRefferedBy() != null && !searchMatterLNENAssignmentReport.getRefferedBy().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("referredBy");	//REF_FIELD_12
        	 predicates.add(group.in(searchMatterLNENAssignmentReport.getRefferedBy()));
         }
         
         /*
          *  matterNumber;
          *  originatingTimeKeeper;	// Case Sold by
          *  partner;
          *  responsibleTimeKeeper;	// Main Attorney
          *  assignedTimeKeeper;
          *  legalAssistant;
          *  lawClerks;
          */
         
         if (searchMatterLNENAssignmentReport.getMatterNumber() != null && !searchMatterLNENAssignmentReport.getMatterNumber().isEmpty()) {
        	 Path<Group> group = root.<Group> get("matterNumber");	
        	 predicates.add(group.in(searchMatterLNENAssignmentReport.getMatterNumber()));
         }
         
         if (searchMatterLNENAssignmentReport.getOriginatingTimeKeeper() != null && !searchMatterLNENAssignmentReport.getOriginatingTimeKeeper().isEmpty()) {
        	 Path<Group> group = root.<Group> get("originatingTimeKeeper");	
        	 predicates.add(group.in(searchMatterLNENAssignmentReport.getOriginatingTimeKeeper()));
         }
         
         if (searchMatterLNENAssignmentReport.getResponsibleTimeKeeper() != null && !searchMatterLNENAssignmentReport.getResponsibleTimeKeeper().isEmpty()) {
        	 Path<Group> group = root.<Group> get("responsibleTimeKeeper");	
        	 predicates.add(group.in(searchMatterLNENAssignmentReport.getResponsibleTimeKeeper()));
         }
         
         if (searchMatterLNENAssignmentReport.getAssignedTimeKeeper() != null && !searchMatterLNENAssignmentReport.getAssignedTimeKeeper().isEmpty()) {
        	 Path<Group> group = root.<Group> get("assignedTimeKeeper");	
        	 predicates.add(group.in(searchMatterLNENAssignmentReport.getAssignedTimeKeeper()));
         }
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
