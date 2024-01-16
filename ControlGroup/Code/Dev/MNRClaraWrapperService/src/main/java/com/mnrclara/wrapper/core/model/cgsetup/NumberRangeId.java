package com.mnrclara.wrapper.core.model.cgsetup;

import lombok.Data;
import java.util.Date;

@Data
public class NumberRangeId {

	private String languageId;

	private String companyId;
	
	private Long numberRangeCode;
	
	private String numberRangeObject;
	
	private String numberRangeFrom;
	
	private String numberRangeTo;
	
	private String numberRangeCurrent;
	
	private Long numberRangeStatus;
	
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

    private String updatedBy;

	private Date updatedOn = new Date();
}
