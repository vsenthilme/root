package com.iweb2b.api.portal.model.consignment.dto.qp;

import java.util.List;

import lombok.Data;

@Data
public class QPTrackingRequest {

	/*
	 * {
	 * 		"pageNumber": 1,
	 * 		"tackingNumbers": [
	 * 			"QP12333536TM"
	 *  	]
	 *  }
	 */
	private String pageNumber;
	private List<String> tackingNumbers;
}
