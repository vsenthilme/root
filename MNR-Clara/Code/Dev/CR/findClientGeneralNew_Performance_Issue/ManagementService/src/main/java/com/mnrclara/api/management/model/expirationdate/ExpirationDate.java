package com.mnrclara.api.management.model.expirationdate;

import java.util.Date;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
/*
 * `LANG_ID`, `CLASS_ID`, `MATTER_NO`, `CLIENT_ID`, `DOC_TYPE`
 */
@Table(
		name = "tblexpirationdate", 
		uniqueConstraints = { 
				@UniqueConstraint (
						name = "unique_key_expirationdate", 
						columnNames = { "LANG_ID", "CLASS_ID", "MATTER_NO", "CLIENT_ID", "DOC_TYPE"})
				}
		)
@IdClass(ExpirationDateCompositeKey.class)
public class ExpirationDate {

    @Id
	@Column(name = "CLIENT_ID") 
	private String clientId;
	
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
	@Column(name = "DOC_TYPE") 
	private String documentType;
	
	@Column(name = "STATUS_ID") 
	private Long statusId;
	
	@Column(name = "APPROVAL_DATE") 
	private Date approvalDate;
	
	@Column(name = "EXPIRATION_DATE") 
	private Date expirationDate;
	
	@Column(name = "ELIGIBILITY_DATE") 
	private Date eligibilityDate;
	
	@Column(name = "REMINDER_DAYS") 
	private Long reminderDays;
	
	@Column(name = "REMINDER_DATE") 
	private Date reminderDate;
	
	@Column(name = "REMINDER_TEXT") 
	private String reminderDescription;
	
	@Column(name = "TOGGLE_NOTIFICATION") 
	private Long toggleNotification;

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

	@Transient
	private String corporationName;

	@Transient
	private String className;

	@Transient
	private String clientName;

	@Transient
	private String clientEmail;

	@Transient
	private String matterDescription;

}
