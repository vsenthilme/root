package com.mnrclara.api.accounting.model.auditlog;

import java.util.Date;

import lombok.Data;

@Data
public class AuditLog {

    private String languageId;
	private Long classId;
	private String auditLogNumber;
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
	private String createdBy;
    private String UpdatedBy;
	private Date updatedOn = new Date();
}
