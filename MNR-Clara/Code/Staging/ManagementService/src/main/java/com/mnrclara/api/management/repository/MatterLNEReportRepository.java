package com.mnrclara.api.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.management.model.mattergeneral.MatterLNEReport;

@Repository
@Transactional
public interface MatterLNEReportRepository extends JpaRepository<MatterLNEReport,Long>,JpaSpecificationExecutor<MatterLNEReport> {

}