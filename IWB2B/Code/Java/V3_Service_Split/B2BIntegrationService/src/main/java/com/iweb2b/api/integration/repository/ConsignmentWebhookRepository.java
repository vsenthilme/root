package com.iweb2b.api.integration.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.iweb2b.api.integration.model.consignment.entity.ConsignmentWebhookEntity;

@Repository
@Transactional
public interface ConsignmentWebhookRepository extends JpaRepository<ConsignmentWebhookEntity, Long> {
	
	public List<ConsignmentWebhookEntity> findByType(String type);
	
	@Query(value = "SELECT *\r\n"
			+ "  FROM tblconsignmentwebhook\r\n"
			+ "  WHERE TYPE = :type AND EVENT_TIME BETWEEN :start_event_time AND :end_event_time AND CUSTOMER_CODE = :customerCode", 
			nativeQuery = true)
	public List<ConsignmentWebhookEntity> findByTypeAndEventTimeAndCustomerCode (
			@Param("type") String type, 
			@Param("start_event_time") Date start_event_time, 
			@Param("end_event_time") Date end_event_time,
			@Param("customerCode") String customerCode);
}