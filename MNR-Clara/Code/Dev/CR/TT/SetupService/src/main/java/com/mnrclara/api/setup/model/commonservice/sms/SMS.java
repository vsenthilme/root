package com.mnrclara.api.setup.model.commonservice.sms;

import lombok.Data;

@Data
public class SMS {

	private Long toNumber;
	private String textMessage;
}
