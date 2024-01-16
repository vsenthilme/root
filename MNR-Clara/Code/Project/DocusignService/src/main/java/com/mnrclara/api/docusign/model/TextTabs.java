package com.mnrclara.api.docusign.model;

import lombok.Data;

@Data
public class TextTabs {
	/*
	 *   "anchorString": "/legal/",
"anchorUnits": "pixels",
"anchorXOffset": "5",
"anchorYOffset": "-9",
"bold": "true",
"font": "helvetica",
"fontSize": "size11",
"locked": "false",
"tabId": "legal_name",
"tabLabel": "Legal name",
"value": "'"${SIGNER_NAME}"'"
	 */
	
	private String anchorString;
	private String anchorUnits;
	private String anchorXOffset;
	private String anchorYOffset;
	private String bold;
	private String locked;
	private String tabId;
	private String tabLabel;
	private String value;
}
