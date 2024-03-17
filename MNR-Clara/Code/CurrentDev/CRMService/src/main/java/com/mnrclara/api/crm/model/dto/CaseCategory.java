package com.mnrclara.api.crm.model.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CaseCategory { 
	
	private Long caseCategoryId;
	private String languageId;
	private Long classId;
	private String caseCategory;
	private String categoryDescription;
	private String taxType;
	private String categoryStatus;
	private Long deletionIndicator;
}
