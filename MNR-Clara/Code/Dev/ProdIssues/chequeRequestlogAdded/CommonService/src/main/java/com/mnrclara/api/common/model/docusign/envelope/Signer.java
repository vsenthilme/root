package com.mnrclara.api.common.model.docusign.envelope;

import lombok.Data;

@Data
public class Signer {

	/*
	 * {
        "email": "muruvel@gmail.com",
        "name": "Murugavel",
        "recipientId": "123"
	   }
	 */
	private String email;
	private String name;
	private String recipientId;
}
