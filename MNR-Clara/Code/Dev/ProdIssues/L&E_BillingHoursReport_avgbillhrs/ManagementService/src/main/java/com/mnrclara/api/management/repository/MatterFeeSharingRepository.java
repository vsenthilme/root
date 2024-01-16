package com.mnrclara.api.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.management.model.matterfeesharing.MatterFeeSharing;


@Repository
public interface MatterFeeSharingRepository extends JpaRepository<MatterFeeSharing, Long>{

	public List<MatterFeeSharing> findAll();
	public Optional<MatterFeeSharing> findByMatterNumber (String matterNumber);
	public Optional<MatterFeeSharing> findByMatterNumberAndTimeKeeperCode (String matterNumber, String timeKeeperCode);
}