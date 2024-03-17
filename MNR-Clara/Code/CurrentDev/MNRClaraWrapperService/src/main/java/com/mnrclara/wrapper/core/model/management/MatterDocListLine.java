package com.mnrclara.wrapper.core.model.management;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
public class MatterDocListLine {
	private Long matterLineId;
	private Long matterHeaderId;
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
	private String createdBy;
	private Date createdOn;
	private String updatedBy;
	private Date updatedOn;
}
