package com.mnrclara.api.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.management.model.crm.PotentialClient;

@Repository
@Transactional
public interface PotentialClientRepository extends JpaRepository<PotentialClient, Long>, JpaSpecificationExecutor<PotentialClient> {
	
	public PotentialClient findByPotentialClientId(@Param("potentialClientId") String potentialClientId);
	public List<PotentialClient> findByClassId(Long classId);
	public List<PotentialClient> findByInquiryNumberAndDeletionIndicator(String inquiryNumber, Long deletionIndicator);
}
