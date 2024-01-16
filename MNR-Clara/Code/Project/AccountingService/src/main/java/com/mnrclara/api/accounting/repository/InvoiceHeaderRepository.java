package com.mnrclara.api.accounting.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.mnrclara.api.accounting.model.impl.ImmigrationPaymentPlanInvoiceReportImpl;
import com.mnrclara.api.accounting.model.impl.ImmigrationPaymentPlanLineReportImpl;
import com.mnrclara.api.accounting.model.impl.ImmigrationPaymentPlanReportImpl;
import com.mnrclara.api.accounting.model.impl.MatterPLReportImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.accounting.model.invoice.InvoiceHeader;
import com.mnrclara.api.accounting.model.invoice.report.IInvoiceActivity;
import com.mnrclara.api.accounting.model.invoice.report.IInvoiceHeader;
import com.mnrclara.api.accounting.model.reports.IARReport;
import com.mnrclara.api.accounting.model.reports.IBilledHourReportInvoiceHeader;
import com.mnrclara.api.accounting.model.reports.IBilledHourReportMatterTimeTicket;
import com.mnrclara.api.accounting.model.reports.IMatterGenAcc;


@Repository
@Transactional
public interface InvoiceHeaderRepository extends PagingAndSortingRepository<InvoiceHeader, String>, 
			JpaSpecificationExecutor<InvoiceHeader>, DynamicNativeQuery {
	
	public List<InvoiceHeader> findAll();
	public InvoiceHeader findByInvoiceNumber(String invoiceNumber);
	public List<InvoiceHeader> findByPreBillNumberAndDeletionIndicator(String preBillNumber, Long isDeleted);
	public List<InvoiceHeader> findByMatterNumberAndDeletionIndicator(String matterNumber, Long isDeleted);
	
	/**
	 * 
	 * @param languageId
	 * @param classId
	 * @param matterNumber
	 * @param clientId
	 * @param caseCategoryId
	 * @param caseSubCategoryId
	 * @param invoiceNumber
	 * @param invoiceFiscalYear
	 * @param invoicePeriod
	 * @return
	 */
	public Optional<InvoiceHeader> 
			findByLanguageIdAndClassIdAndMatterNumberAndClientIdAndCaseCategoryIdAndCaseSubCategoryIdAndInvoiceNumberAndInvoiceFiscalYearAndInvoicePeriodAndPostingDateAndReferenceTextAndDeletionIndicator(
					String languageId, 
					Long classId, 
					String matterNumber, 
					String clientId, 
					Long caseCategoryId, 
					Long caseSubCategoryId, 
					String invoiceNumber, 
					String invoiceFiscalYear, 
					String invoicePeriod, 
					Date postingDate,
					String referenceText, 
					Long deletionIndicator);

	public InvoiceHeader findTopByOrderByCreatedOnDesc();
	public InvoiceHeader findTopBySentToQBAndDeletionIndicatorOrderByCreatedOn(Long sentToQbFlag, Long delFlag);
	
	//----------------ConflictSearch-------------------------------------------------------------------
	@Query (value = "SELECT * FROM MNRCLARA.tblinvoiceheader WHERE \r\n"
			+ "MATCH (CLIENT_ID, MATTER_NO, INVOICE_NO)\r\n"
			+ "AGAINST (:searchText)", nativeQuery = true)
	List<InvoiceHeader> findRecords(@Param(value = "searchText") String searchText);
	
	@Query(value="SELECT SUM(REMAIN_BAL) FROM tblinvoiceheader\r\n"
			+ "WHERE MATTER_NO = :matterNumber AND INVOICE_NO <> :invoiceNumber AND IS_DELETED = 0 \r\n"
			+ "GROUP BY MATTER_NO", nativeQuery=true)
    public Double getRemBal(@Param ("matterNumber") String matterNumber, @Param ("invoiceNumber") String invoiceNumber);
	
	@Query(value="SELECT SUM(REMAIN_BAL) FROM tblinvoiceheader\r\n"
			+ "WHERE MATTER_NO = :matterNumber AND IS_DELETED = 0 \r\n"
			+ "GROUP BY MATTER_NO", nativeQuery=true)
    public Double getRemBal(@Param ("matterNumber") String matterNumber);
	
	@Query(value="SELECT SUM(REMAIN_BAL) AS remAmount FROM tblinvoiceheader \r\n"
			+ "WHERE POSTING_DATE BETWEEN DATE_SUB(CURDATE(), INTERVAL :fromDiff DAY) AND \r\n"
			+ "DATE_SUB(CURDATE(), INTERVAL :toDiff DAY)", nativeQuery=true)
    public List<Double> getAccountAgingDetails(@Param ("fromDiff") Long fromDiff, 
    		@Param ("toDiff") Long toDiff);
    		
	@Query(value="SELECT (SUM(INVOICE_AMT) - :sumOfPaymentUpdate) AS TOTAL_AMT_DUE \r\n"
			+ "FROM tblinvoiceheader \r\n"
			+ "WHERE MATTER_NO = :matterNumber AND IS_DELETED = 0 \r\n"
			+ "GROUP BY MATTER_NO", nativeQuery = true)
    public Double getInvAmt(@Param ("matterNumber") String matterNumber, @Param ("sumOfPaymentUpdate") Double sumOfPaymentUpdate);
	
	@Query(value="SELECT SUM(PAID_AMOUNT) AS paymentAmount \r\n"
			+ " FROM tblinvoiceheader WHERE MATTER_NO like :matterNumber \r\n "
			+ " GROUP BY MATTER_NO", nativeQuery=true)
    public Double getSumOfPaidAmountByMatterNumber(@Param ("matterNumber") String matterNumber);
	
	@Query(value="SELECT SUM(INVOICE_AMT) FROM tblinvoiceheader\r\n"
			+ "WHERE MATTER_NO = :matterNumber AND INVOICE_NO IN (:invoiceNumbers) AND IS_DELETED = 0 \r\n"
			+ "GROUP BY MATTER_NO", nativeQuery = true)
    public Double getInvoiceAmountForBucket(@Param ("matterNumber") String matterNumber, @Param ("invoiceNumbers") List<String> invoiceNumbers);
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Query(value="SELECT COALESCE(SUM(INVOICE_AMT),0) AS UNPAID_AMT\r\n"
			+ "	FROM tblinvoiceheader\r\n"
			+ "	WHERE MATTER_NO = :matterNumber \r\n"
			+ "	AND (DATEDIFF(CURDATE(),POSTING_DATE) between :startRange and :endRange) AND IS_DELETED = 0\r\n"
			+ "	GROUP BY MATTER_NO", nativeQuery = true)
    public Double getUnpaidAmountInvoice(
    		@Param ("matterNumber") String matterNumber, 
    		@Param ("startRange") Long startRange, @Param ("endRange") Long endRange);
	
	@Query(value="SELECT COALESCE(SUM(PAID_AMOUNT),0) AS UNPAID_AMT\r\n"
			+ "	FROM tblinvoiceheader\r\n"
			+ "	WHERE MATTER_NO = :matterNumber \r\n"
			+ "	AND (DATEDIFF(CURDATE(),POSTING_DATE) between :startRange and :endRange) AND IS_DELETED = 0\r\n"
			+ "	GROUP BY MATTER_NO", nativeQuery = true)
    public Double getUnpaidAmountFromInvoice(
    		@Param ("matterNumber") String matterNumber, 
    		@Param ("startRange") Long startRange, @Param ("endRange") Long endRange);
	
	@Query(value="SELECT COALESCE(SUM(p.PAYMENT_AMOUNT),0) \r\n"
			+ "FROM tblpaymentupdate p \r\n"
			+ "WHERE p.MATTER_NO = :matterNumber \r\n"
			+ "AND (DATEDIFF(CURDATE(),PAYMENT_DATE) between :startRange and :endRange)\r\n"
			+ "GROUP BY p.MATTER_NO", nativeQuery = true)
    public Double getUnpaidAmountPaymentUpdate (
    		@Param ("matterNumber") String matterNumber, 
    		@Param ("startRange") Long startRange, @Param ("endRange") Long endRange);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Query(value="SELECT SUM(REMAIN_BAL) AS remAmount FROM tblinvoiceheader \r\n"
			+ "WHERE MATTER_NO = :matterNumber AND INVOICE_DATE BETWEEN :fromDiff AND :toDiff", nativeQuery=true)
    public Double getUnpaidTotal(@Param ("matterNumber") String matterNumber,
    		@Param ("fromDiff") Date fromDiff, 
    		@Param ("toDiff") Date toDiff);
	
	//--------------------Matter-Billing-Activity-------------------------------------------------------------
	@Query(value="SELECT IH.INVOICE_NO AS invoiceNumber, IH.POSTING_DATE AS postingDate, \r\n"
			+ "IH.PRE_BILL_NO AS preBillNumber, IL.ITEM_NO AS itemNumber, \r\n"
			+ "IL.BILL_AMOUNT AS billAmount, IH.INVOICE_REFERENCE AS invoiceRemarks , IL.INVOICE_TEXT AS invoiceDesc \r\n"
			+ "FROM tblinvoiceheader IH \r\n"
			+ "JOIN tblinvoiceline IL ON IH.INVOICE_NO = IL.INVOICE_NO \r\n"
			+ "WHERE IH.MATTER_NO = :matterNumber AND IH.IS_DELETED = 0  \r\n"
			+ "AND POSTING_DATE BETWEEN :fromPostingDate AND :toPostingDate", nativeQuery=true)
    public List<IInvoiceActivity> getInvoiceActivity(@Param ("matterNumber") String matterNumber,
    		@Param ("fromPostingDate") String fromPostingDate, 
    		@Param ("toPostingDate") String toPostingDate);
	
	@Query(value="SELECT sum(me.EXP_AMOUNT) AS expAmount\r\n"
			+ "FROM tblmatterexpenseid me JOIN tblexpensecodeid ec \r\n"
			+ "ON me.EXP_CODE = ec.EXP_CODE \r\n"
			+ "WHERE me.matter_no = :matterNumber and me.ref_field_1 = :preBillNo\r\n"
			+ "AND ec.cost_typ = :costType group by me.matter_no", nativeQuery=true)
    public Double getExpenseAmount(	@Param ("matterNumber") String matterNumber,
    		@Param ("preBillNo") String preBillNo,
    		@Param ("costType") String costType);

	Page<InvoiceHeader> findByDeletionIndicator (Long deletionIndicator, Pageable page);

	@Query(value="SELECT class \r\n"
			+ "	FROM tblclassid \r\n"
			+ "	WHERE class_id = :classId ", nativeQuery=true)
	public String getClassName(@Param ("classId") Long classId);

	@Query(value="SELECT partner_assigned \r\n"
			+ "	FROM tblbillingreport \r\n"
			+ "	WHERE inv_no = :invoiceNumber ", nativeQuery=true)
	public String getPartnerAssigned(@Param ("invoiceNumber") String invoiceNumber);

	@Query(value="SELECT partner_assigned \r\n"
			+ "	FROM tblinvoiceheader \r\n"
			+ "	WHERE invoice_no = :invoiceNumber ", nativeQuery=true)
	public String getPartnerAssignedFromInvoice(@Param ("invoiceNumber") String invoiceNumber);

	@Query(value="SELECT bill_mode_text \r\n"
			+ "	FROM tblbillingmodeid \r\n"
			+ "	WHERE bill_mode_id = :billingModeId ", nativeQuery=true)
	public String getBillingModeName(@Param ("billingModeId") String classId);
	
	//-------------------------------------------------------------------------------------------------------------------------------
	@Query(value="SELECT m.MATTER_TEXT AS matterName, m.BILL_MODE_ID AS billingMode, b.BILL_MODE_TEXT AS billingDesc, \r\n"
			+ "c.FIRST_LAST_NM AS clientName, m.CASE_CATEGORY_ID AS categoryId, m.CASE_SUB_CATEGORY_ID AS subCategoryId, m.ADMIN_COST AS adminCost \r\n"
			+ "FROM tblmattergenaccid m\r\n"
			+ "JOIN tblbillingmodeid b on b.BILL_MODE_ID = m.BILL_MODE_ID\r\n"
			+ "JOIN tblclientgeneralid c on m.CLIENT_ID = c.CLIENT_ID\r\n"
			+ "WHERE m.MATTER_NO = :matterNumber", nativeQuery=true)
	public IMatterGenAcc getMatterGen(@Param ("matterNumber") String matterNumber);
	
	@Query(value="SELECT m.MATTER_TEXT AS matterName, m.BILL_MODE_ID AS billingMode, b.BILL_MODE_TEXT AS billingDesc, c.FIRST_LAST_NM AS clientName\r\n"
			+ "FROM tblmattergenaccid m\r\n"
			+ "JOIN tblbillingmodeid b on b.BILL_MODE_ID = m.BILL_MODE_ID\r\n"
			+ "JOIN tblclientgeneralid c on m.CLIENT_ID = c.CLIENT_ID\r\n"
			+ "WHERE m.MATTER_NO = :matterNumber and m.CASE_CATEGORY_ID = :caseCategoryId", nativeQuery=true)
	public IMatterGenAcc getMatterGenByCaseCategory(@Param ("matterNumber") String matterNumber, @Param ("caseCategoryId") List<Long> caseCategoryId);
	
	@Query(value="SELECT m.MATTER_TEXT AS matterName, m.BILL_MODE_ID AS billingMode, b.BILL_MODE_TEXT AS billingDesc, c.FIRST_LAST_NM AS clientName\r\n"
			+ "FROM tblmattergenaccid m\r\n"
			+ "JOIN tblbillingmodeid b on b.BILL_MODE_ID = m.BILL_MODE_ID\r\n"
			+ "JOIN tblclientgeneralid c on m.CLIENT_ID = c.CLIENT_ID\r\n"
			+ "WHERE m.MATTER_NO = :matterNumber and m.CASE_SUB_CATEGORY_ID = :caseSubCategoryId", nativeQuery=true)
	public IMatterGenAcc getMatterGenByCaseSubCategory(@Param ("matterNumber") String matterNumber, @Param ("caseSubCategoryId") List<Long> caseSubCategoryId);
	
	@Query(value="SELECT m.MATTER_TEXT AS matterName, m.BILL_MODE_ID AS billingMode, b.BILL_MODE_TEXT AS billingDesc, c.FIRST_LAST_NM AS clientName\r\n"
			+ "FROM tblmattergenaccid m\r\n"
			+ "JOIN tblbillingmodeid b on b.BILL_MODE_ID = m.BILL_MODE_ID\r\n"
			+ "JOIN tblclientgeneralid c on m.CLIENT_ID = c.CLIENT_ID\r\n"
			+ "WHERE m.MATTER_NO = :matterNumber and m.CASE_CATEGORY_ID = :caseCategoryId and m.CASE_SUB_CATEGORY_ID = :caseSubCategoryId", nativeQuery=true)
	public IMatterGenAcc getMatterGenByCaseCategoryAndCaseSubCategory(
			@Param ("matterNumber") String matterNumber, 
			@Param ("caseCategoryId") List<Long> caseCategoryId, 
			@Param ("caseSubCategoryId") List<Long> caseSubCategoryId);
	
	//-------------------------------------------------------------------------------------------------------------------------------
	
	@Query(value="SELECT INVOICE_NO FROM tblinvoiceheader WHERE SENT_TO_QB = 0 ORDER BY CTD_ON LIMIT 1", nativeQuery=true)
	public String findTopRecord();
	
	@Query(value="SELECT INVOICE_NO FROM tblinvoiceheader WHERE QB_QUERY = 0 AND STATUS_ID IN (51, 52, 53) ORDER BY UTD_ON LIMIT 1", nativeQuery=true)
	public String findTopRecordByStatusIdAndQbQuery();

	@Query(value="SELECT (SUM(INVOICE_AMT) - (SELECT SUM(COALESCE((SELECT SUM(p.PAYMENT_AMOUNT) FROM tblpaymentupdate p \r\n"
			+ "WHERE p.MATTER_NO = :matterNumber and PAYMENT_DATE BETWEEN '2000-01-01 00:00:00' \r\n"
			+ "AND :prebillEndDate AND IS_DELETED = 0 \r\n"
			+ "GROUP BY p.MATTER_NO), 0)))) AS TOTAL FROM tblinvoiceheader  \r\n"
			+ "WHERE INVOICE_DATE <= :dateBeforeRange AND MATTER_NO = :matterNumber AND IS_DELETED = 0  \r\n"
			+ "GROUP BY MATTER_NO", nativeQuery=true)
	public Double getInvoicePriorBalance (@Param ("matterNumber") String matterNumber, 
			@Param ("prebillEndDate") Date prebillEndDate, 
			@Param ("dateBeforeRange") Date dateBeforeRange);
	
	// For PreBill
	@Query(value="SELECT (SUM(INVOICE_AMT) - (SELECT SUM(COALESCE((SELECT SUM(p.PAYMENT_AMOUNT) FROM tblpaymentupdate p \r\n"
			+ "WHERE p.MATTER_NO = :matterNumber and PAYMENT_DATE BETWEEN '2000-01-01 00:00:00' AND :endDate AND IS_DELETED = 0 \r\n"
			+ "GROUP BY p.MATTER_NO), 0)))) AS TOTAL FROM tblinvoiceheader  \r\n"
			+ "WHERE MATTER_NO = :matterNumber AND IS_DELETED = 0  \r\n"
			+ "GROUP BY MATTER_NO", nativeQuery=true)
	public Double getPrebillDetailsPriorBalance (@Param ("matterNumber") String matterNumber, @Param ("endDate") Date endDate);
	
	@Query(value="SELECT (SUM(INVOICE_AMT) - (:totalSum))  AS TOTAL FROM tblinvoiceheader  \r\n"
			+ "WHERE INVOICE_DATE < :dateBeforeRange AND MATTER_NO = :matterNumber AND IS_DELETED = 0  \r\n"
			+ "GROUP BY MATTER_NO", nativeQuery=true)
	public Double getInvoicePriorBalanceByMatterNumberLike (@Param ("matterNumber") String matterNumber, @Param ("dateBeforeRange") Date dateBeforeRange, @Param ("totalSum") double totalSum);
	
	@Query(value="SELECT SUM(PAYMENT_AMOUNT) \r\n"
			+ "FROM tblpaymentupdate WHERE MATTER_NO LIKE :matterNumber AND IS_DELETED = 0 AND PAYMENT_DATE BETWEEN :startDate AND :paymentCutOffDate \r\n"
			+ "GROUP BY MATTER_NO", nativeQuery=true)
	public Double getPaymentReceivedForInvoiceWithLike(@Param ("matterNumber") String matterNumber,
			@Param ("startDate") Date startDate, @Param ("paymentCutOffDate") Date paymentCutOffDate);
	
	@Query(value="SELECT SUM(PAYMENT_AMOUNT) \r\n"
			+ "FROM tblpaymentupdate WHERE MATTER_NO = :matterNumber AND IS_DELETED = 0 AND PAYMENT_DATE BETWEEN :startDate AND :paymentCutOffDate \r\n"
			+ "GROUP BY MATTER_NO", nativeQuery=true)
	public Double getPaymentReceivedForInvoice(@Param ("matterNumber") String matterNumber,
											   @Param ("startDate") Date startDate, @Param ("paymentCutOffDate") Date paymentCutOffDate);
	
	//---------------------------AR-AGING-REPORT----------------------------------------------------------------------------------------------------
	@Query(value="SELECT INVOICE_NO \r\n"
			+ "FROM tblinvoiceheader\r\n"
			+ "WHERE INVOICE_NO IN :invoiceNumbers \r\n"
			+ "AND DATEDIFF(CURDATE(),INVOICE_DATE) BETWEEN :startRange AND :endRange", nativeQuery=true)
	public List<String> findBucketByInvoiceNumbers(@Param ("invoiceNumbers") Set<String> invoiceNumbers,
											   @Param ("startRange") Long startRange, @Param ("endRange") Long endRange);
	
	@Query(value="SELECT DISTINCT MATTER_NO AS matterNumber, INVOICE_NO AS invoiceNumber \r\n"
			+ "FROM tblinvoiceheader\r\n"
			+ "WHERE CLASS_ID IN (1,2) \r\n"
			+ "AND IS_DELETED = 0", nativeQuery=true)
	public List<IInvoiceHeader> getByCLassId();
	
	//---------------------------TransferBilling-----------------------------------------------------------------------------------------
	@Modifying
	@Query (value = "UPDATE MNRCLARA.tblinvoiceheader set STATUS_ID = 59, IS_DELETED = 1 \r\n"
			+ " WHERE INVOICE_NO = :invoiceNumber", nativeQuery = true)
	public void deleteInvoiceHeader(@Param(value = "invoiceNumber") String invoiceNumber);


	//Matter P&L Report
	@Query(value = "select \n"
			+ "ih.client_id clientId, \n"
			+ "cg.FIRST_LAST_NM clientName, \n"
			+ "ih.matter_no matterNumber, \n"
			+ "mg.MATTER_TEXT matterDescription, \n"
			+ "ih.partner_assigned partnerAssigned, \n"
			+ "ih.invoice_date invoiceDate, \n"
			+ "ih.invoice_no invoiceNumber, \n"
			+ "(select sum(time_ticket_amount) from tblmattertimeticketid where is_deleted=0 and \n"
			+ "	ref_field_1=ih.pre_bill_no) timeticketCaptured, \n"
			+ "(select sum(exp_amount) from tblmatterexpenseid where is_deleted=0 and \n"
			+ "	ref_field_1=ih.pre_bill_no) costCaptured \n"
			+ "	from tblinvoiceheader ih \n"
			+ "	left join tblclientgeneralid cg on cg.client_id = ih.client_id \n"
			+ "	left join tblmattergenaccid mg on mg.matter_no = ih.matter_no \n"
			+ "	where \n"
			+ "(COALESCE(:classId) IS NULL OR (mg.class_id IN (:classId))) and \n"
			+ "(COALESCE(:partner) IS NULL OR (ih.partner_assigned IN (:partner))) and \n"
			+ "(COALESCE(:matterNumber) IS NULL OR (mg.matter_no IN (:matterNumber))) and \n"
			+ "(COALESCE(:clientNumber) IS NULL OR (cg.client_id IN (:clientNumber))) and \n"
			+ "(COALESCE(:caseCategoryId) IS NULL OR (mg.case_category_id IN (:caseCategoryId))) and \n"
			+ "(COALESCE(:caseSubCategoryId) IS NULL OR (mg.case_sub_category_id IN (:caseSubCategoryId))) and \n"
			+ "(COALESCE(:startDate) IS NULL OR (ih.invoice_date between :startDate and :endDate)) and \n"
			+ "ih.is_deleted=0 and mg.bill_mode_id=2", nativeQuery = true)
	public List<MatterPLReportImpl> getMatterPLReport(
			@Param(value = "classId") List<Long> classId,
			@Param(value = "partner") List<String> partner,
			@Param(value = "matterNumber") List<String> matterNumber,
			@Param(value = "clientNumber") List<String> clientNumber,
			@Param(value = "caseCategoryId") List<Long> caseCategoryId,
			@Param(value = "caseSubCategoryId") List<Long> caseSubCategoryId,
			@Param(value = "startDate") Date startDate,
			@Param(value = "endDate") Date endDate);
	
	//------------------------AR-REPORT---------------------------------------------------------------------
	@Query(value="SELECT INVOICE_NO FROM tblinvoiceheader \r\n"
			+ "WHERE MATTER_NO IN :matterNumbers AND IS_DELETED = 0  \r\n"
			+ "AND POSTING_DATE BETWEEN :fromPostingDate AND :toPostingDate", nativeQuery=true)
	public List<String> getInvoiceNumbers(@Param(value = "matterNumbers") List<String> matterNumbers,
			@Param(value = "fromPostingDate") Date fromPostingDate,
			@Param(value = "toPostingDate") Date toPostingDate);
	
	@Query(value="SELECT DISTINCT CLASS_ID AS classId, CLIENT_ID AS clientId, MATTER_NO AS matterNumber, POSTING_DATE AS postingDate FROM tblinvoiceheader \r\n"
			+ "WHERE MATTER_NO IN :matterNumbers AND IS_DELETED = 0  \r\n"
			+ "AND POSTING_DATE BETWEEN :fromPostingDate AND :toPostingDate", nativeQuery=true)
	public List<IARReport> getInvoiceNumbersForARRpoer(@Param(value = "matterNumbers") List<String> matterNumbers,
			@Param(value = "fromPostingDate") Date fromPostingDate,
			@Param(value = "toPostingDate") Date toPostingDate);
	
//	List<InvoiceHeader> findDistinctByMatterNumberInAndPostingDateBetweenAndDeletionIndicator(List<String> matterNumbers, 
//			Date fromPostingDate, Date toPostingDate, Long delInd);
	
	@Query(value="SELECT INVOICE_DATE FROM tblinvoiceheader WHERE MATTER_NO = :matterNumber ORDER BY INVOICE_DATE DESC LIMIT 1", nativeQuery=true)
	public Date getLastBillDateByMatterNumber (@Param ("matterNumber") String matterNumber);
	
	//-----------------------Finding-Previous-Invoice-Date-----------------------------------------------------
	@Query(value="SELECT MAX(POSTING_DATE) FROM tblinvoiceheader \r\n"
			+ "WHERE INVOICE_NO IN :invoiceNumbers \r\n"
			+ "AND POSTING_DATE < (SELECT POSTING_DATE FROM tblinvoiceheader WHERE INVOICE_NO = :invoiceNumber)", nativeQuery=true)
	public Date getPreviosInvoiceDate(
			@Param(value = "invoiceNumbers") List<Integer> listInvoiceNumbers,
			@Param(value = "invoiceNumber") String invoiceNumber);

	//Immigration Payment Plan Report - Header
	@Query(value = "select \n"
			+ "pph.payment_plan_no paymentPlanNumber, \n"
			+ "pph.client_id clientId, \n"
			+ "pph.matter_no matterNumber, \n"
			+ "pph.quote_no quoteNumber, \n"
			+ "pph.payment_plan_amt paymentPlanAmount, \n"
			+ "pph.install_amt instalmentAmount, \n"
			+ "pph.start_date startDate, \n"
			+ "pph.payment_plan_date paymentPlanDate, \n"
			+ "pph.status_id status, \n"
			+ "cg.first_last_nm clientName, \n"
			+ "cg.cont_no clientCell, \n"
			+ "cg.work clientWorkNumber, \n"
			+ "cg.home clientPhoneNumber \n"
			+ "from tblpaymentplanheader pph \n"
			+ "left join tblclientgeneralid cg on cg.client_id = pph.client_id \n"
			+ "where \n"
			+ "(COALESCE(:matterNumber) IS NULL OR (pph.matter_no IN (:matterNumber))) and \n"
			+ "(COALESCE(:clientNumber) IS NULL OR (pph.client_id IN (:clientNumber))) and \n"
			+ "pph.is_deleted=0 ", nativeQuery = true)
	public List<ImmigrationPaymentPlanReportImpl> getImmigrationPaymentPlanReport(
			@Param(value = "matterNumber") List<String> matterNumber,
			@Param(value = "clientNumber") List<String> clientNumber);

	//Immigration Payment Plan Report - Due Date
	@Query(value = "select pl.due_date dueDate from tblpaymentplanline pl\n"
			+ "where \n"
			+ "pl.payment_plan_no IN (:paymentPlanNumber) and \n"
			+ "(COALESCE(:startDueDate) IS NULL OR (pl.due_date between :startDueDate and :endDueDate)) and \n"
			+ "pl.due_date>current_date()  and\n"
			+ "pl.is_deleted=0 order by pl.due_date limit 1", nativeQuery = true)
	public ImmigrationPaymentPlanLineReportImpl getPaymentPlanReportLine2(
			@Param(value = "paymentPlanNumber") String paymentPlanNumber,
			@Param(value = "startDueDate") Date startDueDate,
			@Param(value = "endDueDate") Date endDueDate);

	//Immigration Payment Plan Report - Remainder Date
	@Query(value = "select pl2.reminder_date remainderDate from tblpaymentplanline pl2\n"
			+ "where \n"
			+ "pl2.payment_plan_no IN (:paymentPlanNumber) and \n"
			+ "(COALESCE(:fromDate) IS NULL OR (pl2.reminder_date between :fromDate and :toDate)) and \n"
			+ "pl2.reminder_date>current_date()  and\n"
			+ "pl2.is_deleted=0 order by pl2.reminder_date limit 1", nativeQuery = true)
	public ImmigrationPaymentPlanLineReportImpl getPaymentPlanReportLine3(
			@Param(value = "paymentPlanNumber") String paymentPlanNumber,
			@Param(value = "fromDate") Date fromDate,
			@Param(value = "toDate") Date toDate);

	//Immigration Payment Plan Report - Balance Amount & Paid Amount
	@Query(value = "select \n"
			+ "balance_amount balanceAmount, \n"
			+ "paid_amount paidAmount \n"
			+ "from tblpaymentplanline ppl \n"
			+ "where \n"
			+ "ppl.payment_plan_no IN (:paymentPlanNumber) and \n"
			+ "(ppl.reminder_date in (:toDate)) and \n"
			+ "(ppl.due_date in (:endDueDate)) and \n"
			+ "ppl.is_deleted=0", nativeQuery = true)
	public ImmigrationPaymentPlanLineReportImpl getPaymentPlanReportLine4(
			@Param(value = "paymentPlanNumber") String paymentPlanNumber,
			@Param(value = "toDate") Date toDate,
			@Param(value = "endDueDate") Date endDueDate);

	//Immigration Payment Plan Report - Invoice Header
	@Query(value = "select \n"
			+ "invoice_no invoiceNumber, \n"
			+ "invoice_amt invoiceAmount, \n"
			+ "invoice_date invoiceDate \n"
			+ "from tblinvoiceheader ih \n"
			+ "where \n"
			+ "ih.matter_no IN (:matterNumber) and \n"
			+ "ih.is_deleted=0", nativeQuery = true)
	public List<ImmigrationPaymentPlanInvoiceReportImpl> getPaymentPlanReportInvoice(
			@Param(value = "matterNumber") String matterNumber);
	
	//-------------------------BILLED_HOURS_PAID_REPORT-------------------------------------------------------------------
	@Query(value="SELECT ih.MATTER_NO AS matterNumber, ih.INVOICE_NO AS invoiceNumber, \r\n"
			+ "SUM(ih.INVOICE_AMT) AS invoiceAmount, ih.PRE_BILL_NO AS preBillNumber, ih.POSTING_DATE as dateOfBill \r\n"
			+ "FROM tblinvoiceheader ih\r\n"
			+ "WHERE ih.POSTING_DATE BETWEEN :fromDate AND :toDate \r\n"
			+ "GROUP BY ih.MATTER_NO, ih.INVOICE_NO, ih.PRE_BILL_NO, ih.POSTING_DATE", nativeQuery = true)
    public List<IBilledHourReportInvoiceHeader> getSumOfInvoiceAmtWOMatterNumberAndClientID(
    		@Param(value = "fromDate") Date fromDate,
			@Param(value = "toDate") Date toDate);
	
	@Query(value="SELECT ih.MATTER_NO AS matterNumber, ih.INVOICE_NO AS invoiceNumber, \r\n"
			+ "SUM(ih.INVOICE_AMT) AS invoiceAmount, ih.PRE_BILL_NO AS preBillNumber, ih.POSTING_DATE as dateOfBill \r\n"
			+ "FROM tblinvoiceheader ih\r\n"
			+ "WHERE ih.POSTING_DATE BETWEEN :fromDate AND :toDate AND ih.MATTER_NO IN :matterNumber \r\n"
			+ "GROUP BY ih.MATTER_NO, ih.INVOICE_NO, ih.PRE_BILL_NO, ih.POSTING_DATE", nativeQuery = true)
    public List<IBilledHourReportInvoiceHeader> getSumOfInvoiceAmtByMatterNumber(
    		@Param(value = "fromDate") Date fromDate,
			@Param(value = "toDate") Date toDate,
			@Param(value = "matterNumber") List<String> matterNumber);
	
	@Query(value="SELECT ih.MATTER_NO AS matterNumber, ih.INVOICE_NO AS invoiceNumber, \r\n"
			+ "SUM(ih.INVOICE_AMT) AS invoiceAmount, ih.PRE_BILL_NO AS preBillNumber, ih.POSTING_DATE as dateOfBill \r\n"
			+ "FROM tblinvoiceheader ih\r\n"
			+ "WHERE ih.POSTING_DATE BETWEEN :fromDate AND :toDate AND ih.MATTER_NO IN :matterNumber AND ih.CLIENT_ID IN :clientId\r\n"
			+ "GROUP BY ih.MATTER_NO, ih.INVOICE_NO, ih.PRE_BILL_NO, ih.POSTING_DATE", nativeQuery = true)
    public List<IBilledHourReportInvoiceHeader> getSumOfInvoiceAmtByMatterNumberAndClientID (
    		@Param(value = "fromDate") Date fromDate,
			@Param(value = "toDate") Date toDate,
			@Param(value = "matterNumber") List<String> matterNumber,
			@Param(value = "clientId") List<String> clientId);
	
	@Query(value="SELECT ih.MATTER_NO AS matterNumber, ih.INVOICE_NO AS invoiceNumber, \r\n"
			+ "SUM(ih.INVOICE_AMT) AS invoiceAmount, ih.PRE_BILL_NO AS preBillNumber, ih.POSTING_DATE as dateOfBill \r\n"
			+ "FROM tblinvoiceheader ih\r\n"
			+ "WHERE ih.POSTING_DATE BETWEEN :fromDate AND :toDate AND ih.CLIENT_ID IN :clientId \r\n"
			+ "GROUP BY ih.MATTER_NO, ih.INVOICE_NO, ih.PRE_BILL_NO, ih.POSTING_DATE", nativeQuery = true)
    public List<IBilledHourReportInvoiceHeader> getSumOfInvoiceAmtByClientID (
    		@Param(value = "fromDate") Date fromDate,
			@Param(value = "toDate") Date toDate,
			@Param(value = "clientId") List<String> clientId);
	
//	@Query(value="SELECT SUM(pu.PAYMENT_AMOUNT)\r\n"
//			+ "FROM tblpaymentupdate pu \r\n"
//			+ "where pu.MATTER_NO = :matterNumber AND pu.IS_DELETED = 0\r\n"
//			+ "AND pu.PAYMENT_DATE BETWEEN :fromDate AND :toDate \r\n"
//			+ "GROUP BY pu.MATTER_NO", nativeQuery = true)
//    public Double getSumOfPaymentAmtByMatterNumber(
//    		@Param(value = "fromDate") Date fromDate,
//			@Param(value = "toDate") Date toDate,
//			@Param(value = "matterNumber") String matterNumber);
	
	@Query(value="SELECT SUM(ih.PAID_AMOUNT) \r\n"
			+ "FROM tblinvoiceheader ih \r\n"
			+ "where ih.MATTER_NO = :matterNumber AND ih.IS_DELETED = 0\r\n"
			+ "AND ih.INVOICE_DATE BETWEEN :fromDate AND :toDate \r\n"
			+ " AND ih.INVOICE_NO = :invoiceNumber GROUP BY ih.INVOICE_NO", nativeQuery = true)
    public Double getSumOfPaymentAmtByMatterNumber(
    		@Param(value = "fromDate") Date fromDate,
			@Param(value = "toDate") Date toDate,
			@Param(value = "matterNumber") String matterNumber, 
			@Param(value = "invoiceNumber") String invoiceNumber);
	
	@Query(value="SELECT REF_FIELD_1 AS preBillNumber, TK_CODE AS attorney, SUM(APP_BILL_TIME) AS hoursBilled, SUM(APP_BILL_AMOUNT) AS amountBilled \r\n"
			+ "FROM tblmattertimeticketid tt \r\n"
			+ "WHERE MATTER_NO = :matterNumber and REF_FIELD_1 = :preBillNumber \r\n"
			+ "GROUP BY REF_FIELD_1, TK_CODE", nativeQuery = true)
    public List<IBilledHourReportMatterTimeTicket> getMatterTimeTicketByMatterNumberWOTKCode(
    		@Param(value = "preBillNumber") String preBillNumber,
    		@Param(value = "matterNumber") String matterNumber);
	
	@Query(value="SELECT REF_FIELD_1 AS preBillNumber, TK_CODE AS attorney, SUM(APP_BILL_TIME) AS hoursBilled, SUM(APP_BILL_AMOUNT) AS amountBilled \r\n"
			+ " FROM tblmattertimeticketid \r\n"
			+ " WHERE MATTER_NO = :matterNumber and REF_FIELD_1 = :preBillNumber \r\n"
			+ " AND TK_CODE IN :tkCode GROUP BY REF_FIELD_1", nativeQuery = true)
    public List<IBilledHourReportMatterTimeTicket> getMatterTimeTicketByMatterNumberWithTKCode(
    		@Param(value = "preBillNumber") String preBillNumber,
    		@Param(value = "matterNumber") String matterNumber,
    		@Param(value = "tkCode") List<String> tkCode);
}