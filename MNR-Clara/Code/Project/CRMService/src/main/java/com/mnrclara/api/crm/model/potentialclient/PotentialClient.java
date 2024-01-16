package com.mnrclara.api.crm.model.potentialclient;

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
@Table(name = "tblpotentialclientid")
public class PotentialClient {

	@Column(name = "LANG_ID")
	private String languageId;
	
	@Column(name = "CLASS_ID")
	private Long classId;
	
	@Id
	@Column(name = "POT_CLIENT_ID")
	private String potentialClientId;
	
	@Column(name = "INQ_NO")
	private String inquiryNumber;
	
	@Column(name = "IT_FORM_NO")
	private String intakeFormNumber;
	
	@Column(name = "IT_FORM_ID")
	private Long intakeFormId;
	
	@Column(name = "TRANS_ID")
	private Long transactionId;
	
	@Column(name = "CASE_CATEGORY_ID")
	private Long caseCategoryId;
	
	@Column(name = "CLIENT_CAT_ID")
	private Long clientCategoryId;
	
	@Column(name = "FIRST_NM")
	private String firstName;
	
	@Column(name = "LAST_NM")
	private String lastName;
	
	@Column(name = "FIRST_LAST_NM")
	private String firstNameLastName;
	
	@Column(name = "LAST_FIRST_NM")
	private String lastNameFirstName;

	@Column(name = "REFERRAL_ID")
	private Long referralId;
	
	@Column(name = "EMail_ID")
	private String emailId;
	
	@Column(name = "CONT_NO")
	private String contactNumber;
									
	@Column(name = "ALT_PHONE_1")
	private String alternateTelephone1;
	
	@Column(name = "ALT_PHONE_2")
	private String alternateTelephone2;
	
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
	
	@Column(name = "PC_NOTE_NO")
	private String pcNotesNumber;
	
	@Column(name = "AGREEMENT_CODE")
	private String agreementCode;
	
	@Column(name = "AGREEMENT_URL")
	private String agreementUrl;
	
	@Column(name = "AGREEMENT_URL_VER")
	private Long agreementCurrentVerion = 0L;
	
	@Column(name = "STATUS_ID")
	private Long statusId;
	
	@Column(name = "IS_DELETED")
	private Long deletionIndicator = 0L;
	
	@Column(name = "REF_FIELD_1")
	private Date referenceField1;
	
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
	
	@Column(name = "CTD_BY")
	private String createdBy;

	@Column(name = "CTD_ON")
    private Date createdOn = new Date();

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
}
