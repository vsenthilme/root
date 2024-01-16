package com.mnrclara.api.management.model.receiptappnotice;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
/*
 * `LANG_ID`, `CLASS_ID`, `MATTER_NO`, `RECEIPT_NO`
 */
@Table(
		name = "tblreceiptappnotice", 
		uniqueConstraints = { 
				@UniqueConstraint (
						name = "unique_key_receiptappnotice", 
						columnNames = { "LANG_ID" , "CLASS_ID" , "MATTER_NO" , "RECEIPT_NO"})
				}
		)
@IdClass(ReceiptAppNoticeCompositeKey.class)
public class ReceiptAppNotice { 
	
	@Id
	@Column(name = "LANG_ID") 
	private String languageId;
	
	@Id
	@Column(name = "CLASS_ID")
	private Long classId;
	
	@Id
	@Column(name = "MATTER_NO") 
	private String matterNumber;
	
	@Id
	@Column(name = "RECEIPT_NO") 
	private String receiptNo;
	
	@Column(name = "CLIENT_ID")
	private String clientId;
	
	@Column(name = "DOC_TYPE") 
	private String documentType;
	
	@Column(name = "RECEIPT_TYPE")
	private String receiptType;
	
	@Column(name = "DATE_GOVT")
	private Date dateGovt;
	
	@Column(name = "RECEIPT_DATE")
	private Date receiptDate;
	
	@Column(name = "RECEIPT_FILE") 
	private String receiptFile;
	
	@Column(name = "RECEIPT_NOTICE_DATE") 
	private Date receiptNoticeDate;
	
	@Column(name = "STATUS_ID") 
	private Long statusId;
	
	@Column(name = "RECEIPT_TEXT")
	private String receiptNote;
	
	@Column(name = "APPROVED_ON") 
	private Date approvedOn;
	
	@Column(name = "APP_REC_DATE")
	private Date approvalReceiptDate;
	
	@Column(name = "APPROVAL_DATE")
	private Date approvalDate;
	
	@Column(name = "EXPIRATION_DATE") 
	private Date expirationDate;
	
	@Column(name = "ELIGIBILITY_DATE") 
	private Date eligibiltyDate;
	
	@Column(name = "REMINDER_DAYS")
	private Long reminderDays;
	
	@Column(name = "REMINDER_DATE") 
	private Date reminderDate;
	
	@Column(name = "TOGGLE_NOTIFICATION") 
	private Boolean toggleNotification;
	
	@Column(name = "IS_DELETED") 
	private Long deletionIndicator = 0L;
	
	@Column(name = "REF_FIELD_1") 
	private String referenceField1;
	
	@Column(name = "REF_FIELD_2")
	private String referenceField2;
	
	@Column(name = "REF_FIELD_3")
	private String referenceField3;
	
	@Column(name = "REF_FIELD_4")
	private String referenceField4;
	
	@Column(name = "REF_FIELD_5")
	private String referenceField5;
	
	@Column(name = "REF_FIELD_6") 
	private String referenceField6;
	
	@Column(name = "REF_FIELD_7")
	private String referenceField7;
	
	@Column(name = "REF_FIELD_8")
	private String referenceField8;
	
	@Column(name = "REF_FIELD_9")
	private String referenceField9;
	
	@Column(name = "REF_FIELD_10")
	private String referenceField10;
	
	@Column(name = "CTD_BY")
	private String createdBy;	
	
	@Column(name = "CTD_ON")
	private Date createdOn = new Date();
	
	@Column(name = "UTD_BY") 
	private String updatedBy;
	
	@Column(name = "UTD_ON") 
	private Date updatedOn = new Date();
}
