package com.iweb2b.api.master.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iweb2b.api.master.model.subscriptionmgmt.SubscriptionManagement;

@Repository
public interface SubscriptionManagementRepository extends JpaRepository<SubscriptionManagement, Long>{

	public List<SubscriptionManagement> findAll();

	public Optional<SubscriptionManagement> findByPartnerIdAndDeletionIndicator(String partnerId, long l);
}