package com.iweb2b.api.integration.model.consignment.dto.alialgm;

import lombok.Data;

@Data
public class OrderStatusUpdateRequest {

	/*
	 * {
	    "order_id": 3019,
	    "new_status": "accept"
		}
	 */
    private String order_id;
    private String new_status;
}