package com.mnrclara.api.docusign.model;

import lombok.Data;

@Data
public class TextCustomFields {
	/*
	 * "name": "salary",
 		"required": "false",
		"show": "true",
		"value": "123000"
	 */
	private String name;
	private String required;
	private String show;
	private String value;
	private String xPosition;
	private String yPosition;
}
