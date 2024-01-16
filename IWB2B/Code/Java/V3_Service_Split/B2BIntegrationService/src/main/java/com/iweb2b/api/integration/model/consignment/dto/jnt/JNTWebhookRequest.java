package com.iweb2b.api.integration.model.consignment.dto.jnt;

import java.util.List;

import lombok.Data;

@Data
public class JNTWebhookRequest {
	/*
	 * {
	 * 	"billCode":"UTE300000056947",
	 * 	"details":[
	 * 		{
	 * 			"billCode":"UTE300000056947",
	 * 			"desc":"【Riyadh】The Pickup courier [Pinky(+966-08070003)] of [Riyadh station 1] has picked up the parcel ",
	 * 			"scanNetworkCity":"Riyadh",
	 * 			"scanNetworkId":193,
	 * 			"scanNetworkName":"Riyadh station 1",
	 * 			"scanNetworkProvince":"Riyadh",
	 * 			"scanNetworkTypeName":"网点",
	 * 			"scanTime":"2022-06-06 10:10:28",
	 * 			"scanType":"Pickup scan"
	 * 		}
	 * 	]
	 * }
	 */
    private String billCode;
    private List<Details> details;
}