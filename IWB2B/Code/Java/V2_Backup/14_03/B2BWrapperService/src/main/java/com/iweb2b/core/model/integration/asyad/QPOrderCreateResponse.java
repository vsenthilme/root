package com.iweb2b.core.model.integration.asyad;

import java.util.List;

import lombok.Data;

@Data
public class QPOrderCreateResponse {
	/*
	 * {
	 * 	"errors": []
	 * 	"data": true
	 * 	"isSuccessful": true
	 * }
	 */

	private List<String> errors;
	private Boolean data;
	private Boolean isSuccessful;
}