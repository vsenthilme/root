package com.mnrclara.wrapper.core.model.accounting;

import java.util.Date;

import javax.persistence.Column;

import lombok.Data;

@Data
public class ReceiptAppNotice { 
	
	private String languageId;
	private Long classId;
	private String matterNumber;
	private String receiptNo;
	private String clientId;
	private String documentType;
	private String receiptType;
	private String receiptFile;
	
	private String receiptNoticeDate;
	private String receiptDate;
	private String dateGovt;
	private String approvedOn;
	private String approvalReceiptDate;	
	private String approvalDate;
	private String expirationDate;
	private String eligibiltyDate;
	private String reminderDate;
	private String createdOn;
	
	private Long statusId;
	private String receiptNote;
	private Long reminderDays;
	private Boolean toggleNotification;
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
