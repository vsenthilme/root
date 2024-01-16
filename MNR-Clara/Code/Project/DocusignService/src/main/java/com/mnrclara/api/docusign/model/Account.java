package com.mnrclara.api.docusign.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Account {

	/*
	 * "accounts": [
        {
            "account_id": "cc83e9d5-71d4-4606-bcbc-815360cdf457",
            "is_default": true,
            "account_name": "Tekclover",
            "base_uri": "https://demo.docusign.net",
            "organization": {
                "organization_id": "50231ab8-489c-4c0a-835f-1db414d17ace",
                "links": [
                    {
                        "rel": "self",
                        "href": "https://account-d.docusign.com/organizations/50231ab8-489c-4c0a-835f-1db414d17ace"
                    }
                ]
            }
        }
    ]
}
	 */
	private String account_id;
	private Boolean is_default;
	private String account_name;
	private String base_uri;
	private Organization organization;
}
