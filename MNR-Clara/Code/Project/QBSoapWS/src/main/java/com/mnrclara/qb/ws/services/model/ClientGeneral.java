package com.mnrclara.qb.ws.services.model;

import lombok.Data;

@Data
public class ClientGeneral {

	private String languageId;
	private Long classId;
	private String clientId;
	private Long clientCategoryId;
	private String firstName;
	private String lastName;
	private String firstNameLastName;
	private String lastNameFirstName;
	private String corporationClientId;
	private String emailId;
	private String contactNumber;
	private String addressLine1;
	private String city;
	private String state;
	private String country;
	private String zipCode;
	private String consultationDate;
	private String socialSecurityNo;
	private String mailingAddress;
	private String occupation;
	private String suiteDoorNo;
	private String workNo;
	private String homeNo;
	private String fax;
	private String alternateEmailId;
	private Boolean isMailingAddressSame;
	private String referenceField1;
	private String referenceField16;
	private String referenceField17;
	private String referenceField18;
	private String referenceField19;
	private String referenceField20;
	private Long sentToQB = 0L;
}
