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

import com.mnrclara.api.management.model.mattertimeticket.MatterTimeTicket;
import com.mnrclara.api.management.model.mattertimeticket.SearchMatterTimeTicket;

@SuppressWarnings("serial")
public class MatterTimeTicketSpecification implements Specification<MatterTimeTicket> {
	
	SearchMatterTimeTicket searchMatterTimeTicket;
	
	public MatterTimeTicketSpecification (SearchMatterTimeTicket inputSearchParams) {
		this.searchMatterTimeTicket = inputSearchParams;
	}
	 
	@Override
    public Predicate toPredicate(Root<MatterTimeTicket> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
         List<Predicate> predicates = new ArrayList<Predicate>();

         if (searchMatterTimeTicket.getTimeTicketNumber() != null && !searchMatterTimeTicket.getTimeTicketNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("timeTicketNumber");
        	 predicates.add(group.in(searchMatterTimeTicket.getTimeTicketNumber()));
         }
         
         if (searchMatterTimeTicket.getBillType() != null && !searchMatterTimeTicket.getBillType().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("billType");
        	 predicates.add(group.in(searchMatterTimeTicket.getBillType()));
         }
         
         if (searchMatterTimeTicket.getTimeKeeperCode() != null && !searchMatterTimeTicket.getTimeKeeperCode().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("timeKeeperCode");
        	 predicates.add(group.in(searchMatterTimeTicket.getTimeKeeperCode()));
         }
         
         if (searchMatterTimeTicket.getMatterNumber() != null && !searchMatterTimeTicket.getMatterNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("matterNumber");
        	 predicates.add(group.in(searchMatterTimeTicket.getMatterNumber()));
         }
         
         if (searchMatterTimeTicket.getStatusId() != null && searchMatterTimeTicket.getStatusId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchMatterTimeTicket.getStatusId()));
         }
         
         if (searchMatterTimeTicket.getStartCreatedOn() != null && searchMatterTimeTicket.getEndCreatedOn() != null) {
             predicates.add(cb.between(root.get("createdOn"), searchMatterTimeTicket.getStartCreatedOn(), searchMatterTimeTicket.getEndCreatedOn()));
         }
         
         if (searchMatterTimeTicket.getStartTimeTicketDate() != null && searchMatterTimeTicket.getEndTimeTicketDate() != null) {
             predicates.add(cb.between(root.get("timeTicketDate"), 
            		 searchMatterTimeTicket.getStartTimeTicketDate(), searchMatterTimeTicket.getEndTimeTicketDate()));
         }

         predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
         return cb.and(predicates.toArray(new Predicate[] {}));
     }
}
