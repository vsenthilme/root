package com.mnrclara.api.crm.model.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Class { 
	
	private Long classId;
	private String languageId;
	private String companyId;
	private String classDescription;
	private String classStatus;
	private String createdBy;
    private Date createdOn;
    private String updatedBy;
	private Date updatedOn;
}