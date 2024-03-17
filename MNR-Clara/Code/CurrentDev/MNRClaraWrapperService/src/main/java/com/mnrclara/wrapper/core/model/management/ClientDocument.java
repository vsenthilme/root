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
public class ClientDocument {
	private Long clientDocumentId;
	private String documentNo;
	private String languageId;
	private Long classId;
	private String clientId;
	private String documentUrl;
	private Date documentDate;
	private String documentUrlVersion;
	private String clientUserId;
	private String matterNumber;
	private Long statusId;
	private String sentBy;
	private Date sentOn;
	private Date receivedOn;
	private Date resentOn;
	private Date approvedOn;
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
	private String UpdatedBy;
	private Date updatedOn = new Date();
}
