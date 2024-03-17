package com.mnrclara.api.management.repository.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.mnrclara.api.management.model.qb.QBSync;
import com.mnrclara.api.management.model.qb.SearchQbSync;

@SuppressWarnings("serial")
public class QbSyncSpecification implements Specification<QBSync> {
	
	SearchQbSync searchClientGeneral;
	
	public QbSyncSpecification (SearchQbSync inputSearchParams) {
		this.searchClientGeneral = inputSearchParams;
	}
	 
	@Override
	public Predicate toPredicate(Root<QBSync> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
	     List<Predicate> predicates = new ArrayList<Predicate>();
	
	     if (searchClientGeneral.getStartCreatedOn() != null && searchClientGeneral.getEndCreatedOn() != null) {
	         predicates.add(cb.between(root.get("createdOn"), searchClientGeneral.getStartCreatedOn(), searchClientGeneral.getEndCreatedOn()));
	     }
	     
	     if (searchClientGeneral.getObjectId() != null) {
	         predicates.add(cb.like(root.get("id"), "%" + searchClientGeneral.getObjectId() + "%" ));
	     }
	     
	     predicates.add(cb.equal(root.get("statusId"), 2L));
	     return cb.and(predicates.toArray(new Predicate[] {}));
	 }
}
