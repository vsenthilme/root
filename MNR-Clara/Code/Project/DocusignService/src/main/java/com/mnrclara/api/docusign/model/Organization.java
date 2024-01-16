package com.mnrclara.api.docusign.model;

import java.util.List;

import lombok.Data;

@Data
public class Organization {

	/*
	 * "organization": {
	        "organization_id": "50231ab8-489c-4c0a-835f-1db414d17ace",
	        "links": [
	            {
	                "rel": "self",
	                "href": "https://account-d.docusign.com/organizations/50231ab8-489c-4c0a-835f-1db414d17ace"
	            }
	        ]
	    }
	 */
	private String organization_id;
	private List<Links> links;
}
