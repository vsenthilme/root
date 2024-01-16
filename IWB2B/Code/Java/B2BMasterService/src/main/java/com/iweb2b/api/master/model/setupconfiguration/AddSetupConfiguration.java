package com.iweb2b.api.master.model.setupconfiguration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AddSetupConfiguration {

	@NotBlank(message = "Partner ID is mandatory")
	private String partnerId;
	
	@NotNull(message = "System Type is mandatory")
	private String systemType;
	
	@NotBlank(message = "Admin User is mandatory")
	private String adminUser;
	
	private String syncStatusId;
		
	private String sourceSystem;
	
	private String targetSystem;
	
	private String accessType;
	
	private String remarks;
	
	private String userCreation;
		
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
