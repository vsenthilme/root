package com.iweb2b.core.model.integration.asyad;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class QPTrackingResponse {
	/*
	 * {
	    "errors": [],
	    "data": [
	        {
	            "tracking_No": "QP12333536TM",
	            "item_Action_Id": 5,
	            "item_Action_Name": "CREATED",
	            "action_Date": "2023-06-12T12:56:33.317",
	            "action_Time": "2023-06-12T12:56:33.317",
	            "operator_Name": "0",
	            "totalRows": 1
	        }
	    ],
	    "isSuccessful": true
		}
	 */

	private Set<String> errors = new HashSet<>();
	private QPData data;
	private Boolean isSuccessful;
}