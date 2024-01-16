package com.mnrclara.api.accounting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.accounting.model.invoice.InvoiceHeader;
import com.mnrclara.api.accounting.model.reports.ARReport;

@Repository
@Transactional
public interface ARReportRepository extends JpaRepository<ARReport, Long>, JpaSpecificationExecutor<ARReport> {

		
}