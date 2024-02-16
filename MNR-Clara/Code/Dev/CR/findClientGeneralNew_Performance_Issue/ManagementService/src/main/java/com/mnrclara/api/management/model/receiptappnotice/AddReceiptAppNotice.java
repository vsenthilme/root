package com.mnrclara.api.management.model.receiptappnotice;

import java.util.Date;

import lombok.Data;

@Data
public class AddReceiptAppNotice {
	
	private String documentType;
	private String receiptNo;
	private String receiptType;
	private Date receiptDate;
	private Date receiptNoticeDate;
	private Date dateGovt;
	private Long statusId;
	private String receiptNote;
	private String matterNumber;
	
	private String languageId;
	private Long classId;
	private String clientId;
	private String receiptFile;
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
}
