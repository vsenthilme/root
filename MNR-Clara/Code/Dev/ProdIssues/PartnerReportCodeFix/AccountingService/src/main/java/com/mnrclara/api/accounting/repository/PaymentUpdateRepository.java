package com.mnrclara.api.accounting.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.accounting.model.invoice.PaymentUpdate;
import com.mnrclara.api.accounting.model.management.ClientGeneral;

@Repository
@Transactional
public interface PaymentUpdateRepository extends JpaRepository<PaymentUpdate, Long>, JpaSpecificationExecutor<PaymentUpdate> {
	
	public PaymentUpdate findByPaymentId(Long paymentId);
	public PaymentUpdate findByPaymentIdAndDeletionIndicator(Long paymentId, Long deletionIndicator);
	public List<PaymentUpdate> findByMatterNumber(String matterNumber);
	public List<PaymentUpdate> findByMatterNumberLike(String matterNumber);
	
	@Query(value="SELECT SUM(PAYMENT_AMOUNT) AS paymentAmount \r\n"
			+ "FROM tblpaymentupdate WHERE MATTER_NO = :matterNumber AND PAYMENT_DATE BETWEEN :startDate AND :paymentCutOffDate \r\n"
			+ "AND IS_DELETED = 0 GROUP BY MATTER_NO", nativeQuery=true)
    public Double getPmtRec(@Param ("matterNumber") String matterNumber, @Param ("startDate") Date startDate, 
    		@Param ("paymentCutOffDate") Date paymentCutOffDate);
	
//	@Query(value="SELECT SUM(PAYMENT_AMOUNT) AS paymentAmount \r\n"
//			+ "FROM tblpaymentupdate WHERE MATTER_NO like :matterNumber AND IS_DELETED = 0 \r\n"
//			+ "GROUP BY MATTER_NO", nativeQuery=true)
//    public Double getPmtRec(@Param ("matterNumber") String matterNumber);
	
	@Query(value="SELECT SUM(PAYMENT_AMOUNT) AS paymentAmount \r\n"
			+ " FROM tblpaymentupdate WHERE MATTER_NO like :matterNumber AND IS_DELETED = 0 \r\n "
			+ " GROUP BY MATTER_NO", nativeQuery=true)
    public Double getSumOfPmtRecByMatterNumber(@Param ("matterNumber") String matterNumber);
	
	public PaymentUpdate findTopByMatterNumberOrderByCreatedOnDesc (String matterNumber);
	
	public PaymentUpdate findTopByMatterNumberOrderByPaymentDateDesc (String matterNumber);
	
	@Query(value="SELECT SUM(PAYMENT_AMOUNT) AS paymentAmount FROM tblpaymentupdate\r\n"
			+ "WHERE UPPER(TRANSACTION_TYPE)='ADVANCE' AND MATTER_NO = :matterNumber AND IS_DELETED = 0 \r\n"
			+ "GROUP BY MATTER_NO", nativeQuery=true)
    public Double getMiscDebits(@Param ("matterNumber") String matterNumber);
	
	public PaymentUpdate findByReferenceField2(String referenceField2);
	
	//----------------------MATTER-BILLING-ACTIVITY----------------------------------------------
	// Pass the selected MATTER_NO and From and TO Dates as PAYMENT_DATE in PAYMENTUPDATE table and fetch the below values
	public List<PaymentUpdate> findByMatterNumberContainingAndPaymentDateBetween (String matterNumber,
			Date fromPostingDate, Date toPostingDate);
	
	/*
	 * SELECT PAYMENT_DATE FROM TBLPAYMENTUPDATE WHERE MATTER_NO LIKE '%52655-01%' ORDER BY CTD_ON DESC LIMIT 1;
	 */
	@Query(value="SELECT PAYMENT_DATE FROM tblpaymentupdate \r\n"
			+ " WHERE MATTER_NO LIKE :matterNumber AND PAYMENT_DATE BETWEEN :startDate AND :feesCutOffDate AND IS_DELETED = 0 \r\n "
			+ " ORDER BY PAYMENT_DATE DESC LIMIT 1", nativeQuery=true)
    public Date getPaymentDate(@Param ("matterNumber") String matterNumber, 
    		@Param ("startDate") Date startDate, @Param ("feesCutOffDate") Date feesCutOffDate);
	
