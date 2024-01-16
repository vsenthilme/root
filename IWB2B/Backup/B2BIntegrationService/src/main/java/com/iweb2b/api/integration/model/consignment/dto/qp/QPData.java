package com.iweb2b.api.integration.model.consignment.dto.qp;

import lombok.Data;

@Data
public class QPData {

	/*
	 * "tracking_No": "QP12333536TM",
        "item_Action_Id": 5,
        "item_Action_Name": "CREATED",
        "action_Date": "2023-06-12T12:56:33.317",
        "action_Time": "2023-06-12T12:56:33.317",
        "operator_Name": "0",
        "totalRows": 1
	 */
	private String tracking_No;
	private Long item_Action_Id;
	private String item_Action_Name;
	private String action_Date;
	private String action_Time;
	private String operator_Name;
	private Long totalRows;
}
