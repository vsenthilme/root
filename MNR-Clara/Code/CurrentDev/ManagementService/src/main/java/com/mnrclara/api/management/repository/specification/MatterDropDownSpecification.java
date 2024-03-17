package com.mnrclara.api.management.repository.specification;

import com.mnrclara.api.management.model.dto.MatterDropDown;
import com.mnrclara.api.management.model.dto.MatterNumberDropDown;
import com.mnrclara.api.management.model.mattergeneral.MatterGenAcc;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
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
