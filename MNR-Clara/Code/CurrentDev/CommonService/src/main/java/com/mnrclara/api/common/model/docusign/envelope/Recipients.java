package com.mnrclara.api.common.model.docusign.envelope;

import java.util.List;

import lombok.Data;

@Data
public class Recipients {

	/*
	 * "signers": [
	      {
	        "email": "muruvel@gmail.com",
	        "name": "Murugavel",
	        "recipientId": "123"
	      }
	    ]
	 */
	private List<Signer> signers;
}
