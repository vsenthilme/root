package com.mnrclara.api.setup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.notification.TimeTicketNotification;

@Repository
public interface TimeTicketNotificationRepository extends JpaRepository<TimeTicketNotification, Long> {
	
	public TimeTicketNotification findByTimeKeeperCode(String timeKeeperCode);
	public TimeTicketNotification findByTimeKeeperCodeAndWeekOfYear(String timeKeeperCode, Long weekNumber);
}