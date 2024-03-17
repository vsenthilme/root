package com.mnrclara.api.accounting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.accounting.model.invoice.InvoiceHeader;
import com.mnrclara.api.accounting.model.invoice.InvoiceHeaderTransfer;


@Repository
@Transactional
public interface InvoiceHeaderTransferRepository extends PagingAndSortingRepository<InvoiceHeaderTransfer, String>, 
			JpaSpecificationExecutor<InvoiceHeaderTransfer>, DynamicNativeQuery {
	
	public List<InvoiceHeaderTransfer> findAll();
	
	/**
	 * 
	 * @param invoiceNumber
	 * @return
	 */
	public InvoiceHeaderTransfer findByInvoiceNumber(String invoiceNumber);
	
//	public List<InvoiceHeader> findByMatterNumberAndDeletionIndicator(String matterNumber, Long isDeleted);
//	
//	/**
//	 * 
//	 * @param languageId
//	 * @param classId
//	 * @param matterNumber
//	 * @param clientId
//	 * @param caseCategoryId
//	 * @param caseSubCategoryId
//	 * @param invoiceNumber
//	 * @param invoiceFiscalYear
//	 * @param invoicePeriod
//	 * @param postingDateString
//	 * @return
//	 */
//	public Optional<InvoiceHeader> 
//			findByLanguageIdAndClassIdAndMatterNumberAndClientIdAndCaseCategoryIdAndCaseSubCategoryIdAndInvoiceNumberAndInvoiceFiscalYearAndInvoicePeriodAndPostingDateAndReferenceTextAndDeletionIndicator(
//					String languageId, 
//					Long classId, 
//					String matterNumber, 
//					String clientId, 
//					Long caseCategoryId, 
//					Long caseSubCategoryId, 
//					String invoiceNumber, 
//					String invoiceFiscalYear, 
//					String invoicePeriod, 
//					Date postingDate,
//					String referenceText, 
//					Long deletionIndicator);
//
//	public InvoiceHeader findTopByOrderByCreatedOnDesc();
//	public InvoiceHeader findTopBySentToQBAndDeletionIndicatorOrderByCreatedOn(Long sentToQbFlag, Long delFlag);
	
//	//----------------ConflictSearch-------------------------------------------------------------------
//	@Query (value = "SELECT * FROM MNRCLARA.tblinvoiceheader WHERE \r\n"
//			+ "MATCH (CLIENT_ID, MATTER_NO, INVOICE_NO)\r\n"
//			+ "AGAINST (:searchText)", nativeQuery = true)
//	List<InvoiceHeader> findRecords(@Param(value = "searchText") String searchText);
//	
//	@Query(value="SELECT SUM(REMAIN_BAL) FROM tblinvoiceheader\r\n"
//			+ "WHERE MATTER_NO = :matterNumber AND INVOICE_NO <> :invoiceNumber AND IS_DELETED = 0 \r\n"
//			+ "GROUP BY MATTER_NO", nativeQuery=true)
//    public Double getRemBal(@Param ("matterNumber") String matterNumber, @Param ("invoiceNumber") String invoiceNumber);
//	
//	@Query(value="SELECT SUM(REMAIN_BAL) FROM tblinvoiceheader\r\n"
//			+ "WHERE MATTER_NO = :matterNumber AND IS_DELETED = 0 \r\n"
//			+ "GROUP BY MATTER_NO", nativeQuery=true)
//    public Double getRemBal(@Param ("matterNumber") String matterNumber);
//	
//	@Query(value="SELECT SUM(REMAIN_BAL) AS remAmount FROM tblinvoiceheader \r\n"
//			+ "WHERE POSTING_DATE BETWEEN DATE_SUB(CURDATE(), INTERVAL :fromDiff DAY) AND \r\n"
//			+ "DATE_SUB(CURDATE(), INTERVAL :toDiff DAY)", nativeQuery=true)
//    public List<Double> getAccountAgingDetails(@Param ("fromDiff") Long fromDiff, 
//    		@Param ("toDiff") Long toDiff);
//    		
//	@Query(value="SELECT (SUM(INVOICE_AMT) - :sumOfPaymentUpdate) AS TOTAL_AMT_DUE \r\n"
//			+ "FROM tblinvoiceheader \r\n"
//			+ "WHERE MATTER_NO = :matterNumber AND IS_DELETED = 0 \r\n"
//			+ "GROUP BY MATTER_NO", nativeQuery = true)
//    public Double getInvAmt(@Param ("matterNumber") String matterNumber, @Param ("sumOfPaymentUpdate") Double sumOfPaymentUpdate);
//	
//	@Query(value="SELECT SUM(INVOICE_AMT) FROM tblinvoiceheader\r\n"
//			+ "WHERE MATTER_NO = :matterNumber AND INVOICE_NO IN (:invoiceNumbers) AND IS_DELETED = 0 \r\n"
//			+ "GROUP BY MATTER_NO", nativeQuery = true)
//    public Double getInvoiceAmountForBucket(@Param ("matterNumber") String matterNumber, @Param ("invoiceNumbers") List<String> invoiceNumbers);
//	
//	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	
//	@Query(value="SELECT COALESCE(SUM(INVOICE_AMT),0) AS UNPAID_AMT\r\n"
//			+ "	FROM tblinvoiceheader\r\n"
//			+ "	WHERE MATTER_NO = :matterNumber \r\n"
//			+ "	AND (DATEDIFF(CURDATE(),POSTING_DATE) between :startRange and :endRange) AND IS_DELETED = 0\r\n"
//			+ "	GROUP BY MATTER_NO", nativeQuery = true)
//    public Double getUnpaidAmountInvoice(
//    		@Param ("matterNumber") String matterNumber, 
//    		@Param ("startRange") Long startRange, @Param ("endRange") Long endRange);
//	
//	@Query(value="SELECT COALESCE(SUM(p.PAYMENT_AMOUNT),0) \r\n"
//			+ "FROM tblpaymentupdate p \r\n"
//			+ "WHERE p.MATTER_NO = :matterNumber \r\n"
//			+ "AND (DATEDIFF(CURDATE(),PAYMENT_DATE) between :startRange and :endRange)\r\n"
//			+ "GROUP BY p.MATTER_NO", nativeQuery = true)
//    public Double getUnpaidAmountPaymentUpdate (
//    		@Param ("matterNumber") String matterNumber, 
//    		@Param ("startRange") Long startRange, @Param ("endRange") Long endRange);
//	
////	@Query(value="SELECT SUM(INVOICE_AMT)-(SELECT COALESCE(SUM(p.PAYMENT_AMOUNT),0) \r\n"
////			+ "	FROM tblpaymentupdate p \r\n"
////			+ "	WHERE p.MATTER_NO = :matterNumber \r\n"
////			+ "	AND (DATEDIFF(CURDATE(),PAYMENT_DATE) between :startRange and :endRange)\r\n"
////			+ "	GROUP BY p.MATTER_NO) AS UNPAID_AMT\r\n"
////			+ " FROM tblinvoiceheader\r\n"
////			+ " WHERE MATTER_NO = :matterNumber AND (DATEDIFF(CURDATE(),POSTING_DATE) between :startRange and :endRange) AND IS_DELETED = 0\r\n"
////			+ " GROUP BY MATTER_NO", nativeQuery = true)
////    public Double getUnpaidAmountForBucket(
////    		@Param ("matterNumber") String matterNumber, 
////    		@Param ("startRange") Long startRange, @Param ("endRange") Long endRange);
//	
//	
//	
//	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	
//	@Query(value="SELECT SUM(REMAIN_BAL) AS remAmount FROM tblinvoiceheader \r\n"
//			+ "WHERE MATTER_NO = :matterNumber AND INVOICE_DATE BETWEEN :fromDiff AND :toDiff", nativeQuery=true)
//    public Double getUnpaidTotal(@Param ("matterNumber") String matterNumber,
//    		@Param ("fromDiff") Date fromDiff, 
//    		@Param ("toDiff") Date toDiff);
//	
//	//--------------------Matter-Billing-Activity-------------------------------------------------------------
//	@Query(value="SELECT IH.INVOICE_NO AS invoiceNumber, IH.POSTING_DATE AS postingDate, \r\n"
//			+ "IH.PRE_BILL_NO AS preBillNumber, IL.ITEM_NO AS itemNumber, \r\n"
//			+ "IL.BILL_AMOUNT AS billAmount, IH.INVOICE_REFERENCE AS invoiceRemarks , IL.INVOICE_TEXT AS invoiceDesc \r\n"
//			+ "FROM tblinvoiceheader IH \r\n"
//			+ "JOIN tblinvoiceline IL ON IH.INVOICE_NO = IL.INVOICE_NO \r\n"
//			+ "WHERE IH.MATTER_NO = :matterNumber AND IH.IS_DELETED = 0  \r\n"
//			+ "AND POSTING_DATE BETWEEN :fromPostingDate AND :toPostingDate", nativeQuery=true)
//    public List<IInvoiceActivity> getInvoiceActivity(@Param ("matterNumber") String matterNumber,
//    		@Param ("fromPostingDate") String fromPostingDate, 
//    		@Param ("toPostingDate") String toPostingDate);
//	
//	@Query(value="SELECT sum(me.EXP_AMOUNT) AS expAmount\r\n"
//			+ "FROM tblmatterexpenseid me JOIN tblexpensecodeid ec \r\n"
//			+ "ON me.EXP_CODE = ec.EXP_CODE \r\n"
//			+ "WHERE me.matter_no = :matterNumber and me.ref_field_1 = :preBillNo\r\n"
//			+ "AND ec.cost_typ = :costType group by me.matter_no", nativeQuery=true)
//    public Double getExpenseAmount(	@Param ("matterNumber") String matterNumber,
//    		@Param ("preBillNo") String preBillNo,
//    		@Param ("costType") String costType);
//
//	Page<InvoiceHeader> findByDeletionIndicator (Long deletionIndicator, Pageable page);
//
//	@Query(value="SELECT class \r\n"
//			+ "	FROM tblclassid \r\n"
//			+ "	WHERE class_id = :classId ", nativeQuery=true)
//	public String getClassName(@Param ("classId") Long classId);
//
//	@Query(value="SELECT bill_mode_text \r\n"
//			+ "	FROM tblbillingmodeid \r\n"
//			+ "	WHERE bill_mode_id = :billingModeId ", nativeQuery=true)
//	public String getBillingModeName(@Param ("billingModeId") String classId);
//	
//	//-------------------------------------------------------------------------------------------------------------------------------
//	@Query(value="SELECT m.MATTER_TEXT AS matterName, m.BILL_MODE_ID AS billingMode, b.BILL_MODE_TEXT AS billingDesc, \r\n"
//			+ "c.FIRST_LAST_NM AS clientName, m.CASE_CATEGORY_ID AS categoryId, m.CASE_SUB_CATEGORY_ID AS subCategoryId, m.ADMIN_COST AS adminCost \r\n"
//			+ "FROM tblmattergenaccid m\r\n"
//			+ "JOIN tblbillingmodeid b on b.BILL_MODE_ID = m.BILL_MODE_ID\r\n"
//			+ "JOIN tblclientgeneralid c on m.CLIENT_ID = c.CLIENT_ID\r\n"
//			+ "WHERE m.MATTER_NO = :matterNumber", nativeQuery=true)
//	public IMatterGenAcc getMatterGen(@Param ("matterNumber") String matterNumber);
//	
//	@Query(value="SELECT m.MATTER_TEXT AS matterName, m.BILL_MODE_ID AS billingMode, b.BILL_MODE_TEXT AS billingDesc, c.FIRST_LAST_NM AS clientName\r\n"
//			+ "FROM tblmattergenaccid m\r\n"
//			+ "JOIN tblbillingmodeid b on b.BILL_MODE_ID = m.BILL_MODE_ID\r\n"
//			+ "JOIN tblclientgeneralid c on m.CLIENT_ID = c.CLIENT_ID\r\n"
//			+ "WHERE m.MATTER_NO = :matterNumber and m.CASE_CATEGORY_ID = :caseCategoryId", nativeQuery=true)
//	public IMatterGenAcc getMatterGenByCaseCategory(@Param ("matterNumber") String matterNumber, @Param ("caseCategoryId") List<Long> caseCategoryId);
//	
//	@Query(value="SELECT m.MATTER_TEXT AS matterName, m.BILL_MODE_ID AS billingMode, b.BILL_MODE_TEXT AS billingDesc, c.FIRST_LAST_NM AS clientName\r\n"
//			+ "FROM tblmattergenaccid m\r\n"
//			+ "JOIN tblbillingmodeid b on b.BILL_MODE_ID = m.BILL_MODE_ID\r\n"
//			+ "JOIN tblclientgeneralid c on m.CLIENT_ID = c.CLIENT_ID\r\n"
//			+ "WHERE m.MATTER_NO = :matterNumber and m.CASE_SUB_CATEGORY_ID = :caseSubCategoryId", nativeQuery=true)
//	public IMatterGenAcc getMatterGenByCaseSubCategory(@Param ("matterNumber") String matterNumber, @Param ("caseSubCategoryId") List<Long> caseSubCategoryId);
//	
//	@Query(value="SELECT m.MATTER_TEXT AS matterName, m.BILL_MODE_ID AS billingMode, b.BILL_MODE_TEXT AS billingDesc, c.FIRST_LAST_NM AS clientName\r\n"
//			+ "FROM tblmattergenaccid m\r\n"
//			+ "JOIN tblbillingmodeid b on b.BILL_MODE_ID = m.BILL_MODE_ID\r\n"
//			+ "JOIN tblclientgeneralid c on m.CLIENT_ID = c.CLIENT_ID\r\n"
//			+ "WHERE m.MATTER_NO = :matterNumber and m.CASE_CATEGORY_ID = :caseCategoryId and m.CASE_SUB_CATEGORY_ID = :caseSubCategoryId", nativeQuery=true)
//	public IMatterGenAcc getMatterGenByCaseCategoryAndCaseSubCategory(
//			@Param ("matterNumber") String matterNumber, 
//			@Param ("caseCategoryId") List<Long> caseCategoryId, 
//			@Param ("caseSubCategoryId") List<Long> caseSubCategoryId);
//	
//	//-------------------------------------------------------------------------------------------------------------------------------
//	
//	@Query(value="SELECT INVOICE_NO FROM tblinvoiceheader WHERE SENT_TO_QB = 0 ORDER BY CTD_ON LIMIT 1", nativeQuery=true)
//	public String findTopRecord();
//	
//	@Query(value="SELECT INVOICE_NO FROM tblinvoiceheader WHERE QB_QUERY = 0 AND STATUS_ID IN (51, 52, 53) ORDER BY UTD_ON LIMIT 1", nativeQuery=true)
//	public String findTopRecordByStatusIdAndQbQuery();
//
//	@Query(value="SELECT (SUM(INVOICE_AMT) - (SELECT SUM(COALESCE((SELECT SUM(p.PAYMENT_AMOUNT) FROM tblpaymentupdate p \r\n"
//			+ "WHERE p.MATTER_NO = :matterNumber and PAYMENT_DATE BETWEEN '2000-01-01 00:00:00' \r\n"
//			+ "AND :prebillEndDate \r\n"
//			+ "GROUP BY p.MATTER_NO), 0)))) AS TOTAL FROM tblinvoiceheader  \r\n"
//			+ "WHERE INVOICE_DATE <= :dateBeforeRange AND MATTER_NO = :matterNumber AND IS_DELETED = 0  \r\n"
//			+ "GROUP BY MATTER_NO", nativeQuery=true)
//	public Double getInvoicePriorBalance (@Param ("matterNumber") String matterNumber, 
//			@Param ("prebillEndDate") Date prebillEndDate, 
//			@Param ("dateBeforeRange") Date dateBeforeRange);
//	
//	// For PreBill
//	@Query(value="SELECT (SUM(INVOICE_AMT) - (SELECT SUM(COALESCE((SELECT SUM(p.PAYMENT_AMOUNT) FROM tblpaymentupdate p \r\n"
//			+ "WHERE p.MATTER_NO = :matterNumber and PAYMENT_DATE BETWEEN '2000-01-01 00:00:00' AND :endDate \r\n"
//			+ "GROUP BY p.MATTER_NO), 0)))) AS TOTAL FROM tblinvoiceheader  \r\n"
//			+ "WHERE MATTER_NO = :matterNumber AND IS_DELETED = 0  \r\n"
//			+ "GROUP BY MATTER_NO", nativeQuery=true)
//	public Double getPrebillDetailsPriorBalance (@Param ("matterNumber") String matterNumber, @Param ("endDate") Date endDate);
//	
//	@Query(value="SELECT (SUM(INVOICE_AMT) - (:totalSum))  AS TOTAL FROM tblinvoiceheader  \r\n"
//			+ "WHERE INVOICE_DATE < :dateBeforeRange AND MATTER_NO = :matterNumber AND IS_DELETED = 0  \r\n"
//			+ "GROUP BY MATTER_NO", nativeQuery=true)
//	public Double getInvoicePriorBalanceByMatterNumberLike (@Param ("matterNumber") String matterNumber, @Param ("dateBeforeRange") Date dateBeforeRange, @Param ("totalSum") double totalSum);
//	
//	@Query(value="SELECT SUM(PAYMENT_AMOUNT) \r\n"
//			+ "FROM tblpaymentupdate WHERE MATTER_NO LIKE :matterNumber AND PAYMENT_DATE BETWEEN :startDate AND :paymentCutOffDate \r\n"
//			+ "GROUP BY MATTER_NO", nativeQuery=true)
//	public Double getPaymentReceivedForInvoiceWithLike(@Param ("matterNumber") String matterNumber,
//			@Param ("startDate") Date startDate, @Param ("paymentCutOffDate") Date paymentCutOffDate);
//	
//	@Query(value="SELECT SUM(PAYMENT_AMOUNT) \r\n"
//			+ "FROM tblpaymentupdate WHERE MATTER_NO = :matterNumber AND PAYMENT_DATE BETWEEN :startDate AND :paymentCutOffDate \r\n"
//			+ "GROUP BY MATTER_NO", nativeQuery=true)
//	public Double getPaymentReceivedForInvoice(@Param ("matterNumber") String matterNumber,
//											   @Param ("startDate") Date startDate, @Param ("paymentCutOffDate") Date paymentCutOffDate);
//	
//	//---------------------------AR-AGING-REPORT----------------------------------------------------------------------------------------------------
//	@Query(value="SELECT INVOICE_NO \r\n"
//			+ "FROM tblinvoiceheader\r\n"
//			+ "WHERE INVOICE_NO IN :invoiceNumbers \r\n"
//			+ "AND DATEDIFF(CURDATE(),INVOICE_DATE) BETWEEN :startRange AND :endRange", nativeQuery=true)
//	public List<String> findBucketByInvoiceNumbers(@Param ("invoiceNumbers") Set<String> invoiceNumbers,
//											   @Param ("startRange") Long startRange, @Param ("endRange") Long endRange);
//
//	
//	@Query(value="SELECT DISTINCT MATTER_NO AS matterNumber, INVOICE_NO AS invoiceNumber \r\n"
//			+ "FROM tblinvoiceheader\r\n"
//			+ "WHERE CLASS_ID IN (1,2) \r\n"
//			+ "AND IS_DELETED = 0", nativeQuery=true)
//	public List<IInvoiceHeader> getByCLassId();
//	
//	//---------------------------TransferBilling-----------------------------------------------------------------------------------------
//	@Modifying
//	@Query (value = "UPDATE MNRCLARA.tblinvoiceheader set STATUS_ID = 59, IS_DELETED = 1 \r\n"
//			+ " WHERE INVOICE_NO = :invoiceNumber", nativeQuery = true)
//	public void deleteInvoiceHeader(@Param(value = "invoiceNumber") String invoiceNumber);
}