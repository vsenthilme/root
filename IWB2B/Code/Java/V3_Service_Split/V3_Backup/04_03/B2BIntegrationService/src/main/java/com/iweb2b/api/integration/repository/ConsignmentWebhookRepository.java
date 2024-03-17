package com.iweb2b.api.integration.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.iweb2b.api.integration.model.consignment.entity.ConsignmentWebhookEntity;

@Repository
@Transactional
public interface ConsignmentWebhookRepository extends JpaRepository<ConsignmentWebhookEntity, Long> {
	
	public List<ConsignmentWebhookEntity> findByType(String type);
	public List<ConsignmentWebhookEntity> findByEvent_timeBetween(Date start_event_time, Date end_event_time);
}