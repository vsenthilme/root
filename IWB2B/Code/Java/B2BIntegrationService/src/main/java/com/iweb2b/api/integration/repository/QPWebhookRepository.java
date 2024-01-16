package com.iweb2b.api.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.iweb2b.api.integration.model.consignment.dto.qp.QPWebhook;
import com.iweb2b.api.integration.repository.fragments.StreamableJpaSpecificationRepository;

@Repository
@Transactional
public interface QPWebhookRepository extends JpaRepository<QPWebhook, Long>, StreamableJpaSpecificationRepository<QPWebhook> {

	public QPWebhook findByTrackingNoAndItemActionName(String tracking_No, String item_Action_Name);
}

