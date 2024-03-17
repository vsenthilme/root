package com.iweb2b.core.model.integration.asyad;

import lombok.Data;

@Data
public class JNTWebhookEntity {
    private Long jnt_webhook_id;
	private String billCode;
	private String desc;
    private String scanNetworkCity;
    private Long scanNetworkId;
    private String scanNetworkName;
    private String scanNetworkProvince;
    private String scanNetworkTypeName;
    private String scanTime;
}