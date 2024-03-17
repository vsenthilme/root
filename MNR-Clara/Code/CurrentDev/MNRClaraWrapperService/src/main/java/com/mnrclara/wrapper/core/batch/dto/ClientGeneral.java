package com.mnrclara.wrapper.core.batch.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ClientGeneral { 
	
	private String languageId;
	private Long classId;
	private String clientId;
	private Long clientCategoryId;
	private Long transactionId;
	private String potentialClientId;
	private String inquiryNumber;
	private Long intakeFormId;
	private String intakeFormNumber;
	private String firstName;
	private String lastName;
	private String firstNameLastName;
	private String lastNameFirstName;
	private String corporationClientId;
	private String referralId;
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
	private String referenceField21;
	private String referenceField22;
	private String referenceField23;
	private String referenceField25;
	private String referenceField26;
	private String referenceField27;
	private String referenceField28;
	private String createdBy;
	private Date createdOn;
	private String UpdatedBy;
	private Date updatedOn;
	
	/**
	 * 
	* @param languageId
	* @param classId
	* @param clientId
	* @param clientCategoryId
	* @param transactionId
	* @param potentialClientId
	* @param inquiryNumber
	* @param intakeFormId
	* @param intakeFormNumber
	* @param firstName
	* @param lastName
	* @param firstNameLastName
	* @param lastNameFirstName
	* @param corporationClientId
	* @param referralId
	* @param emailId
	* @param contactNumber
	* @param addressLine1
	* @param addressLine2
	* @param addressLine3
	* @param city
	* @param state
	* @param country
	* @param zipCode
	* @param consultationDate
	* @param socialSecurityNo
	* @param mailingAddress
	* @param occupation
	* @param statusId
	* @param suiteDoorNo
	* @param workNo
	* @param homeNo
	* @param fax
	* @param alternateEmailId
	* @param isMailingAddressSame
	* @param deletionIndicator
	* @param referenceField1
	* @param referenceField2
	* @param referenceField3
	* @param referenceField4
	* @param referenceField5
	* @param referenceField6
	* @param referenceField7
	* @param referenceField8
	* @param referenceField9
	* @param referenceField10
	* @param referenceField11
	* @param referenceField12
	* @param referenceField13
	* @param referenceField14
	* @param referenceField15
	* @param referenceField16
	* @param referenceField17
	* @param referenceField18
	* @param referenceField19
	* @param referenceField20
	* @param referenceField21
	* @param referenceField22
	* @param referenceField23
	* @param referenceField25
	* @param referenceField26
	* @param referenceField27
	* @param createdBy
	* @param createdOn
	* @param UpdatedBy
	* @param updatedOn
	* @param sentToQB
	 */
	 
	public ClientGeneral (String languageId, Long classId, String clientId, Long clientCategoryId, Long transactionId, 
			String potentialClientId, String inquiryNumber, Long intakeFormId, String intakeFormNumber, String firstName, 
			String lastName, String firstNameLastName, String lastNameFirstName, String corporationClientId, String referralId, 
			String emailId, String contactNumber, String addressLine1, String addressLine2, String addressLine3, String city, 
			String state, String country, String zipCode, String consultationDate, String socialSecurityNo, String mailingAddress, 
			String occupation, Long statusId, String suiteDoorNo, String workNo, String homeNo, String fax, String alternateEmailId, 
			Boolean isMailingAddressSame, Long deletionIndicator, String referenceField21, String referenceField22,
			String referenceField23, String referenceField25, String referenceField26, String referenceField27, 
			String referenceField28, String createdBy, Date createdOn, String UpdatedBy, Date updatedOn) {
		this.languageId = languageId;
		this.classId = classId;
		this.clientId = clientId;
		this.clientCategoryId = clientCategoryId;
		this.transactionId = transactionId;
		this.potentialClientId = potentialClientId;
		this.inquiryNumber = inquiryNumber;
		this.intakeFormId = intakeFormId;
		this.intakeFormNumber = intakeFormNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.firstNameLastName = firstNameLastName;
		this.lastNameFirstName = lastNameFirstName;
		this.corporationClientId = corporationClientId;
		this.referralId = referralId;
		this.emailId = emailId;
		this.contactNumber = contactNumber;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.addressLine3 = addressLine3;
		this.city = city;
		this.state = state;
		this.country = country;
		this.zipCode = zipCode;
		this.consultationDate = consultationDate;
		this.socialSecurityNo = socialSecurityNo;
		this.mailingAddress = mailingAddress;
		this.occupation = occupation;
		this.statusId = statusId;
		this.suiteDoorNo = suiteDoorNo;
		this.workNo = workNo;
		this.homeNo = homeNo;
		this.fax = fax;
		this.alternateEmailId = alternateEmailId;
		this.isMailingAddressSame = isMailingAddressSame;
		this.deletionIndicator = deletionIndicator;
		this.referenceField21 = referenceField21;
		this.referenceField22 = referenceField22;
		this.referenceField23 = referenceField23;
		this.referenceField25 = referenceField25;
		this.referenceField26 = referenceField26;
		this.referenceField27 = referenceField27;
		this.referenceField28 = referenceField28;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.UpdatedBy = UpdatedBy;
		this.updatedOn = updatedOn;
	}
}
