package com.mnrclara.wrapper.core.model.management;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class MatterDocument { 
	
	private Long matterDocumentId;
	private String documentNo;
	private String languageId;
	private Long classId;
	private String matterNumber;
	private String clientId;
	private String documentUrl;
	private Date docuemntDate;
	private String documentUrlVersion;
	private Long sequenceNo;
	private String clientUserId;
	private Long statusId;
	private String sentBy;
	private Date sentOn;
	private Date receivedOn;
	private Date resentOn;
	private Date approvedOn;
	private Long caseCategoryId;
	private Long caseSubCategoryId;	
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
    private String UpdatedBy;
	private Date updatedOn;
}
