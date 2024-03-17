package com.mnrclara.api.management.model.matterdoclist;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AddMatterDocListHeader {

	private String languageId;
	private Long classId;
	private Long checkListNo;
	private String matterNumber;
	private String clientId;
	private Long caseCategoryId;
	private Long caseSubCategoryId;
	private String sentBy;
	private Date sentDate;
	private Date receivedDate;
	private Date resentDate;
	private Date approvedDate;
	private Date receivedOn;
	private Long statusId;
	private List<AddMatterDocListLine> matterDocLists;
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
}
