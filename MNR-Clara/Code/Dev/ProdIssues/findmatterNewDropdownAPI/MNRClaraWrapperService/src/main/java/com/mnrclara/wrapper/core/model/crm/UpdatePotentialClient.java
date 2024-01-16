package com.mnrclara.wrapper.core.model.crm;

import java.util.Date;

import javax.validation.constraints.Email;

import lombok.Data;

@Data
public class UpdatePotentialClient {
	private Long classId;
	private String inquiryNumber;
	private Long intakeFormNumber;
	private Long intakeFormId;
	private Long caseCategoryId;
	private Long clientCategoryId;
	private String firstName;
	private String lastName;
	private String firstNameLastName;
	private String lastNameFirstName;
	private Long referralId;
	
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
	private String pcNotesNumber;
	private String agreementCode;
	private String agreementUrl;
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

	//later added for academy report requested by client
	private String leadership;
	private String hrPartners;
	private String position;
	private String dateOfHire;
	private String academyComments;
	private String auditEmail;
}
