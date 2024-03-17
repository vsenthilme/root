package com.mnrclara.api.crm.model.dto;

import lombok.Data;

@Data
public class UserProfile { 
	
	private String userId;
	private String languageId;
	private Long classId;
	private Long userRoleId;
	private Long userTypeId;
	private String firstName;
	private String lastName;
	private String fullName;
	private String userStatus;
	private String emailId;
	private String phoneNumber;	
}
