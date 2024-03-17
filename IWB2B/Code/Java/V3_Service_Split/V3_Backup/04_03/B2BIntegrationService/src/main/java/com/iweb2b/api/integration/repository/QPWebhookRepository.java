package com.iweb2b.api.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.iweb2b.api.integration.model.consignment.dto.qp.QPWebhook;
import com.iweb2b.api.integration.repository.fragments.StreamableJpaSpecificationRepository;

@Repository
@Transactional
public interface QPWebhookRepository extends JpaRepository<QPWebhook, Long>, StreamableJpaSpecificationRepository<QPWebhook> {

	public List<QPWebhook> findByTrackingNo(String trackingNo);
	
	public List<QPWebhook> findByTrackingNoAndItemActionName(String tracking_No, String item_Action_Name);
	
	@Query(value = "  SELECT TRACKING_NO\r\n"
			+ "  FROM tblqpwebhook\r\n"
			+ "  WHERE ITEM_ACTION_NAME <> 'CREATED' AND LMD_SYNCED_STATUS = 0 \r\n"
			+ "  GROUP BY TRACKING_NO\r\n"
			+ "  ORDER BY TRACKING_NO", nativeQuery = true)
	public List<String> findUniqueTrackingNo();
	
	public List<QPWebhook> findByTrackingNoAndItemActionNameNotOrderByActionTime(String trackingNo, String itemActionName);
	
	@Query(value = "SELECT TOP 1 TRACKING_NO FROM tblqpwebhook WHERE TRACKING_NO = :tracking_No AND ITEM_ACTION_NAME = :item_Action_Name \r\n"
			+ " ORDER BY ACTION_TIME DESC", nativeQuery = true)
	public QPWebhook findLatestByTrackingNoAndItemActionName(String tracking_No, String item_Action_Name);
	
	@Query(value = "SELECT TRACKING_NO FROM tblqpwebhook where TRACKING_NO IN :tracking_No AND ITEM_ACTION_NAME <> :item_Action_Name", nativeQuery = true)
	public List<String> findByActionName(List<String> tracking_No, String item_Action_Name);
}

