package com.mnrclara.api.management.model.caseinfosheet;

import java.util.Date;

import lombok.Data;

@Data
public class ImmCaseInfoSheet extends BaseCaseInfoSheet {

	private String languageId;
	private Long classId;
	private String clientId;
	private String potentialClientId;
	private Long transactionId;
	private String inquiryNumber;
	private Long caseCategoryId;
	private Long caseSubCategoryId;
	private String matterNumber;
	private String intakeFormNumber;
	private Long intakeFormId;
	private String firstNameLastName;
	private String typeOfMatter;
	private String matterDescription;
	private String title;
	private String emailId;
	private String companyName;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String country;
	private String zipCode;
	private String contactNumber;
	private String workNo;
	private String homeNo;
	private String fax;
	private String locationOfFile;
	private String nameOfEr;
	private String alternateEmailId;
	private String alternateTelephone1;
	private String alienNumber;
	private String countryOfBirth;
	private String dateOfBirth;
	private Long statusId;
	private Long deletionIndicator;
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
	private String referenceField11;
	private String referenceField12;
	private String referenceField13;
	private String referenceField14;
	private String referenceField15;
	private String referenceField16;
	private String referenceField17;
	private String referenceField18;
	private String referenceField19;
	private String referenceField20;
	private String createdBy;
	private Date createdOn = new Date();
	private String UpdatedBy;
	private Date updatedOn = new Date();
}
