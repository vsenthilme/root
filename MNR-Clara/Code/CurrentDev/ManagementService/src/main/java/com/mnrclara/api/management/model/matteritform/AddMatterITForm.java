package com.mnrclara.api.management.model.matteritform;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AddMatterITForm {

	@NotBlank(message = "Matter Number is mandatory")
	private String matterNumber;	
	
	@NotBlank(message = "Client ID is mandatory")
	private String clientId;
	
	@NotNull(message = "IntakeForm ID is mandatory")
	private Long intakeFormId;
	
	private String intakeFormNumber;
	
	private String clientUserId;
	
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
