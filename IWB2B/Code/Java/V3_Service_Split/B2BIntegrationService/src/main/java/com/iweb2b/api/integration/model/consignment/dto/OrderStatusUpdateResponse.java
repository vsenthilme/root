package com.iweb2b.api.integration.model.consignment.dto;

import lombok.Data;

@Data
public class OrderStatusUpdateResponse {

	/*
	 * {
		  "reference_number": "2022-09-13_GTEST_G1",
		  "status": 1
		}
	 */
	private String reference_number;
	private Long status;
}
