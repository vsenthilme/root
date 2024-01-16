package com.iweb2b.api.portal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.iweb2b.api.portal.model.consignment.entity.ConsignmentWebhookEntity;

@Repository
@Transactional
public interface ConsignmentWebhookRepository extends JpaRepository<ConsignmentWebhookEntity, Long> {
	List<ConsignmentWebhookEntity> findByType(String type);
}