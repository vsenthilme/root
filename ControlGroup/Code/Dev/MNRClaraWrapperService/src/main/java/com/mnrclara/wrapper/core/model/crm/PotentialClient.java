package com.mnrclara.wrapper.core.model.crm;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PotentialClient {
	private String languageId;
	private Long classId;
	private String potentialClientId;
	private String inquiryNumber;
	private String intakeFormNumber;
	private Long intakeFormId;
	private Long transactionId;
	private Long caseCategoryId;
	private Long clientCategoryId;
	private String firstName;
	private String lastName;
	private String firstNameLastName;
	private String lastNameFirstName;
	private Long referralId;
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
    private Date consultationDate;
    private String sConsultationDate;
	private String socialSecurityNo;
	private String mailingAddress;
	private String occupation;
	private String pcNotesNumber;
	private String agreementCode;
	private String agreementUrl;
	private Long agreementCurrentVerion;
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
    private String createdBy;
    private Date createdOn;
    private String sCreatedOn;
    private String updatedBy;
    private Date updatedOn;
}
