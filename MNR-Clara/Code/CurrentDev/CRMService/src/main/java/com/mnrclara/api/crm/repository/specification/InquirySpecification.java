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

import com.mnrclara.api.crm.model.inquiry.Inquiry;
import com.mnrclara.api.crm.model.inquiry.SearchInquiry;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
public class InquirySpecification implements Specification<Inquiry> {
	SearchInquiry searchInquiry;
	
	public InquirySpecification (SearchInquiry inputSearchParams) {
		this.searchInquiry = inputSearchParams;
	}
	
	/**
	 * Inquiry Number
	 * Inquiry Date
	 * Inquiry Mode
	 * First Name
	 * Last Name
	 * Email ID
	 * Phone
	 * @param searchInquiry
	 * @return
	 */
	@Override
    public Predicate toPredicate(Root<Inquiry> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
         List<Predicate> predicates = new ArrayList<Predicate>();

         if (searchInquiry.getInquiryNumber() != null && !searchInquiry.getInquiryNumber().isEmpty()) {
        	 final Path<Group> group = root.<Group> get("inquiryNumber");
        	 predicates.add(group.in(searchInquiry.getInquiryNumber()));
         }
         
         if (searchInquiry.getInqStartDate() != null && searchInquiry.getInqEndDate() != null) {
             predicates.add(cb.between(root.get("inquiryDate"), searchInquiry.getInqStartDate(), searchInquiry.getInqEndDate()));
         }
         
         if (searchInquiry.getInquiryModeId() != null && searchInquiry.getInquiryModeId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("inquiryModeId");
        	 predicates.add(group.in(searchInquiry.getInquiryModeId()));
         }
         
         if (searchInquiry.getFirstName() != null && !searchInquiry.getFirstName().isEmpty()) {
             predicates.add(cb.like(root.get("firstName"), "%" + searchInquiry.getFirstName() + "%"));
         }
         
         if (searchInquiry.getLastName() != null && !searchInquiry.getLastName().isEmpty()) {
             predicates.add(cb.like(root.get("lastName"), "%" + searchInquiry.getLastName() + "%"));
         }
         
         if (searchInquiry.getEmail() != null && !searchInquiry.getEmail().isEmpty()) {
             predicates.add(cb.like(root.get("email"), "%" + searchInquiry.getEmail() + "%"));
         }
         
         if (searchInquiry.getContactNumber() != null && !searchInquiry.getContactNumber().isEmpty()) {
             predicates.add(cb.like(root.get("contactNumber"), "%" + searchInquiry.getContactNumber() + "%"));
         }
         
         if (searchInquiry.getStatusId() != null && searchInquiry.getStatusId().size() > 0) {
        	 final Path<Group> group = root.<Group> get("statusId");
        	 predicates.add(group.in(searchInquiry.getStatusId()));
         }
         
         if (searchInquiry.getClassId() != null && searchInquiry.getClassId().size() > 0) {
             final Path<Group> group = root.<Group> get("classId");
        	 predicates.add(group.in(searchInquiry.getClassId()));
         }
         
         if (searchInquiry.getAssignedUserId() != null && !searchInquiry.getAssignedUserId().isEmpty()) {
             final Path<Group> group = root.<Group> get("assignedUserId");
        	 predicates.add(group.in(searchInquiry.getAssignedUserId()));
         }
         
         if (searchInquiry.getSAssignedOn() != null && searchInquiry.getEAssignedOn() != null) {
             predicates.add(cb.between(root.get("assignedOn"), searchInquiry.getSAssignedOn(), searchInquiry.getEAssignedOn()));
         }
         
         return cb.and(predicates.toArray(new Predicate[] {}));
    }
}
