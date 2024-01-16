package com.iweb2b.api.integration.model.consignment.dto.jnt;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Details {
/*
 * {
	"billCode": "JTE000159748033",
	"details": [
		{
			"billCode": "JTE000159748033",
			"desc": "【Jeddah】The parcel has been signed by [Receiver signed], the Delivery courier is [+966-560240654] ",
			"otp": "200065",
			"scanNetworkCity": "Jeddah",
			"scanNetworkId": 210,
			"scanNetworkName": "Jeddah Station 3",
			"scanNetworkProvince": "Makkah Province",
			"scanNetworkTypeName": "网点",
			"scanTime": "2023-07-26 11:33:28",
			"scanType": "Sign scan",
			"sigPicUrl": "https://pro-jmssa-file.s3.me-south-1.amazonaws.com/lite-ylappbc/SIGNING_SCAN_LIST/b625f029f4404e02bb2690e10bfc1539.png?response-content-disposition=inline%3B%20filename%3Db625f029f4404e02bb2690e10bfc1539.png&response-content-encoding=UTF-8&response-content-type=image%2Fpng&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20230726T083700Z&X-Amz-SignedHeaders=host&X-Amz-Expires=604800&X-Amz-Credential=AKIAY3KSSVR4UM3VR7X7%2F20230726%2Fme-south-1%2Fs3%2Faws4_request&X-Amz-Signature=23ce9b211fbf7d86c813bb516ef10b25b2cb08cb3f2974210ab8ec8fdbbb3c01,https://pro-jmssa-file.s3.me-south-1.amazonaws.com/lite-ylappbc/SIGNING_SCAN_LIST/e3ef57182af843d89d69539f77126d6c.png?response-content-disposition=inline%3B%20filename%3De3ef57182af843d89d69539f77126d6c.png&response-content-encoding=UTF-8&response-content-type=image%2Fpng&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20230726T083700Z&X-Amz-SignedHeaders=host&X-Amz-Expires=604800&X-Amz-Credential=AKIAY3KSSVR4UM3VR7X7%2F20230726%2Fme-south-1%2Fs3%2Faws4_request&X-Amz-Signature=831bcae279ab95067d8d4c33d8143b1603d119370b4e9097bf9b52abdb1ee958"
		}
	]
}
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