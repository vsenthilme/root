package com.iweb2b.api.master.model.subscriptionmgmt;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AddSubscriptionManagement {

	@NotBlank(message = "Partner ID is mandatory")
	private String partnerId;
	
	@NotNull(message = "Subcription Type is mandatory")
	private String subcriptionType;

	private String sourceSystem;
	
	private String targetSystem;
	
	private String apiKey;
	
	private String syncControl;
	
	private String accessType;
	
	private String chargeable;
		
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
