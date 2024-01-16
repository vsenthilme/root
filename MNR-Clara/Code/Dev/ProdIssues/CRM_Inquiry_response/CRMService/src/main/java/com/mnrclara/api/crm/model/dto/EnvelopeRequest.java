package com.mnrclara.api.crm.model.dto;

import lombok.Data;

@Data
public class EnvelopeRequest {

	private String file;
	private String documentId;
	private String docName;
	private String signerName;
	private String signerEmail;
	private String agreementCode;
	private String filePath;
}
