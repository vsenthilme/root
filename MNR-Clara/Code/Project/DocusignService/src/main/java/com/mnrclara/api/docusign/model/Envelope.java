package com.mnrclara.api.docusign.model;

import java.util.List;


import lombok.Data;

@Data
public class Envelope {

	/*
	 * {
		  "documents": [
		    {
		      "documentBase64": "{{document}}",
		      "documentId": "123",
		      "fileExtension": "docx",
		      "name": "Test Docusign"
		    }
		  ],
		  "emailSubject": "Please sign the document",
		  "recipients": {
		    "signers": [
		      {
		        "email": "muruvel@gmail.com",
		        "name": "Murugavel",
		        "recipientId": "123"
		      }
		    ]
		  },
		  "status": "sent"
		}
	 */
	private List<Document> documents;
	private String emailSubject;
	private Recipients recipients;
	private String status;
}
