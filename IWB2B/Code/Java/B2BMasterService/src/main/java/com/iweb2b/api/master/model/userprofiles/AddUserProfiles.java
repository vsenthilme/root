package com.iweb2b.api.master.model.userprofiles;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AddUserProfiles {

	@NotBlank(message = "User ID is mandatory")
	private String userId;
	
	@NotNull(message = "Profile Type is mandatory")
	private String profileType;
	
	@NotBlank(message = "Partner is mandatory")
	private String partner;
			
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
