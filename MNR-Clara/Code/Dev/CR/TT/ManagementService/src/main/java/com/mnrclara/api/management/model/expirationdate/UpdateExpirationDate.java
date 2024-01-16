package com.mnrclara.api.management.model.expirationdate;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateExpirationDate {

	@NotBlank(message = "Language Id is mandatory")
    private String languageId;
	
	@NotNull(message = "Class Id is mandatory")
	private Long classId;
	
	@NotBlank(message = "DocumentType is mandatory")
	private String documentType;	

	@NotBlank(message = "Client Id is mandatory")
	private String clientId;
	
	private Long statusId;
	
	private Date approvalDate;
	
	private Date expirationDate;
	
	private Date eligibilityDate;
	
	private Long reminderDays;
	
	private Date reminderDate;
	
	private String reminderDescription;
	
	private Long deletionIndicator = 0L;
	
	private Boolean toggleNotification;
	
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
