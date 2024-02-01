package com.iweb2b.api.integration.model.consignment.dto;

import lombok.Data;

@Data
public class CancelShipmentRequest {
	/*
	 * {
		  "AWBNo": [
		    "G12340262"
		  ],
		  "customerCode": "XYZ"
		}
	 */
	private String[] AWBNo;
    private String customerCode;
}