package com.iweb2b.api.master.model.healthcheck;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AddHealthCheck {
	
	@NotBlank(message = "Partner ID is mandatory")
	private String partnerId;
	
	@NotNull(message = "Partner Name is mandatory")
	private String partnerName;
	
	@NotBlank(message = "System List is mandatory")
	private String systemsList;
	
	private String activeSystems;

	private String inactiveSystems;
	
	private String ipAvailability;
	
	private String memoryUtilization;
	
	private String cpuUtilization;
	
	private String heatMap;
	
	private String alarms;
	
	private Date activeSince;
			
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
