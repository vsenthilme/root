package com.mnrclara.api.setup.model.userprofile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class AddUserProfile {

    private String languageId;
	
	private Long classId;
	
	private String userId;

	private Long userRoleId;
	
	@NotBlank(message = "Password is mandatory")
	private String password;

	private Long userTypeId;

	private String firstName;

	private String lastName;

	private String fullName;
	
	private String userStatus;
	
	@Email (message = "Please correct Email Address")
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
	
	private String createdBy;
}
