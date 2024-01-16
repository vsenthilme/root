package com.iweb2b.api.master.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iweb2b.api.master.model.notification.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>{

	public List<Notification> findAll();

	public Optional<Notification> findByPartnerIdAndDeletionIndicator(String partnerId, long l);
}