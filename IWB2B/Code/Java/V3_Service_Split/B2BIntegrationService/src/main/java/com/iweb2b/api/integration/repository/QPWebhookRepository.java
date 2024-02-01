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

	public QPWebhook findByTrackingNoAndItemActionName(String tracking_No, String item_Action_Name);
	
	@Query(value = "SELECT TRACKING_NO FROM tblqpwebhook where TRACKING_NO IN :tracking_No AND ITEM_ACTION_NAME <> :item_Action_Name", nativeQuery = true)
	public List<String> findByActionName(List<String> tracking_No, String item_Action_Name);
}

