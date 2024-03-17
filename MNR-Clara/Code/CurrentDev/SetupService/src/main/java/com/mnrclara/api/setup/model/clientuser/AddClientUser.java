package com.mnrclara.api.setup.model.clientuser;

import java.util.Date;

import lombok.Data;

@Data
public class AddClientUser {

	private String languageId;
	private Long classId;
	private String clientUserId;
	private String clientId;
	private Long clientCategoryNo;
	private String contactNumber;
	private String firstName;
	private String lastName;
	private String fullName;
	private String statusId;
	private String emailId;
	private Integer quotation;
	private Integer paymentPlan;
	private Integer matter;
	private Integer documents;
	private Integer invoice;
	private Integer agreement;
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
	private Date createdOn = new Date();
	private String updatedBy;
	private Date updatedOn = new Date();
}
