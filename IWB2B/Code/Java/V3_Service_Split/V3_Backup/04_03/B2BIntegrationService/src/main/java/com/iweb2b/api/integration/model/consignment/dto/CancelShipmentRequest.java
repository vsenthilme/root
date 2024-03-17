package com.iweb2b.api.integration.model.consignment.dto;

import lombok.Data;

@Data
public class CancelShipmentRequest {
	/*
	 * {
		  "AWBNo": [
		    "G12340262"
		  ],
		  "customerReferenceNo": "BOQXXXXX"
		}
	 */
	private String[] AWBNo;
    private String customerReferenceNo;
}