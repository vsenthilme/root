package com.iweb2b.api.master.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iweb2b.api.master.model.analytics.DataTrafficAnalytics;

@Repository
public interface DataTrafficAnalyticsRepository extends JpaRepository<DataTrafficAnalytics, Long>{

	public List<DataTrafficAnalytics> findAll();

	public Optional<DataTrafficAnalytics> findByPartnerIdAndDeletionIndicator(String partnerId, long l);
}