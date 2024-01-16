package com.mnrclara.api.management.repository.specification;

import com.mnrclara.api.management.model.mattertimeticket.FindTimeTicketNotification;
import com.mnrclara.api.management.model.mattertimeticket.TimeTicketNotification;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public class TimeTicketNotificationSpecification implements Specification<TimeTicketNotification> {

    FindTimeTicketNotification findTimeTicketNotification;

    public TimeTicketNotificationSpecification(FindTimeTicketNotification inputSearchParams) {
        this.findTimeTicketNotification = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<TimeTicketNotification> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<Predicate>();

        if (findTimeTicketNotification.getTimeKeeperCode() != null && !findTimeTicketNotification.getTimeKeeperCode().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("timeKeeperCode");
            predicates.add(group.in(findTimeTicketNotification.getTimeKeeperCode()));
        }

        if (findTimeTicketNotification.getWeekOfYear() != null && !findTimeTicketNotification.getWeekOfYear().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("weekOfYear");
            predicates.add(group.in(findTimeTicketNotification.getWeekOfYear()));
        }

        if (findTimeTicketNotification.getClassId() != null && !findTimeTicketNotification.getClassId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("classId");
            predicates.add(group.in(findTimeTicketNotification.getClassId()));
        }
        
        if (findTimeTicketNotification.getYear() != null && findTimeTicketNotification.getYear() != 0) {
        	predicates.add(cb.equal(root.get("year"), findTimeTicketNotification.getYear()));
        }

        return cb.and(predicates.toArray(new Predicate[]{}));
    }

}
