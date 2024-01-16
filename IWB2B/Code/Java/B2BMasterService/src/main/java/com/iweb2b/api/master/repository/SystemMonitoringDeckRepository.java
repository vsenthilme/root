package com.iweb2b.api.master.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iweb2b.api.master.model.monitoring.SystemMonitoringDeck;

@Repository
public interface SystemMonitoringDeckRepository extends JpaRepository<SystemMonitoringDeck, Long>{

	public List<SystemMonitoringDeck> findAll();

	public Optional<SystemMonitoringDeck> findByPartnerIdAndDeletionIndicator(String systemMonitoringDeckId, long l);
}