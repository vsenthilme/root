package com.iweb2b.api.integration.model.consignment.dto.jnt;

import lombok.Data;

@Data
public class JNTPrintLabelBzContent {
	
	/*
	 * {
	 * 		"customerCode": "J0086024173",
	 * 		"digest": "VdlpKaoq64AZ0yEsBkvt1A==",
	 * 		"billCode": "UTE010005886251"
	 * }
	 */
    private String customerCode;
    private String digest;
    private String billCode;
}