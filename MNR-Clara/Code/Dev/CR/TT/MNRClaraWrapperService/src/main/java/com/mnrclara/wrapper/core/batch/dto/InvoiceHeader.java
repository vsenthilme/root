package com.mnrclara.wrapper.core.batch.dto;

import java.util.Date;

import lombok.Data;

@Data
public class InvoiceHeader {

	private String languageId;
	private Long classId;
	private String matterNumber;
	private String clientId;
	private String invoiceNumber;
	private String invoiceFiscalYear;
	private String invoicePeriod;
	private Date postingDate;
	private String referenceText;
	private String partnerAssigned;
	private Date invoiceDate;
	private Double invoiceAmount;
	private Double totalPaidAmount;
	private Double remainingBalance;
	private Long statusId;
	private Long deletionIndicator;
	private String createdBy;
	private Date createdOn;

	/**
	 * 
	 * @param languageId
	 * @param classId
	 * @param matterNumber
	 * @param clientId
	 * @param invoiceNumber
	 * @param invoiceFiscalYear
	 * @param invoicePeriod
	 * @param postingDate
	 * @param referenceText
	 * @param invoiceDate
	 * @param invoiceAmount
	 * @param totalPaidAmount
	 * @param remainingBalance
	 * @param statusId
	 * @param deletionIndicator
	 * @param createdBy
	 * @param createdOn
	 */
	public InvoiceHeader(String languageId, Long classId, String matterNumber, String clientId, String invoiceNumber,
			String invoiceFiscalYear, String invoicePeriod, Date postingDate, String referenceText, String partnerAssigned, Date invoiceDate,
			Double invoiceAmount, Double totalPaidAmount, Double remainingBalance, Long statusId,
			Long deletionIndicator, String createdBy, Date createdOn) {
		this.invoiceNumber = invoiceNumber;
		this.languageId = languageId;
		this.classId = classId;
		this.matterNumber = matterNumber;
		this.clientId = clientId;
		this.invoiceFiscalYear = invoiceFiscalYear;
		this.invoicePeriod = invoicePeriod;
		this.postingDate = postingDate;
		this.referenceText = referenceText;
		this.partnerAssigned = partnerAssigned;
		this.invoiceDate = invoiceDate;
		this.invoiceAmount = invoiceAmount;
		this.totalPaidAmount = totalPaidAmount;
		this.remainingBalance = remainingBalance;
		this.statusId = statusId;
		this.deletionIndicator = deletionIndicator;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}
}
