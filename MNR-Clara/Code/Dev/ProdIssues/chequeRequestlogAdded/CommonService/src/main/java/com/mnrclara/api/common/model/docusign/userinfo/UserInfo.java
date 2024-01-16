package com.mnrclara.api.common.model.docusign.userinfo;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class UserInfo {

	/*
	 * {
    "sub": "3e1dac76-7f99-45a2-86dc-37f7703c3333",
    "name": "Murugavel R",
    "given_name": "Murugavel",
    "family_name": "R",
    "created": "2021-07-21T12:38:08.79",
    "email": "murugavel.r@prodapt.com",
    "accounts": [
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
	private String sub;
	private String name;
	private String given_name;
	private String family_name;
	private Date created;
	private String email;
	private List<Account> accounts;
}
