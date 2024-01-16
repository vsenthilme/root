package com.flourish.b2b.api.gateway.model.consignmentoutbound;

import java.util.Date;

import lombok.Data;

@Data
public class ServiceProviderResponse {
	private String status;
	private String resultDescription;
	private Date timestamp;
}
