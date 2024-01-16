package com.mnrclara.api.crm.model.dto;

import lombok.Data;

@Data
public class MailMerge {
	
	private Long classId;
	private String clientId;
	private String documentUrl;
	private Long documentUrlVersion;
	private String documentStorageFolder;
	private String agreementCode;
	private String processedFilePath;
	private String[] fieldNames;
	private Object[] fieldValues;
}
