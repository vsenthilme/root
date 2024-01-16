package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.report.TransactionHistoryReport;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TransactionHistoryReportRepository extends JpaRepository<TransactionHistoryReport, Long>,
        JpaSpecificationExecutor<TransactionHistoryReport>,
        StreamableJpaSpecificationRepository<TransactionHistoryReport> {



}