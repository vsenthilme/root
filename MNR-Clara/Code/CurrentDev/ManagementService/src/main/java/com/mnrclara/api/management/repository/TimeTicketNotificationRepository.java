package com.mnrclara.api.management.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.management.model.mattertimeticket.TimeTicketNotification;

@Repository
public interface TimeTicketNotificationRepository extends JpaRepository<TimeTicketNotification, Long>, JpaSpecificationExecutor<TimeTicketNotification> {

	@Transactional
	@Modifying
	@Query("DELETE FROM TimeTicketNotification tn WHERE tn.weekOfYear = :weekOfYear")
	public void deleteTTNotification(@Param("weekOfYear") long weekOfYear);
}