package com.mnrclara.api.management.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.management.model.dto.InvoiceHeader;

@Repository
@Transactional
public interface InvoiceHeaderRepository
		extends JpaRepository<InvoiceHeader, String>, JpaSpecificationExecutor<InvoiceHeader> {

	@Query(value = "SELECT SUM(INVOICE_AMT) AS invoiceAmount FROM tblinvoiceheader\r\n"
			+ "WHERE MATTER_NO = :matterNumber AND CLIENT_ID = :clientId\r\n"
			+ "GROUP BY MATTER_NO", nativeQuery = true)
	public Double getInvoiceAmount(@Param("matterNumber") String matterNumber, @Param("clientId") String clientId);

	@Query(value = "SELECT SUM(REMAIN_BAL) AS invoiceAmount FROM tblinvoiceheader\r\n"
			+ "WHERE MATTER_NO = :matterNumber AND IS_DELETED = 0 \r\n" + "GROUP BY MATTER_NO", nativeQuery = true)
	public Double getRemainingBalance(@Param("matterNumber") String matterNumber);

	public InvoiceHeader findByMatterNumber(String matterNumber);

	@Query(value = "SELECT (COALESCE(SUM(INVOICE_AMT),0) - :paidAmount) AS REMAIN_BAL \r\n"
			+ "	FROM tblinvoiceheader\r\n" + "	WHERE MATTER_NO = :matterNumber \r\n"
			+ "	AND POSTING_DATE between :fromDate and :toDate AND IS_DELETED = 0\r\n"
			+ "	GROUP BY MATTER_NO", nativeQuery = true)
	public Double getPriorBalance(@Param("matterNumber") String matterNumber, @Param("paidAmount") Double paidAmount,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	@Query(value = "SELECT SUM(PAYMENT_AMOUNT) AS paymentAmount \r\n"
			+ " FROM tblpaymentupdate WHERE MATTER_NO like :matterNumber \r\n "
			+ "	AND PAYMENT_DATE between :fromDate and :toDate \r\n"
			+ " GROUP BY MATTER_NO", nativeQuery = true)
	public Double getSumOfPmtRecByMatterNumber(@Param("matterNumber") String matterNumber,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
}