package com.mnrclara.api.docusign.model;

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
	private String documentBase64;
	private String documentId;
	private String fileExtension;
	private String name;
	
}
