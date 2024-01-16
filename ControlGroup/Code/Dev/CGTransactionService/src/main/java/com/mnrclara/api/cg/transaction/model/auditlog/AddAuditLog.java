package com.mnrclara.api.cg.transaction.model.auditlog;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AddAuditLog {

	@NotEmpty(message = "Language ID is mandatory")
    private String languageId;
	
	@NotNull(message = "Class ID is mandatory")
	private Long classId;
	
	@NotEmpty(message = "Audit Log Number is mandatory")
	private String auditLogNumber;

	@NotNull(message = "Transaction ID is mandatory")
	private Long transactionId;

	@NotEmpty(message = "Transaction No is mandatory")
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
	
	private String createdBy;
}
