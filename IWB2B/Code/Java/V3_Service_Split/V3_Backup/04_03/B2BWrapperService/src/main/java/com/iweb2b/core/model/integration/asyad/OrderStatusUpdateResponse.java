package com.iweb2b.core.model.integration.asyad;

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
