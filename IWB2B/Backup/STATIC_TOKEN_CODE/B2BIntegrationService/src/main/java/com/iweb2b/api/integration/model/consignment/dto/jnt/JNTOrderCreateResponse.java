package com.iweb2b.api.integration.model.consignment.dto.jnt;

import lombok.Data;

@Data
public class JNTOrderCreateResponse {
	/*
	 * {
	 * 	code:"1"
	 * 	msg:"success"
	 * 	data:{
	 * 		txlogisticId:"SA67733240451"
	 * 		billCode:"UTE010001283460"
	 * 		sortingCode:"DC02-CN222-"
	 * 		createOrderTime:"2021-12-02 10:02:51"
	 * 		lastCenterName:"Dubai DC"
	 * 	}
	 * }
	 */

    private String code;
    private String msg;
    private JNTData data;
}