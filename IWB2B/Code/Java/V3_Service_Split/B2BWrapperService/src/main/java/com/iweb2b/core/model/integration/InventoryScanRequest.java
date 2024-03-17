package com.iweb2b.core.model.integration;

import java.util.Date;

import lombok.Data;

@Data
public class InventoryScanRequest {
	private Date fromDate;
	private Date toDate;
}