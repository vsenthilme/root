package com.mnrclara.api.setup.model.expirationdoctype;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateExpirationDocType {

	@NotBlank(message = "Language Id is mandatory")
    private String languageId;
	
	@NotNull(message = "Class Id is mandatory")
	private Long classId;
	
	private String documentTypeDescription;
	
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
