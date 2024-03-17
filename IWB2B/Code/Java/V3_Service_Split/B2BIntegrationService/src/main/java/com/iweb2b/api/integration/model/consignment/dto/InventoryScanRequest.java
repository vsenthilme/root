package com.iweb2b.api.integration.model.consignment.dto;

import java.util.Date;

import lombok.Data;

@Data
public class InventoryScanRequest {
	private String customerCode;
	private String type;
	private Date fromDate;
	private Date toDate;
}