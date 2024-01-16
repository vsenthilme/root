package com.mnrclara.api.crm.model.potentialclient;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AddPotentialClient {

	@NotBlank(message = "Inquiry Number is mandatory")
	private String inquiryNumber;
	
	@NotEmpty(message = "Intake Form Number is mandatory")
	private String intakeFormNumber;
	
	@NotNull(message = "Intake Form ID is mandatory")
	private Long intakeFormId;
	
	private String referenceField9;

	private String leadership;
	private String hrPartners;
	private String position;
	private String dateOfHire;
}
