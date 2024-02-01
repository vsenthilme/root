package com.iweb2b.api.integration.model.consignment.dto.jnt;

import lombok.Data;

@Data
public class JNTWebhookDetails {
	
	/*
	 * 	data:{
	 * 		txlogisticId:"SA67733240451"
	 * 		billCode:"UTE010001283460"
	 * 		sortingCode:"DC02-CN222-"
	 * 		createOrderTime:"2021-12-02 10:02:51"
	 * 		lastCenterName:"Dubai DC"
	 * 	}
	 */

    private String txlogisticId;
    private String billCode;
    private String sortingCode;
    private String createOrderTime;
    private String lastCenterName;
}