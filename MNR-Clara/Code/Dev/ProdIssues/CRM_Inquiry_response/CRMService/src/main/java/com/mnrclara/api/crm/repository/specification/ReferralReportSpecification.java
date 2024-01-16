package com.mnrclara.api.crm.repository.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.context.annotation.DeferredImportSelector.Group;
import org.springframework.data.jpa.domain.Specification;

import com.mnrclara.api.crm.model.potentialclient.PotentialClient;
import com.mnrclara.api.crm.model.potentialclient.SearchReferralReport;

@SuppressWarnings("serial")
public class ReferralReportSpecification implements Specification<PotentialClient> {
	SearchReferralReport searchPotentialClient;
	
	public ReferralReportSpecification (SearchReferralReport inputSearchParams) {
		this.searchPotentialClient = inputSearchParams;
	}
	
	/**
	 * CLASS_ID
	 * REFERRAL_ID
	 * POT_CLIENT_ID
	 * INQ_NO
	 * STATUS_ID
	 * REF_FIELD_3
	 * REF_FIELD4
	 * REF_FIELD2
	 * CTD_ON
	 * @return
	 */
	@Override
	public Predicate toPredicate(Root<PotentialClient> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
	     List<Predicate> predicates = new ArrayList<Predicate>();
	     
	     if (searchPotentialClient.getClassId() != null) {
             final Path<Group> group = root.<Group> get("classId");
        	 predicates.add(group.in(searchPotentialClient.getClassId()));
         }
	     
	     if (searchPotentialClient.getReferralId() != null && !searchPotentialClient.getReferralId().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("referralId");
        	 predicates.add(group.in(searchPotentialClient.getReferralId()));
         }
	     
	     if (searchPotentialClient.getFromCreatedOn() != null && searchPotentialClient.getToCreatedOn() != null) {
             predicates.add(cb.between(root.get("createdOn"), 
            		 searchPotentialClient.getFromCreatedOn(), searchPotentialClient.getToCreatedOn()));
         }

		predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
	     
	     return cb.and(predicates.toArray(new Predicate[] {}));
	}
}
