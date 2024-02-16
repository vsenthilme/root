package com.mnrclara.api.management.model.clientgeneral;

import java.util.Date;

import lombok.Data;

@Data
public class UpdateClientGeneral {

	private String languageId;

	private Long classId;

	private String clientId;

	private Long transactionId;

	private String potentialClientId;

	private String inquiryNumber;

	private Long intakeFormId;

	private String intakeFormNumber;

	private String firstName;

	private String lastName;

	private Long clientCategoryId;

	private String firstNameLastName;

	private String lastNameFirstName;

	private String corporationClientId;

	private Long referralId;

	private String emailId;

	private String contactNumber;

	private String addressLine1;

	private String addressLine2;

	private String addressLine3;

	private String city;

	private String state;

	private String country;

	private String zipCode;

	private String consultationDate;

	private String socialSecurityNo;

	private String mailingAddress;

	private String occupation;

	private Long statusId;

	private String suiteDoorNo;

	private String workNo;

	private String homeNo;

	private String fax;

	private String alternateEmailId;

	private Boolean isMailingAddressSame;

	private Long deletionIndicator;

	private String referenceField1;

	private String referenceField2;

	private String referenceField3;

	private Date referenceField4;

	private String referenceField5;

	private String referenceField6;

	private String referenceField7;

	private String referenceField8;

	private String referenceField9;

	private String referenceField10;

	private String referenceField15;

	private String referenceField16;

	private String referenceField17;

	private String referenceField18;

	private String referenceField19;

	private String referenceField20;

	private String updatedBy;
	
	private Long sentToQB = 0L;
	
	//later added for academy report requested by client
	private String leadership;
	private String hrPartners;
	private String position;
	private String dateOfHire;
	private String academyComments;
	private String auditEmail;
}
