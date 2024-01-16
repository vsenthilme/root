package com.mnrclara.api.accounting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.accounting.model.management.entity.MatterAssignment;

@Repository
@Transactional
public interface MatterAssignmentRepository extends JpaRepository<MatterAssignment, Long>,JpaSpecificationExecutor<MatterAssignment> {

	public MatterAssignment findByMatterNumber(String matterNumber);
}