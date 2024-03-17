package com.mnrclara.api.common.model.docusign.envelope;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Document {
	/*
	 *  {
	      "documentBase64": "{{document}}",
	      "documentId": "123",
	      "fileExtension": "docx",
	      "name": "Test Docusign"
	    }
	 */
	@JsonIgnore
	private String documentBase64;
	private String documentId;
	private String fileExtension;
	private String name;
	
}
