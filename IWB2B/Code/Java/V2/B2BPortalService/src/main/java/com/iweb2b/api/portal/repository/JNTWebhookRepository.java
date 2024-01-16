package com.iweb2b.api.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iweb2b.api.portal.model.consignment.entity.JNTWebhookEntity;

@Repository
public interface JNTWebhookRepository extends JpaRepository<JNTWebhookEntity, Long> {

}