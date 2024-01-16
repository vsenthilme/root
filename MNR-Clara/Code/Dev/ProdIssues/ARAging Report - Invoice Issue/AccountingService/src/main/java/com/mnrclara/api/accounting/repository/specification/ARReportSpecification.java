package com.mnrclara.api.accounting.repository.specification;

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

import com.mnrclara.api.accounting.model.invoice.report.ARAgingReport;
import com.mnrclara.api.accounting.model.reports.ARReport;
import com.mnrclara.api.accounting.model.reports.SearchAR;

@SuppressWarnings("serial")
public class ARReportSpecification implements Specification<ARReport> {
	
	SearchAR searchARReport;
	
	/**
	 * 
	 * @param inputSearchParams
	 */
	public ARReportSpecification(SearchAR inputSearchParams) {
		this.searchARReport = inputSearchParams;
	}
	
	/*
	 * private List<Long> classId;
	private List<Long> caseCategory;
	private List<Long> caseSubCategory;
	private List<String> timeKeeper;
	private Date fromDate;
	private Date toDate;
	private Boolean includeClosedMatter = false;
	private List<String> clientId;
	
	 */
	 
	@Override
    public Predicate toPredicate(Root<ARReport> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
         List<Predicate> predicates = new ArrayList<Predicate>();

         if (searchARReport.getClassId() != null && !searchARReport.getClassId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("classId");
        	 predicates.add(group.in(searchARReport.getClassId()));
         }
         
         if (searchARReport.getClientId() != null && !searchARReport.getClientId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("clientId");
        	 predicates.add(group.in(searchARReport.getClientId()));
         }
         
         if (searchARReport.getCaseCategory() != null && !searchARReport.getCaseCategory().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("caseCategory");
        	 predicates.add(group.in(searchARReport.getCaseCategory()));
         }
		 
		 if (searchARReport.getCaseSubCategory() != null&& !searchARReport.getCaseSubCategory().isEmpty()) {
			 final Path<Group> group = root.<Group> get("caseSubCategory");
			 predicates.add(group.in(searchARReport.getCaseSubCategory()));
         }
		 
		 if (searchARReport.getClientId() != null&& !searchARReport.getClientId().isEmpty()) {
			 final Path<Group> group = root.<Group> get("clientId");
			 predicates.add(group.in(searchARReport.getClientId()));
         }
		 
		 if (searchARReport.getMatterNumber() != null&& !searchARReport.getMatterNumber().isEmpty()) {
			 final Path<Group> group = root.<Group> get("matterNumber");
			 predicates.add(group.in(searchARReport.getMatterNumber()));
         }
		 
		 predicates.add(
				 cb.or(
						 cb.between(root.get("postingDate"), searchARReport.getFromDate(), searchARReport.getToDate()),
						 cb.between(root.get("lastPaymentDate"), searchARReport.getFromDate(), searchARReport.getToDate())
				 ));
		 return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
