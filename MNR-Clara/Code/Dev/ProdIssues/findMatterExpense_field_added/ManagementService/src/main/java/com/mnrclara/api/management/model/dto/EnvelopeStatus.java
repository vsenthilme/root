package com.mnrclara.api.management.model.dto;

import lombok.Data;

@Data
public class EnvelopeStatus {

	/*
	    "status": "completed",
	    "documentsUri": "/envelopes/ef74f4a1-2026-44aa-bdfd-92a59af5c3b5/documents",
	 */
	private String status;
	private String documentsUri;
}
