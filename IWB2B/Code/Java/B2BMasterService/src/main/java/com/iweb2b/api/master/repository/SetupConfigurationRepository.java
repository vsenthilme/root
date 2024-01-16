package com.iweb2b.api.master.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iweb2b.api.master.model.setupconfiguration.SetupConfiguration;

@Repository
public interface SetupConfigurationRepository extends JpaRepository<SetupConfiguration, Long>{

	public List<SetupConfiguration> findAll();

	public Optional<SetupConfiguration> findByPartnerIdAndDeletionIndicator(String partnerId, long l);
}