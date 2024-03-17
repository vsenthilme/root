package com.mnrclara.wrapper.core.model.crm;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Agreement {

	private String languageId = "EN";
	private Long classId;
	private String agreementCode;
	private String clientId;
	private String potentialClientId;
	private String inquiryNumber;
	private String agreementURL;
	private String agreementURLVersion;
	private Long caseCategoryId;
	private Long statusId;
	private Long transactionId;
	private String emailId;
    private Date sentOn;
    private Date receivedOn;
    private Date resentOn;
    private Date approvedOn;
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
    private String updatedBy;
    private Date updatedOn;
}
