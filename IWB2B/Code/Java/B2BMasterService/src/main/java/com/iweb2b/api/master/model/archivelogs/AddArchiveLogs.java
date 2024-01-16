package com.iweb2b.api.master.model.archivelogs;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AddArchiveLogs {

	@NotBlank(message = "Partner ID is mandatory")
	private String partnerId;
	
	@NotNull(message = "Partner Name is mandatory")
	private String partnerName;
	
	@NotBlank(message = "Access Log User is mandatory")
	private String accessLogUser;
		
	private String accessLogApi;
	
	private Date averageResponseTime;
	
	private String responseTimeGreaterthanAverage;
	
	private String deliveryFrequency;
		
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
