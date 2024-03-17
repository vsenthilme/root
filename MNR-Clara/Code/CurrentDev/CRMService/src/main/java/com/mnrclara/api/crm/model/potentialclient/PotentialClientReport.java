package com.mnrclara.api.crm.model.potentialclient;

import lombok.Data;

@Data
public class PotentialClientReport {

	private String classId;
	private String potentialClientId;
	private String firstNameLastName;
	private String addressLine1;
	private String emailId;
	private String contactNumber;
	private Long referralId;
	private String referralIdDesc;
	private String socialSecurityNo;
	private Long statusId;
	
	private String referenceField2;	// Followup by
	
	private String referenceField3;	// onboarding Status

	private String referenceField4;	// Consulting Attorney
}
