package com.mnrclara.wrapper.core.model.datamigration;

import com.opencsv.bean.CsvBindByPosition;

import lombok.Data;

@Data
public class ClientGeneral {

	@CsvBindByPosition(position = 0)
	private String languageId;
	
	@CsvBindByPosition(position = 1)
	private Long classId;
	
	@CsvBindByPosition(position = 2)
	private String clientId;
	
	@CsvBindByPosition(position = 3)
	private Long clientCategoryId;
	
	@CsvBindByPosition(position = 4)
	private Long transactionId;
	
	@CsvBindByPosition(position = 5)
	private String potentialClientId;
	
	@CsvBindByPosition(position = 6)
	private String inquiryNumber;
	
	@CsvBindByPosition(position = 7)
	private Long intakeFormId;
	
	@CsvBindByPosition(position = 8)
	private String intakeFormNumber;
	
	@CsvBindByPosition(position = 9)
	private String firstName;
	
	@CsvBindByPosition(position = 10)
	private String lastName;
	
	@CsvBindByPosition(position = 11)
	private String firstNameLastName;
	
	@CsvBindByPosition(position = 12)
	private String lastNameFirstName;
	
	@CsvBindByPosition(position = 13)
	private String corporationClientId;
	
	@CsvBindByPosition(position = 14)
	private Long referralId;
	
	@CsvBindByPosition(position = 15)
	private String emailId;
	
	@CsvBindByPosition(position = 16)
	private String contactNumber;
	
	@CsvBindByPosition(position = 17)
	private String addressLine1;
	
	@CsvBindByPosition(position = 18)
	private String addressLine2;
	
	@CsvBindByPosition(position = 19)
	private String addressLine3;
	
	@CsvBindByPosition(position = 20)
	private String city;
	
	@CsvBindByPosition(position = 21)
	private String state;
	
	@CsvBindByPosition(position = 22)
	private String country;
	
	@CsvBindByPosition(position = 23)
	private String zipCode;
	
	@CsvBindByPosition(position = 24)
	private String consultationDate;
	
	@CsvBindByPosition(position = 25)
	private String socialSecurityNo;
	
	@CsvBindByPosition(position = 26)
	private String mailingAddress;
	
	@CsvBindByPosition(position = 27)
	private String occupation;
	
	@CsvBindByPosition(position = 28)
	private Long statusId;
	
	@CsvBindByPosition(position = 29)
	private String suiteDoorNo;
	
	@CsvBindByPosition(position = 30)
	private String workNo;
	
	@CsvBindByPosition(position = 31)
	private String homeNo;
	
	@CsvBindByPosition(position = 32)
	private String fax;
	
	@CsvBindByPosition(position = 33)
	private String alternateEmailId;
	
	@CsvBindByPosition(position = 34)
	private Boolean isMailingAddressSame;
	
	@CsvBindByPosition(position = 35)
	private Long deletionIndicator;
	
	@CsvBindByPosition(position = 36)
	private String createdBy;
	
	@CsvBindByPosition(position = 37)
	private String createdOn;
	
	@CsvBindByPosition(position = 38)
	private String updatedBy;
	
	@CsvBindByPosition(position = 39)
	private String updatedOn;
	
	@CsvBindByPosition(position = 40)
	private String referenceField21;
	
	@CsvBindByPosition(position = 41)
	private String referenceField22;
	
	@CsvBindByPosition(position = 42)
	private String referenceField23;
	
	@CsvBindByPosition(position = 43)
	private String referenceField24;
	
	@CsvBindByPosition(position = 44)
	private String referenceField25;
	
	@CsvBindByPosition(position = 45)
	private String referenceField26;
	
	@CsvBindByPosition(position = 46)
	private String referenceField27;
}
