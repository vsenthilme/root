package com.mnrclara.api.management.model.dto;

import lombok.Data;

@Data
public class MailMerge {
	private Long classId;
	private String clientId;
	private String documentUrl;
//	private String documentDropboxFolder;
	private String documentStorageFolder;
	private Long documentUrlVersion;
	private String agreementCode;
	private String documentCode;
	private String processedFilePath;
	private String[] fieldNames;
	private Object[] fieldValues;
	
	// Property added for only to handle clientDocument and matterDocument
	private boolean fromClientORMatterDocument = false;
}
