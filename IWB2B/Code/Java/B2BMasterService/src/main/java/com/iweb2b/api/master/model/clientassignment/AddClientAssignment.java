package com.iweb2b.api.master.model.clientassignment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AddClientAssignment {

	@NotBlank(message = "Partner ID is mandatory")
	private String partnerId;
	
	@NotNull(message = "Partner Name is mandatory")
	private String partnerName;
	
	private String subscriptionType;
	
	@NotBlank(message = "Partner Type is mandatory")
	private String partnerType;
	
	private String accessType;
	
	private String statusId;
		
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