	@Query(value="SELECT p.PAYMENT_AMOUNT FROM tblpaymentupdate p WHERE p.PAYMENTID IN (:paymentIds) AND p.MATTER_NO LIKE :matterNumber AND IS_DELETED = 0", nativeQuery=true)
	public List<Double> getPaymentAmountForARReport(@Param ("paymentIds") List<Long> paymentIds, @Param ("matterNumber") String matterNumber);
	
	// ByDate
	public List<PaymentUpdate> findByPaymentDateBetween(Date fromDate, Date toDate);
	public List<PaymentUpdate> findByMatterNumberLikeAndPaymentDateBetweenAndDeletionIndicator(String matterNumber, Date fromDate, Date toDate, Long delIndicator);
	public PaymentUpdate findByInvoiceNumber (String invoiceNumber);
	
	//--------------------------------------------------------------------------------------------------------------------------
	@Query(value="SELECT SUM(p.PAYMENT_AMOUNT) FROM tblpaymentupdate p \r\n"
			+ "WHERE p.MATTER_NO like :matterNumberLike and PAYMENT_DATE BETWEEN '2000-01-01 00:00:00' \r\n"
			+ "AND (SELECT START_DATE FROM tblprebilldetails WHERE MATTER_NO = :matterNumber AND PRE_BILL_NO = :preBillNumber) \r\n"
			+ "AND IS_DELETED = 0 GROUP BY p.MATTER_NO", nativeQuery=true)
	public List<Double> getSumOfPaymentAmountByMatterNumberLike (
			@Param ("matterNumber") String matterNumber, 
			@Param ("matterNumberLike") String matterNumberLike, 
			@Param ("preBillNumber") String preBillNumber);
	
	public List<PaymentUpdate> findByMatterNumberAndPaymentDateBetweenAndDeletionIndicator(String matterNumberWithClientId,
			Date paymentReceivedStartDate, Date paymentReceivedEndDate, Long delInd);
	
	//---------------------------AR-AGING-REPORT-----------------------------------------------------------------------------------------------
	
	@Query(value="SELECT PAYMENTID \r\n"
			+ "FROM tblpaymentupdate \r\n"
			+ "WHERE MATTER_NO IN :matterNumbers AND IS_DELETED = 0 \r\n"
			+ "AND DATEDIFF(CURDATE(),PAYMENT_DATE) BETWEEN :startRange AND :endRange", nativeQuery=true)
	public List<Long> findBucketByMatterNumbers (@Param ("matterNumbers") Set<String> matterNumbers,
			@Param ("startRange") Long startRange, @Param ("endRange") Long endRange);
	
	//----------------------------------------------------------------------------------------------------------------------------------------
	@Modifying
	@Query (value = "UPDATE MNRCLARA.tblpaymentupdate set STATUS_ID = '59' and IS_DELETED = 1\r\n"
			+ " WHERE PAYMENTID IN :paymentId", nativeQuery = true)
	public void deletePaymentUpdate(@Param(value = "paymentId") List<Long> paymentId);
	
	@Query(value="SELECT MAX(PAYMENTID) FROM tblpaymentupdate ", nativeQuery=true)
	public Long findMaxPaymentID ();
	
	//-------------------------AR REPORT-----------------------------------------------------------------------------------------------------
	// 
	@Query(value="SELECT PAYMENT_DATE FROM tblpaymentupdate WHERE MATTER_NO = :matterNumber ORDER BY PAYMENT_DATE DESC LIMIT 1", nativeQuery=true)
	public Date getLastPaymentDateByMatterNumber (@Param ("matterNumber") String matterNumber);
	
}