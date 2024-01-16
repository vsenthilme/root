package com.iweb2b.api.master.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iweb2b.api.master.model.healthcheck.HealthCheck;

@Repository
public interface HealthCheckRepository extends JpaRepository<HealthCheck, Long>{

	public List<HealthCheck> findAll();
	public Optional<HealthCheck> findByPartnerIdAndDeletionIndicator(String healthCheckId, long l);
}