package com.mnrclara.api.common.model.docusign.userinfo;

import lombok.Data;

@Data
public class Links {

	/*
	 * "links": [
	            {
	                "rel": "self",
	                "href": "https://account-d.docusign.com/organizations/50231ab8-489c-4c0a-835f-1db414d17ace"
	            }
	        ]
	 */
	private String rel;
	private String href;
}
