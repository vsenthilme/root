package com.iweb2b.api.integration.model.consignment.dto;

import lombok.Data;

@Data
public class OrderStatusUpdate {

	/*
	 * {
		  "reference_number": "2022-09-13_GTEST_G1",
		  "event_time_epoch": 1614093117,
		  "worker_code": "JTEST",
		  "hub_code": "HUB123",
		  "employee_code": "string",
		  "notes": "Updated by Ram",
		  "reason_code": "string",
		  "cancel_reason": "string",
		  "lat": 28.123,
		  "lng": 70.123,
		  "cod_amount": 345.54
		}
	 */
	private String reference_number;
	private Long event_time_epoch;
	private String worker_code;
	private String hub_code;
	private String employee_code;
	private String notes;
	private String reason_code;
	private String cancel_reason;
	private String lat;
	private String lng;
	private Double cod_amount;
}
