package com.mnrclara.wrapper.core.model.management;

import lombok.Data;

import java.util.Date;

@Data
public class ClientGeneralNew {

	private String classId;
	private String clientId;
	private String firstNameLastName;
	private String corporationClientId;
	private String emailId;
	private String contactNumber;
	private String addressLine1;
//	private String addressLine2;
	private String intakeFormNumber;
//	private Long intakeFormId;
//	private String city;
//	private String state;
//	private String zipCode;
//	private String statusId;
	private String createdOnString;

	//later added for academy report requested by client
	private String leadership;
	private String hrPartners;
	private String position;
	private String dateOfHire;
}
