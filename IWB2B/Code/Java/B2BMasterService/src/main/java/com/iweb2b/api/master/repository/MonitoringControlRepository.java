package com.iweb2b.api.master.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iweb2b.api.master.model.monitoring.MonitoringControl;

@Repository
public interface MonitoringControlRepository extends JpaRepository<MonitoringControl, Long>{

	public List<MonitoringControl> findAll();

	public Optional<MonitoringControl> findByPartnerIdAndDeletionIndicator(String monitoringControlId, long l);
}