package com.mnrclara.api.crm.model.inquiry;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblinquiryid")
public class Inquiry {

	@Id
	@Column(name = "INQ_NO")
	private String inquiryNumber;
	
	@Column(name = "CLASS_ID")
	private Long classId;
	
	@Column(name = "TRANS_ID")
	private Long transactionId;
	
	@Column(name = "LANG_ID")
	private String languageId;
	
	@Column(name = "EMail_ID")
	private String email;
	
	@Column(name = "ALT_EMAIL_ID")
	private String alternateEmail;
	
	@Column(name = "CONT_NO")
	private String contactNumber;
	
	@Column(name = "INQ_MODE_ID")
	private Long inquiryModeId;
	
	@Column(name = "INQ_DATE")
	private Date inquiryDate = new Date();
	
	@Column(name = "FIRST_NM")
	private String firstName;
		
	@Column(name = "LAST_NM")
	private String lastName;
	
	@Column(name = "STATUS_ID")
	private Long statusId;
	
	@Column(name = "INQ_NOTE_NO")
	private String inquiryNotesNumber;
	
	@Column(name = "ASSIGNED_USR_ID")
	private String assignedUserId;
	
	@Column(name = "IS_DELETED")
	private Long deletionIndicator = 0L;
		
	@Column(name = "IT_FORM_ID")
	private Long intakeFormId;
	
	@Column(name = "IT_NOTE_NO")
	private String intakeNotesNumber;
	
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
	
	@Column(name = "REF_FIELD_11")
	private String referenceField11;
	
	@JsonIgnore
	@Column(name = "ASSIGN_BY")
	private String assignedBy = "SUPERADMIN";

	@JsonIgnore
	@Column(name = "ASSIGN_ON")
    private Date assignedOn = new Date();
	
	@Column(name = "CTD_BY")
	private String createdBy;

	@Column(name = "CTD_ON")
    private Date createdOn = new Date();

	@Column(name = "UTD_BY")
    private String updatedBy;

	@Column(name = "UTD_ON")
	private Date updatedOn = new Date();
}
