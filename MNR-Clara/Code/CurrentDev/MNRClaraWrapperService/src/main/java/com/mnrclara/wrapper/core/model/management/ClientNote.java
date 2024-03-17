package com.mnrclara.wrapper.core.model.management;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ClientNote {

	private String notesNumber;
	private String languageId;
	private Long classId;
	private String clientId;
	private String noteText;
	private String matterNumber;
	private Long noteTypeId;
	private Long statusId;
	private Long deletionIndicator = 0L;
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
	private Date createdOn = new Date();
	private String updatedBy;
	private Date updatedOn = new Date();
	private String additionalFields1;
	private String additionalFields2;
	private String additionalFields3;
	private String additionalFields4;
	private String additionalFields5;
	private String additionalFields6;
	private String additionalFields7;
}
