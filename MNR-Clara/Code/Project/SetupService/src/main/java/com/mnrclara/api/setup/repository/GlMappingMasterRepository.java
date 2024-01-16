package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.setup.model.glmappingmaster.GlMappingMaster;

@Repository
@Transactional
public interface GlMappingMasterRepository extends JpaRepository<GlMappingMaster,Long>, JpaSpecificationExecutor<GlMappingMaster> {
	
	public List<GlMappingMaster> findAll();
	public Optional<GlMappingMaster> findByItemNumberAndDeletionIndicator(Long itemNumber, long l);
}