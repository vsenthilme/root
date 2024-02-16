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

import com.mnrclara.api.crm.model.agreement.Agreement;
import com.mnrclara.api.crm.model.agreement.SearchAgreement;

@SuppressWarnings("serial")
public class AgreementSpecification implements Specification<Agreement> {
	SearchAgreement searchAgreement;
		
	public AgreementSpecification (SearchAgreement inputSearchParams) {
		this.searchAgreement = inputSearchParams;
	}

	@Override
    public Predicate toPredicate(Root<Agreement> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (searchAgreement.getAgreementCode() != null && !searchAgreement.getAgreementCode().isEmpty()) {
			final Path<Group> group = root.<Group> get("agreementCode");
        	predicates.add(group.in(searchAgreement.getAgreementCode()));
        }
         
        if (searchAgreement.getPotentialClientId() != null && !searchAgreement.getPotentialClientId().isEmpty()) {
        	final Path<Group> group = root.<Group> get("potentialClientId");
        	predicates.add(group.in(searchAgreement.getPotentialClientId()));
        }

		if (searchAgreement.getInquiryNumber() != null && !searchAgreement.getInquiryNumber().isEmpty()) {
			final Path<Group> group = root.<Group> get("inquiryNumber");
        	predicates.add(group.in(searchAgreement.getInquiryNumber()));
        }
		
		if (searchAgreement.getCaseCategoryId() != null && searchAgreement.getCaseCategoryId().size() > 0) {
	       	final Path<Group> group = root.<Group> get("caseCategoryId");
	       	predicates.add(group.in(searchAgreement.getCaseCategoryId()));
        }
		
		if (searchAgreement.getStatusId() != null && searchAgreement.getStatusId().size() > 0) {
	       	final Path<Group> group = root.<Group> get("statusId");
	       	predicates.add(group.in(searchAgreement.getStatusId()));
        }
		
		if (searchAgreement.getSSentOn() != null && searchAgreement.getESentOn() != null) {
            predicates.add(cb.between(root.get("sentOn"), searchAgreement.getSSentOn(), searchAgreement.getESentOn()));
        }
		 
		if (searchAgreement.getSReceivedOn() != null && searchAgreement.getEReceivedOn() != null) {
            predicates.add(cb.between(root.get("receivedOn"), searchAgreement.getSReceivedOn(), searchAgreement.getEReceivedOn()));
        }
		 
		if (searchAgreement.getSResentOn() != null && searchAgreement.getEResentOn() != null) {
            predicates.add(cb.between(root.get("resentOn"), searchAgreement.getSResentOn(), searchAgreement.getEResentOn()));
        }
		
		if (searchAgreement.getSApprovedOn() != null && searchAgreement.getEApprovedOn() != null) {
            predicates.add(cb.between(root.get("approvedOn"), searchAgreement.getSApprovedOn(), searchAgreement.getEApprovedOn()));
        }
        return cb.and(predicates.toArray(new Predicate[] {}));
    }
}
