package com.mnrclara.wrapper.core.model.accounting;

import java.util.Date;

import javax.persistence.Column;

import lombok.Data;

@Data
public class PaymentUpdate {

	private Long paymentId;
	private String invoiceNumber;
	private String languageId;
	private Long classId;
	private String matterNumber;
	private String clientId;
	private Date postingDate;
	private Double paymentAmount;
	private String statusId;
	private Date paymentDate;
	private String paymentNumber;
	private String paymentText;
	private String transactionType;
	private String paymentCode;
	private String referenceField1;
	private String referenceField2;
	private String referenceField3;
	private String referenceField4;
	private String referenceField5;
	private String referenceField6;
	private String referenceField7;
	private String referenceField8;
	private String referenceField9;
	private String referenceField10;
	private String createdBy;
	private Date createdOn;
}
