package com.mnrclara.wrapper.core.batch.dto;

import java.util.Date;

import lombok.Data;

@Data
public class InvoiceLine {

	private String languageId;
	private Long classId;
	private String matterNumber;
	private String clientId;
	private String invoiceNumber;
	private String serialNumber;
	private String invoiceFiscalYear;
	private String invoicePeriod;
	private Long itemNumber;
	private String invoicedetailDescription;
	private Double billableAmount;
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
	 * @param serialNumber
	 * @param invoiceFiscalYear
	 * @param invoicePeriod
	 * @param itemNumber
	 * @param invoicedetailDescription
	 * @param billableAmount
	 * @param statusId
	 * @param deletionIndicator
	 * @param createdBy
	 * @param createdOn
	 */
	public InvoiceLine(String languageId, Long classId, String matterNumber, String clientId, String invoiceNumber,
			String serialNumber, String invoiceFiscalYear, String invoicePeriod, Long itemNumber,
			String invoicedetailDescription, Double billableAmount, Long statusId, Long deletionIndicator,
			String createdBy, Date createdOn) {
		this.languageId = languageId;
		this.classId = classId;
		this.matterNumber = matterNumber;
		this.clientId = clientId;
		this.invoiceNumber = invoiceNumber;
		this.invoiceFiscalYear = invoiceFiscalYear;
		this.invoicePeriod = invoicePeriod;
		this.serialNumber = serialNumber;
		this.itemNumber = itemNumber;
		this.invoicedetailDescription = invoicedetailDescription;
		this.billableAmount = billableAmount;
		this.statusId = statusId;
		this.deletionIndicator = deletionIndicator;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}
}
