package com.mnrclara.api.cg.transaction.model.auditlog;

import lombok.Data;

@Data
public class UpdateAuditLog {

    private String languageId;

	private Long classId;

	private Long transactionId;
	
	private String transactionNo;
	
	private String modifiedTableName;
	
	private String modifiedField;
	
	private String oldValue;
	
	private String newValue;

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
	
	private String updatedBy;
}
