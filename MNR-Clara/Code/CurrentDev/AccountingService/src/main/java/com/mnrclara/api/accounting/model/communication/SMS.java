package com.mnrclara.api.accounting.model.communication;


import lombok.Data;

@Data
public class SMS {

	private Long toNumber;
	private String textMessage;
}
