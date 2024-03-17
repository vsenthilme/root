package com.iweb2b.api.integration.model.consignment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ShipsyCancelShipmentRequest {
	/*
	 * {
		  "AWBNo": [
		    "G12340262"
		  ],
		  "customerReferenceNo": "BOQXXXXX"
		}
	 */
	@JsonProperty("AWBNo")
	private String[] AWBNo;
    private String customerCode;
}