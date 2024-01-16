package com.mnrclara.api.management.model.dto;

import java.util.Date;

import lombok.Data;

@Data
public class EnvelopeResponse {

	/*
	 * {
	    "envelopeId": "16609626-7258-45af-bafb-11c9f15a3ccc",
	    "uri": "/envelopes/16609626-7258-45af-bafb-11c9f15a3ccc",
	    "statusDateTime": "2021-10-15T08:15:22.5930000Z",
	    "status": "sent"
		}
	 */
	private String envelopeId;
	private String uri;
	private Date statusDateTime;
	private String status;
}
