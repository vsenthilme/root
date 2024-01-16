package com.mnrclara.api.management.model.clientdocument;

import java.util.Date;

import lombok.Data;

@Data
public class UpdateClientDocument {

    private String languageId;

	private Long classId;

	private String clientId;

	private String documentUrl;

	private Date documentDate = new Date();
	
	private String documentUrlVersion;
	
	private String clientUserId;
	
	private String matterNumber;
	
	private Long statusId;
	
	private String sentBy;
	
	private Date sentDate = new Date();
	
	private Date receivedDate = new Date();
	
	private Date resentDate = new Date();
	
	private Date approvedDate = new Date();
	
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
