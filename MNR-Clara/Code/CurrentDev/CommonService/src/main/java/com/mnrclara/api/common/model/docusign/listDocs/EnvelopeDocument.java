package com.mnrclara.api.common.model.docusign.listDocs;

import lombok.Data;

@Data
public class EnvelopeDocument {
	/*
	 * "envelopeDocuments": [
	        {
	            "documentId": "123",
	            "documentIdGuid": "6119c003-509b-4fba-bbe9-b35fa87e35b5",
	            "name": "Test Docusign",
	            "type": "content",
	            "uri": "/envelopes/16609626-7258-45af-bafb-11c9f15a3ccc/documents/123",
	            "order": "1",
	            "pages": [],
	            "availableDocumentTypes": [],
	            "display": "inline",
	            "includeInDownload": "true",
	            "signerMustAcknowledge": "no_interaction",
	            "templateRequired": "false",
	            "authoritativeCopy": "false"
	        }
	 */
	private String documentId;
    private String documentIdGuid;
    private String name;
    private String type;
    private String uri;
}
