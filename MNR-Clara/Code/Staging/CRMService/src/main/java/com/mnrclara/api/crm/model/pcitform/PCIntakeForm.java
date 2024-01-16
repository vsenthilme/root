package com.mnrclara.api.crm.model.pcitform;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblpcintakeformid")
public class PCIntakeForm {

	@Id
	@Column(name = "IT_FORM_NO")
	private String intakeFormNumber;
	
	@Column(name = "CLASS_ID")
	private Long classId;
	
	@Column(name = "LANG_ID")
	private String languageId;
	
	@Column(name = "INQ_NO")
	private String inquiryNumber;
	
	@Column(name = "TRANS_ID")
	private Long transactionId;
	
	@Column(name = "IT_FORM_ID")
	private Long intakeFormId;	
	
	@Column(name = "STATUS_ID")
	private Long statusId;
	
	@Column(name = "EMail_ID")
	private String email;
	
	@Column(name = "ALT_EMAIL_ID")
	private String alternateEmailId;
	
	@Column(name = "IT_NOTE_NO")
	private String intakeNotesNumber;
	
	@Column(name = "SENT_ON")
    private Date sentOn;
	
	@Column(name = "RECEIVED_ON")
    private Date receivedOn;
	
	@Column(name = "RESENT_ON")
    private Date resentOn;
	
	@Column(name = "APPROVED_BY")
	private String approvedBy;
	
	@Column(name = "APPROVED_ON")
    private Date approvedOn;
	
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
	private String referenceField5;		// ContactNumber
	
	@Column(name = "REF_FIELD_6")
	private String referenceField6;		// Telephone1
	
	@Column(name = "REF_FIELD_7")
	private String referenceField7;		// Telephone2
	
	@Column(name = "REF_FIELD_8")
	private String referenceField8;		// Referral
	
	@Column(name = "REF_FIELD_9")
	private String referenceField9;
	
	@Column(name = "REF_FIELD_10")
	private String referenceField10;
	
	@Column(name = "FEEDBACK_STATUS")
	private String feedbackStatus;
	
	@Column(name = "FEEDBACK_SMS_STATUS")
	private Boolean smsStatus;
	
	@Column(name = "CTD_BY")
	private String createdBy;

	@Column(name = "CTD_ON")
    private Date createdOn;

	@Column(name = "UTD_BY")
    private String updatedBy;

	@Column(name = "UTD_ON")
	private Date updatedOn;
}
