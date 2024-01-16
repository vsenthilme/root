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

import com.tekclover.wms.api.transaction.model.cyclecount.periodic.PeriodicHeader;
import com.tekclover.wms.api.transaction.model.cyclecount.periodic.SearchPeriodicHeader;

@SuppressWarnings("serial")
public class PeriodicHeaderSpecification implements Specification<PeriodicHeader> {
	
	SearchPeriodicHeader searchPeriodicHeader;
	
	public PeriodicHeaderSpecification(SearchPeriodicHeader inputSearchParams) {
		this.searchPeriodicHeader = inputSearchParams;
	}
	 
	@Override
    public Predicate toPredicate(Root<PeriodicHeader> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

         List<Predicate> predicates = new ArrayList<Predicate>();
		
		 if (searchPeriodicHeader.getWarehouseId() != null && !searchPeriodicHeader.getWarehouseId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("warehouseId");
        	 predicates.add(group.in(searchPeriodicHeader.getWarehouseId()));
         }
		if (searchPeriodicHeader.getCompanyCodeId() != null && !searchPeriodicHeader.getCompanyCodeId().isEmpty()) {
			final Path<Group> group = root.<Group> get("companyCodeId");
			predicates.add(group.in(searchPeriodicHeader.getCompanyCodeId()));
		}
		if (searchPeriodicHeader.getLanguageId() != null && !searchPeriodicHeader.getLanguageId().isEmpty()) {
			final Path<Group> group = root.<Group> get("languageId");
			predicates.add(group.in(searchPeriodicHeader.getLanguageId()));
		}
		if (searchPeriodicHeader.getPlantId() != null && !searchPeriodicHeader.getPlantId().isEmpty()) {
			final Path<Group> group = root.<Group> get("plantId");
			predicates.add(group.in(searchPeriodicHeader.getPlantId()));
		}
		 
		 if (searchPeriodicHeader.getCycleCountTypeId() != null && !searchPeriodicHeader.getCycleCountTypeId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("cycleCountTypeId");
        	 predicates.add(group.in(searchPeriodicHeader.getCycleCountTypeId()));
         }
		 
         if (searchPeriodicHeader.getCycleCountNo() != null && !searchPeriodicHeader.getCycleCountNo().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("cycleCountNo");
        	 predicates.add(group.in(searchPeriodicHeader.getCycleCountNo()));
         }
		 
		 if (searchPeriodicHeader.getHeaderStatusId() != null && !searchPeriodicHeader.getHeaderStatusId().isEmpty()) {	
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchPeriodicHeader.getHeaderStatusId()));
         }	
		 
		 if (searchPeriodicHeader.getCreatedBy() != null && !searchPeriodicHeader.getCreatedBy().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("createdBy");
        	 predicates.add(group.in(searchPeriodicHeader.getCreatedBy()));
         }   		 
	
		  if (searchPeriodicHeader.getStartCreatedOn() != null && searchPeriodicHeader.getEndCreatedOn() != null) {
        	 predicates.add(cb.between(root.get("createdOn"), searchPeriodicHeader.getStartCreatedOn(), searchPeriodicHeader.getEndCreatedOn()));
         }
			
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
