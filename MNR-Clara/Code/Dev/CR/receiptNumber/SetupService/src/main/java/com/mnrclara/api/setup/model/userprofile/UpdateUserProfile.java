package com.mnrclara.api.setup.model.userprofile;

import lombok.Data;

@Data
public class UpdateUserProfile {

	private String languageId;
	
	private Long classId;
    
	private Long userRoleId;
	
	private String password;
	
	private Long userTypeId;

	private String firstName;

	private String lastName;

	private String fullName;
	
	private String userStatus;
	
	private String emailId;
	
	private String phoneNumber;
	
	private Long otpRequired;

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
	
	private String updatedBy;
}
