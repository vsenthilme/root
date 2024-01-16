package com.mnrclara.api.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.management.model.mattertimeticket.TimeTicketNotification;

@Repository
public interface TimeTicketNotificationRepository extends JpaRepository<TimeTicketNotification, Long> {

}