package com.iweb2b.api.master.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iweb2b.api.master.model.partnermaster.PartnerMaster;

@Repository
public interface PartnerMasterRepository extends JpaRepository<PartnerMaster, Long>{

	public List<PartnerMaster> findAll();

	public Optional<PartnerMaster> findByPartnerIdAndDeletionIndicator(String partnerMasterId, long l);
}