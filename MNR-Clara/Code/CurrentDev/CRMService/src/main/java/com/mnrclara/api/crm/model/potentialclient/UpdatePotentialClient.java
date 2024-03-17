package com.mnrclara.api.crm.model.potentialclient;

import javax.validation.constraints.Email;

import lombok.Data;

@Data
public class UpdatePotentialClient {
	private Long classId;
	private String inquiryNumber;
	private Long intakeFormNumber;
	private Long intakeFormId;
	private Long caseCategoryId;
	private String firstName;
	private String lastName;
	private String firstNameLastName;
	private String lastNameFirstName;
	private Long referralId;
	private Long clientCategoryId;
	
	@Email (message = "Please enter correct Email ID.")
	private String emailId;
	private String contactNumber;
	private String alternateTelephone1;
	private String alternateTelephone2;
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
	private String agreementCode;
	
	private String agreementUrl;
	private String pcNotesNumber;
	private Long statusId;
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
