package com.iweb2b.api.integration.model.consignment.dto.qp;

import java.util.HashSet;
import java.util.Set;

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

	private Set<String> errors = new HashSet<>();
//	private QPData data;
	private boolean data;
	private boolean isSuccessful;
}