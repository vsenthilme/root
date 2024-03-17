package com.mnrclara.api.crm.model.dto;

import lombok.Data;

@Data
public class SMS {

	private Long toNumber;
	private String textMessage;
}
