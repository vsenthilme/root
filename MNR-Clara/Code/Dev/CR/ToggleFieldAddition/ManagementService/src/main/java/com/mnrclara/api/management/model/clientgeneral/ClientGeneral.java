package com.mnrclara.api.management.model.clientgeneral;

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
@Table(name = "tblclientgeneralid")
public class ClientGeneral {

	@Id
	@Column(name = "CLIENT_ID")
	private String clientId;
	
	@JsonIgnore
	@Column(name = "SENT_TO_QB")
	private Long sentToQB = 0L;
	
	@JsonIgnore
	@Column(name = "SENT_TO_DKTW")
	private Long sentToDocketwise = 0L;

	@Column(name = "LANG_ID")
	private String languageId;

	@Column(name = "CLASS_ID")
	private Long classId;

	@Column(name = "CLIENT_CAT_ID")
	private Long clientCategoryId;

	@Column(name = "TRANS_ID")
	private Long transactionId;

	@Column(name = "POT_CLIENT_ID")
	private String potentialClientId;

	@Column(name = "INQ_NO")
	private String inquiryNumber;

	@Column(name = "IT_FORM_ID")
	private Long intakeFormId;

	@Column(name = "IT_FORM_NO")
	private String intakeFormNumber;

	@Column(name = "FIRST_NM")
	private String firstName;

	@Column(name = "LAST_NM")
	private String lastName;

	@Column(name = "FIRST_LAST_NM")
	private String firstNameLastName;

	@Column(name = "LAST_FIRST_NM")
	private String lastNameFirstName;

	@Column(name = "CORP_CLIENT_ID")
	private String corporationClientId;

	@Column(name = "REFERRAL_ID")
	private Long referralId;

	@Column(name = "EMail_ID")
	private String emailId;

	@Column(name = "CONT_NO")
	private String contactNumber;

	@Column(name = "ADDRESS_LINE1")
	private String addressLine1;

	@Column(name = "ADDRESS_LINE2")
	private String addressLine2;

	@Column(name = "ADDRESS_LINE3")
	private String addressLine3;

	@Column(name = "CITY")
	private String city;

	@Column(name = "STATE")
	private String state;

	@Column(name = "COUNTRY")
	private String country;

	@Column(name = "ZIP_CODE")
	private String zipCode;

	@Column(name = "CONSULT_DATE")
	private String consultationDate;

	@Column(name = "SSN_ID")
	private String socialSecurityNo;

	@Column(name = "MAIL_ADDRESS")
	private String mailingAddress;

	@Column(name = "OCCUPATION")
	private String occupation;

	@Column(name = "STATUS_ID")
	private Long statusId;

	@Column(name = "SUITE_NO")
	private String suiteDoorNo;

	@Column(name = "WORK")
	private String workNo;

	@Column(name = "HOME")
	private String homeNo;

	@Column(name = "FAX")
	private String fax;

	@Column(name = "ALT_EMAIL_ID")
	private String alternateEmailId;

	@Column(name = "IS_MAIL_SAME")
	private Boolean isMailingAddressSame;

	@Column(name = "IS_DELETED")
	private Long deletionIndicator;

	@Column(name = "REF_FIELD_1")
	private String referenceField1;

	@Column(name = "REF_FIELD_2")
	private String referenceField2;

	@Column(name = "REF_FIELD_3")
	private String referenceField3;

	@Column(name = "REF_FIELD_4")
	private Date referenceField4;

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
	
	@Column(name = "REF_FIELD_12")
	private String referenceField12;
	
	@Column(name = "REF_FIELD_13")
	private String referenceField13;
	
	@Column(name = "REF_FIELD_14")
	private String referenceField14;
	
	@Column(name = "REF_FIELD_15")
	private String referenceField15;
	
	@Column(name = "REF_FIELD_16")
	private String referenceField16;
	
	@Column(name = "REF_FIELD_17")
	private String referenceField17;
	
	@Column(name = "REF_FIELD_18")
	private String referenceField18;
	
	@Column(name = "REF_FIELD_19")
	private String referenceField19;
	
	@Column(name = "REF_FIELD_20")
	private String referenceField20;
	
	@Column(name = "REF_FIELD_21")
	private String referenceField21;

	@Column(name = "REF_FIELD_22")
	private String referenceField22;

	@Column(name = "REF_FIELD_23")
	private String referenceField23;

	@Column(name = "REF_FIELD_24")
	private Date referenceField24;

	@Column(name = "REF_FIELD_25")
	private String referenceField25;

	@Column(name = "REF_FIELD_26")
	private String referenceField26;

	@Column(name = "REF_FIELD_27")
	private String referenceField27;
	
	@Column(name = "REF_FIELD_28")
	private String referenceField28;
	
	@Column(name = "REF_FIELD_29")
	private String referenceField29;
	
	@Column(name = "REF_FIELD_30")
	private String referenceField30;

	@Column(name = "CTD_BY")
	private String createdBy;

	@Column(name = "CTD_ON")
	private Date createdOn = new Date();
	
	private String createdOnString;

	@Column(name = "UTD_BY")
	private String updatedBy;

	@Column(name = "UTD_ON")
	private Date updatedOn = new Date();
	
	//later added for academy report requested by client
	@Column(name = "LEADERSHIP")
	private String leadership;

	@Column(name = "HR_PARTNERS")
	private String hrPartners;

	@Column(name = "POSITION")
	private String position;

	@Column(name = "DATE_OF_HIRE")
	private String dateOfHire;

	@Column(name = "ACADEMY_COMMENTS", columnDefinition = "nvarchar(500)")
	private String academyComments;

	@Column(name = "AUDIT_EMAIL")
	private String auditEmail;
	
}
