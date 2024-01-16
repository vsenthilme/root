package com.tekclover.wms.api.transaction.repository.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.context.annotation.DeferredImportSelector.Group;
import org.springframework.data.jpa.domain.Specification;

import com.tekclover.wms.api.transaction.model.outbound.quality.QualityLine;
import com.tekclover.wms.api.transaction.model.outbound.quality.SearchQualityLine;

@SuppressWarnings("serial")
public class QualityLineSpecification implements Specification<QualityLine> {
	
	SearchQualityLine searchQualityLine;
	
	public QualityLineSpecification(SearchQualityLine inputSearchParams) {
		this.searchQualityLine = inputSearchParams;
	}
	 
	@Override
    public Predicate toPredicate(Root<QualityLine> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

         List<Predicate> predicates = new ArrayList<Predicate>();
		
		   
		 if (searchQualityLine.getWarehouseId() != null && !searchQualityLine.getWarehouseId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("warehouseId");
        	 predicates.add(group.in(searchQualityLine.getWarehouseId()));
         }

		if (searchQualityLine.getCompanyCodeId() != null && !searchQualityLine.getCompanyCodeId().isEmpty()) {
			final Path<Group> group = root.<Group> get("companyCodeId");
			predicates.add(group.in(searchQualityLine.getCompanyCodeId()));
		}

		if (searchQualityLine.getLanguageId() != null && !searchQualityLine.getLanguageId().isEmpty()) {
			final Path<Group> group = root.<Group> get("languageId");
			predicates.add(group.in(searchQualityLine.getLanguageId()));
		}

		if (searchQualityLine.getPlantId() != null && !searchQualityLine.getPlantId().isEmpty()) {
			final Path<Group> group = root.<Group> get("plantId");
			predicates.add(group.in(searchQualityLine.getPlantId()));
		}

		if (searchQualityLine.getPreOutboundNo() != null && !searchQualityLine.getPreOutboundNo().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("preOutboundNo");
        	 predicates.add(group.in(searchQualityLine.getPreOutboundNo()));
         }
		 
		 if (searchQualityLine.getRefDocNumber() != null && !searchQualityLine.getRefDocNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("refDocNumber");
        	 predicates.add(group.in(searchQualityLine.getRefDocNumber()));
         }
		 
		 if (searchQualityLine.getPartnerCode() != null && !searchQualityLine.getPartnerCode().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("partnerCode");
        	 predicates.add(group.in(searchQualityLine.getPartnerCode()));
         }
		  
		 if (searchQualityLine.getLineNumber() != null && !searchQualityLine.getLineNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("lineNumber");
        	 predicates.add(group.in(searchQualityLine.getLineNumber()));
         }
		 		 
         if (searchQualityLine.getQualityInspectionNo() != null && !searchQualityLine.getQualityInspectionNo().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("qualityInspectionNo");
        	 predicates.add(group.in(searchQualityLine.getQualityInspectionNo()));
         }
        
		 if (searchQualityLine.getItemCode() != null && !searchQualityLine.getItemCode().isEmpty()) {	
        	 final Path<Group> group = root.<Group> get("itemCode");
        	 predicates.add(group.in(searchQualityLine.getItemCode()));
         }

		if (searchQualityLine.getStatusId() != null && !searchQualityLine.getStatusId().isEmpty()) {
			final Path<Group> group = root.<Group> get("statusId");
			predicates.add(group.in(searchQualityLine.getStatusId()));
		}
		 
		 predicates.add(cb.equal(root.get("deletionIndicator"), 0L));

         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
