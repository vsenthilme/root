package com.mnrclara.api.docusign.model;

import lombok.Data;

@Data
public class SignHereTabs {

	private String documentId; 
	private String pageNumber;
	private String xPosition; 
	private String yPosition;
}
