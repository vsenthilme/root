package com.mnrclara.wrapper.core.batch.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PaymentUpdate {

	private Long paymentId;
	private String invoiceNumber;
	private Long classId;
	private String clientId;
	private String createdBy;
	private Date createdOn;
	private String languageId;
	private String matterNumber;
	private Double paymentAmount;
	private Date paymentDate;
	private String paymentNumber;
	private String paymentText;
	private Date postingDate;
	private String statusId;
	private String transactionType;
	private String paymentCode;

	/**
	 * 
	 * @param invoiceNumber
	 * @param classId
	 * @param clientId
	 * @param createdBy
	 * @param createdOn
	 * @param languageId
	 * @param matterNumber
	 * @param paymentAmount
	 * @param paymentDate
	 * @param paymentNumber
	 * @param paymentText
	 * @param postingDate
	 * @param statusId
	 * @param transactionType
	 * @param paymentCode
	 */
	public PaymentUpdate(Long paymentId, String invoiceNumber, Long classId, String clientId, String createdBy, Date createdOn,
			String languageId, String matterNumber, Double paymentAmount, Date paymentDate, String paymentNumber,
			String paymentText, Date postingDate, String statusId, String transactionType, String paymentCode) {
		this.paymentId = paymentId;
		this.invoiceNumber = invoiceNumber;
		this.classId = classId;
		this.clientId = clientId;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.languageId = languageId;
		this.matterNumber = matterNumber;
		this.paymentAmount = paymentAmount;
		this.paymentDate = paymentDate;
		this.paymentNumber = paymentNumber;
		this.paymentText = paymentText;
		this.postingDate = postingDate;
		this.transactionType = transactionType;
		this.paymentCode = paymentCode;
		this.statusId = statusId;
	}
}
