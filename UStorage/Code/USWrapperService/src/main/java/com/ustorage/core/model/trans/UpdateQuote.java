package com.ustorage.core.model.trans;

import lombok.Data;

@Data
public class UpdateQuote {

	private String enquiryReferenceNumber;
	private String codeId;
	private String customerName;
	private String mobileNumber;
	private String requirement;
	private String sbu;
	private String email;
	private String customerGroup;
	private String storeSize;
	private String rent;
	private String notes;
	private String documentStatus;
	private String requirementType;
	private String status;

	private String customerCode;
	private String addressFrom;
	private String addressTo;
	private String numberOfTrips;
	private Float packingCost;
	private String jobcardType;

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
}
