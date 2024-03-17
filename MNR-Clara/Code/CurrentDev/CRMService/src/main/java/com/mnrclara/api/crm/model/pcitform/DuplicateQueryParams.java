package com.mnrclara.api.crm.model.pcitform;

import lombok.Data;

@Data
public class DuplicateQueryParams {

	// Validate data types LANG_ID/INQ_NO/CLASS_ID/IT_FORM_ID/IT_FORM_NO/IS_DELETED=0 
	// for duplicate records before inserting into PICINTAKEFORM table
	private String languageId;
	private String inquiryNumber;
	private Long classId;
	private Long intakeFormId;	
	private String intakeFormNumber;
}
