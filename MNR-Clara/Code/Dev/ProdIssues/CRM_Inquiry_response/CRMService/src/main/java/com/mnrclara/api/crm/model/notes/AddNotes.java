package com.mnrclara.api.crm.model.notes;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AddNotes {

	@NotBlank(message = "Notes Number is mandatory")
	private String notesNumber;
	
	@NotNull(message = "Transaction ID is mandatory")
	private Long transactionId;
	
	@NotBlank(message = "Transaction No is mandatory")
	private String transactionNo;
	
	@NotNull(message = "Class ID is mandatory")
	private Long classId;
	
	@NotBlank(message = "Language ID is mandatory")
	private String languageId;
	
	private Long noteTypeId;
	private Long deletionIndicator = 0L;
	private String notesDescription;
	private String matterNumber;
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
    private Date createdOn;
    private String updatedBy;
	private Date updatedOn;
}
