package com.mnrclara.api.management.model.matterdoclist;

import java.util.Date;

import lombok.Data;

@Data
public class UpdateMatterDocList {

	private String languageId;
	private Long classId;
	private Long checkListNo;
	private String matterNumber;
	private String clientId;
	private String sequenceNumber;
	private String documentName;
	private String sentBy;
	private Date sentDate;
	private Date receivedDate;
	private Date resentDate;
	private Date approvedDate;
	private Long statusId;
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
	private Date createdOn = new Date();
	private String updatedBy;
	private Date updatedOn = new Date();
}
