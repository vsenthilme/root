package com.mnrclara.api.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.management.model.matterrate.MatterRate;


@Repository
public interface MatterRateRepository extends JpaRepository<MatterRate, Long>{

	public List<MatterRate> findAll();
	
	public Optional<MatterRate> findByMatterNumber (String matterNumber);
	
	public Optional<MatterRate> findByMatterNumberAndTimeKeeperCode (String matterNumber, String timeKeeperCode);
	
	public List<MatterRate> findByMatterNumberAndDeletionIndicator(String matterNumber, long l);
}