package com.mnrclara.api.accounting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.accounting.model.invoice.InvoiceHeader;
import com.mnrclara.api.accounting.model.invoice.report.ARAgingReport;

@Repository
@Transactional
public interface ARAgingReportRepository extends JpaRepository<ARAgingReport, Long>, JpaSpecificationExecutor<ARAgingReport> {

	
}