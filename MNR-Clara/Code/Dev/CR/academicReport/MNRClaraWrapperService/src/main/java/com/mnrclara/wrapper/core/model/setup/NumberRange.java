package com.mnrclara.wrapper.core.model.setup;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class NumberRange {
	private String languageId = "EN";
	private Long classId;
	private Long numberRangeCode;
	private String numberRangeObject;
	private String numberRangeFrom;
	private String numberRangeTo;
	private String numberRangeCurrent;
	private String numberRangeStatus;
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
	private String updatedBy;
	private Date createdOn;
	private Date updatedOn;
}
