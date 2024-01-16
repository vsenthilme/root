package com.mnrclara.api.accounting.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.accounting.model.invoice.InvoiceLine;


@Repository
@Transactional
public interface InvoiceLineRepository extends JpaRepository<InvoiceLine,Long>, JpaSpecificationExecutor<InvoiceLine> {
	
	/**
	 * 
	 */
	public List<InvoiceLine> findAll();
	
	/**
	 * 
	 * @param languageId
	 * @param classId
	 * @param matterNumber
	 * @param clientId
	 * @param invoiceNumber
	 * @param invoiceFiscalYear
	 * @param invoicePeriod
	 * @param itemNumber
	 * @param deletionIndicator
	 * @return
	 */
	public Optional<InvoiceLine> 
			findByLanguageIdAndClassIdAndMatterNumberAndClientIdAndInvoiceNumberAndInvoiceFiscalYearAndInvoicePeriodAndItemNumberAndDeletionIndicator(
					String languageId, 
					Long classId, 
					String matterNumber, 
					String clientId, 
					String invoiceNumber, 
					String invoiceFiscalYear, 
					String invoicePeriod, 
					Long itemNumber, 
					Long deletionIndicator);

	public InvoiceLine findByInvoiceFiscalYear(String invoiceFiscalYear);
	
	public List<InvoiceLine> findByInvoiceNumber(String invoiceNumber);
	public List<InvoiceLine> findByInvoiceNumberIn(List<String> invoiceNumber);
	public InvoiceLine findByInvoiceNumberAndItemNumber(String invoiceNumber, Long itemNumber);

	public InvoiceLine findByInvoiceNumberAndMatterNumberAndItemNumber(String invoiceNumber, String matterNumber, Long itemNumber);
	
	@Query(value="SELECT SUM(BILL_AMOUNT) FROM tblinvoiceline\r\n"
			+ "WHERE MATTER_NO = :mtterNumber AND ITEM_NO = :itemNumber\r\n"
			+ "GROUP BY MATTER_NO", nativeQuery=true)
    public Double getBilledAmount(@Param ("mtterNumber") String mtterNumber, 
    		@Param ("itemNumber") Long itemNumber );
	
	//---------------------------TransferBilling-----------------------------------------------------------------------------------------
	@Modifying
	@Query (value = "UPDATE MNRCLARA.tblinvoiceline set STATUS_ID = 59, IS_DELETED = 1 \r\n"
			+ " WHERE INVOICE_NO = :invoiceNumber", nativeQuery = true)
	public void deleteInvoiceLine(@Param(value = "invoiceNumber") String invoiceNumber);

	//---------------------------Matter P&L Report-----------------------------------------------------------------------------------------
	public InvoiceLine findByInvoiceNumberAndItemNumberAndDeletionIndicator(String invoiceNumber, Double itemNumber, Long deletionIndicator);

	//---------------------------AR-Report-----------------------------------------------------------------------------------------
	public List<InvoiceLine> findByMatterNumberInAndClassIdInAndDeletionIndicator (
			List<String> matterNumberList, List<Long> classID, Long delIndi);
	
	@Query(value="SELECT (SUM(BILL_AMOUNT) - (SELECT SUM(COALESCE((select SUM(PAYMENT_AMOUNT)\r\n"
			+ "FROM tblpaymentupdate WHERE PAYMENT_CODE='FPD' and MATTER_NO = :matterNumber\r\n"
			+ "AND PAYMENT_DATE BETWEEN :fromDate AND :toDate GROUP BY MATTER_NO), 0))))\r\n"
			+ "FROM tblinvoiceline \r\n"
			+ "WHERE MATTER_NO = :matterNumber \r\n"
			+ "AND ITEM_NO = 1 and INVOICE_NO = :invoiceNumber \r\n"
			+ "GROUP BY INVOICE_NO", nativeQuery = true)
    public Double getFeesDue(
    		@Param ("matterNumber") String matterNumber, 
    		@Param ("invoiceNumber") String invoiceNumber,
    		@Param ("fromDate") Date fromDate,
    		@Param ("toDate") Date toDate);
	
	@Query(value="SELECT (SUM(EXP_AMOUNT) - (SELECT SUM(COALESCE((SELECT SUM(PAYMENT_AMOUNT) FROM tblpaymentupdate\r\n"
			+ "WHERE PAYMENT_CODE = 'HPD' AND PAYMENT_DATE BETWEEN :fromDate AND :toDate AND MATTER_NO = :matterNumber\r\n"
			+ "GROUP BY MATTER_NO), 0))))\r\n"
			+ "FROM tblmatterexpenseid \r\n"
			+ "WHERE MATTER_NO = :matterNumber \r\n"
			+ "AND REF_FIELD_1 = :preBillNumber \r\n"
			+ "AND UPPER(REF_FIELD_5) = UPPER('HARD COST')\r\n"
			+ "GROUP BY MATTER_NO", nativeQuery = true)
    public Double getHardCostDue(
    		@Param ("matterNumber") String matterNumber, 
    		@Param ("preBillNumber") String preBillNumber,
    		@Param ("fromDate") Date fromDate,
    		@Param ("toDate") Date toDate); 
	//Prebillnumber from invoiceheader = matterexpense ref_field_1, payment_date 
	
	@Query(value="SELECT (SUM(EXP_AMOUNT) - (SELECT SUM(COALESCE((SELECT SUM(PAYMENT_AMOUNT) FROM tblpaymentupdate\r\n"
			+ "WHERE PAYMENT_CODE = 'SPD' AND PAYMENT_DATE BETWEEN :fromDate AND :toDate AND MATTER_NO = :matterNumber\r\n"
			+ "GROUP BY MATTER_NO), 0))))\r\n"
			+ "FROM tblmatterexpenseid \r\n"
			+ "WHERE MATTER_NO = :matterNumber \r\n"
			+ "AND REF_FIELD_1 = :preBillNumber \r\n"
			+ "AND UPPER(REF_FIELD_5) = UPPER('SOFT COST')\r\n"
			+ "GROUP BY MATTER_NO", nativeQuery=true)
    public Double getSoftCostDue(
    		@Param ("matterNumber") String matterNumber, 
    		@Param ("preBillNumber") String preBillNumber,
    		@Param ("fromDate") Date fromDate,
    		@Param ("toDate") Date toDate);
}