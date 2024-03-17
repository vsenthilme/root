package com.iweb2b.api.integration.model.consignment.dto;

import lombok.Data;

@Data
public class CancelShipmentResponse {
	/*
	 * {
		  "status": "OK"
		}
	 */
    private String status;
    private Boolean success;
}