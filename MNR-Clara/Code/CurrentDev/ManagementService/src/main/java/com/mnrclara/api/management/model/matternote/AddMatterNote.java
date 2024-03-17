package com.mnrclara.api.management.model.matternote;

import java.util.Date;

import lombok.Data;

@Data
public class AddMatterNote {

    private String languageId;
	private Long classId;
	private String matterNumber;
	private String clientId;
	private String notesNumber;
	private String notesDescription;
	private Long statusId;
	private Long noteTypeId;
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
	private Date createdOn;
}
