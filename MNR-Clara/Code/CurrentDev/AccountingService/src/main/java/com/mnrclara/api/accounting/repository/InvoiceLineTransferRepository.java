package com.mnrclara.api.accounting.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.accounting.model.invoice.InvoiceLine;
import com.mnrclara.api.accounting.model.invoice.InvoiceLineTransfer;


@Repository
@Transactional
public interface InvoiceLineTransferRepository extends JpaRepository<InvoiceLineTransfer,Long>, JpaSpecificationExecutor<InvoiceLineTransfer> {
	
	/**
	 * 
	 */
	public List<InvoiceLineTransfer> findAll();
	
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
	public Optional<InvoiceLineTransfer> 
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

	public InvoiceLineTransfer findByInvoiceFiscalYear(String invoiceFiscalYear);
	
	public List<InvoiceLineTransfer> findByInvoiceNumber(String invoiceNumber);
	
	public InvoiceLineTransfer findByInvoiceNumberAndItemNumber(String invoiceNumber, Long itemNumber);
	
//	@Query(value="SELECT SUM(BILL_AMOUNT) FROM tblinvoiceline\r\n"
//			+ "WHERE MATTER_NO = :mtterNumber AND ITEM_NO = :itemNumber\r\n"
//			+ "GROUP BY MATTER_NO", nativeQuery=true)
//    public Double getBilledAmount(@Param ("mtterNumber") String mtterNumber, 
//    		@Param ("itemNumber") Long itemNumber );
//	
//	//---------------------------TransferBilling-----------------------------------------------------------------------------------------
//	@Modifying
//	@Query (value = "UPDATE MNRCLARA.tblinvoiceline set STATUS_ID = 59, IS_DELETED = 1 \r\n"
//			+ " WHERE INVOICE_NO = :invoiceNumber", nativeQuery = true)
//	public void deleteInvoiceLine(@Param(value = "invoiceNumber") String invoiceNumber);
}