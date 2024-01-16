package com.mnrclara.wrapper.core.model.accounting;

import java.util.Date;

import lombok.Data;

@Data
public class ReceiptAppNoticeEntity { 
	
	private String languageId;
	private Long classId;
	private String matterNumber;
	private String receiptNo;
	private String clientId;
	private String documentType;
	private String receiptType;
	private String receiptFile;
	
	private Date receiptNoticeDate;
	private Date receiptDate;
	private Date dateGovt;
	private Date approvedOn;
	private Date approvalReceiptDate;	
	private Date approvalDate;
	private Date expirationDate;
	private Date eligibiltyDate;
	private Date reminderDate;
	private Date createdOn;
	
	private Long statusId;
	private String receiptNote;
	private Long reminderDays;
	private Long deletionIndicator = 0L;
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
	private String updatedBy;
	private Date updatedOn;
}
