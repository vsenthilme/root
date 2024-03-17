package com.mnrclara.api.common.model.docusign.listDocs;

import java.util.List;

import lombok.Data;

@Data
public class ListDocument {

	/*
	 * {
	    "envelopeId": "16609626-7258-45af-bafb-11c9f15a3ccc",
	    "envelopeDocuments": []
	    }
	 */
	private String envelopeId;
	private List<EnvelopeDocument> envelopeDocuments;
}
