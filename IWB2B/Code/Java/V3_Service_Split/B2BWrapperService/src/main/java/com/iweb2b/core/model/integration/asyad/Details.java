package com.iweb2b.core.model.integration.asyad;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Details {
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
	private String scanTime;
	private String desc;
	private String scanType;
	private String scanTypeCode;
	private String scanNetworkName;
    private String scanNetworkTypeName;
    private Long scanNetworkId;
    private String billCode;
    private String staffName;
    private String staffContact;
    private String scanNetworkContact;
    private String scanNetworkProvince;
    private String scanNetworkCity;
    private String scanNetworkArea;
    private String nextStopName;
    private String sigPicUrl;
    private String electronicSignaturePicUrl;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String otp;
    private String problemPicUrl;
    private String secondLevelTypeCode;
    private String arrivalPicUrl;
}