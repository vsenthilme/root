package com.mnrclara.api.management.model.matterdoclist;

import lombok.Data;

@Data
public class AddMatterDocListLine {

	private String languageId;
	private Long classId;
	private Long checkListNo;
	private String matterNumber;
	private String documentUrl;
	private String clientId;
	private Long sequenceNumber;
	private String documentName;
	private String matterText;
	private Long caseCategoryId;
	private Long caseSubCategoryId;
	private Long statusId;
	private Long deletionIndicator;
	private String referenceField1;
	private String referenceField2;
	private String referenceField3;
	private String referenceField4;
	private String referenceField5;
}
