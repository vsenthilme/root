package com.mnrclara.wrapper.core.model.management;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class MatterDocListHeader {

	private Long matterHeaderId;
	private String languageId;
	private Long classId;
	private Long checkListNo;
	private String matterNumber;
	private String clientId;
	private String matterText;
	private Long caseCategoryId;
	private Long caseSubCategoryId;
	private String sentBy;
	private Date sentDate;
	private Date receivedDate;
	private Date resentDate;
	private Date approvedDate;
	private Long statusId;
	private Set<MatterDocListLine> matterDocLists;
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
	private Date receivedOn;
	private String createdBy;
	private Date createdOn;
	private String updatedBy;
	private Date updatedOn;
}
