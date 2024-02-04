package com.mnrclara.api.crm.model.communication;


import lombok.Data;

@Data
public class SMS {

	private Long toNumber;
	private String textMessage;
}
