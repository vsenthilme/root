package com.mnrclara.api.management.repository.specification;

import com.mnrclara.api.management.model.dto.MatterNumberDropDown;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class MatterDropDownSpecification implements Specification<MatterNumberDropDown> {


	public MatterDropDownSpecification() {
	}
	 
	@Override
     public Predicate toPredicate(Root<MatterNumberDropDown> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

         List<Predicate> predicates = new ArrayList<Predicate>();


         predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
         
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
