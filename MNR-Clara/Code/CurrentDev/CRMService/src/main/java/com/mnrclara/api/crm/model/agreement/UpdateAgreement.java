package com.mnrclara.api.crm.model.agreement;

import java.util.Date;

import javax.validation.constraints.Email;

import lombok.Data;

@Data
public class UpdateAgreement {

	private Long classId;
	private String clientId;
	private String potetnialClientId;
	private String inquiryNumber;
	private String agreementURL;
	private String agreementURLVersion;
	private Long caseCategoryId;
	private Long statusId;
	
	@Email (message = "Please enter correct Email ID.")
	private String emailId;
	
    private Date sentDate;
    private Date receivedDate;
    private Date resentDate;
    private Date approvedDate;
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
    private String UpdatedBy;
	private Date updatedOn;
}
