package com.mnrclara.api.management.model.matteritform;

import java.util.Date;

import lombok.Data;

@Data
public class UpdateMatterITForm {

	// this.id = language + ":" + classID + ":" + matterNumber + ":" + clientId + ":" + itFormNo + ":" + itFormID;
    
	private String languageId;
	private Long classId;
	private String matterNumber;
	private String clientId;
	private String intakeFormNumber;
	private Long intakeFormId;
	private Long statusId;
	private String notesNumber;
	private Date sentOn;
	private Date receivedOn;
	private Date resentOn;
	private Date approvedOn;
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
