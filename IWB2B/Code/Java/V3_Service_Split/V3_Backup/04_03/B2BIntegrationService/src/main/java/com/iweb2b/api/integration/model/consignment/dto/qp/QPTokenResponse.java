package com.iweb2b.api.integration.model.consignment.dto.qp;

import lombok.Data;

import java.util.List;

@Data
public class QPTokenResponse {
	/*
	 * {
	 * 	"errors": []
	 * 	"data": "authorized"
	 * 	"isSuccessful": true
	 * }
	 */

	private List<String> errors;
	private String data;
	private Boolean isSuccessful;
}