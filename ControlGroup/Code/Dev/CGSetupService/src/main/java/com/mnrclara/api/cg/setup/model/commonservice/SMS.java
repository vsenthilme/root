package com.mnrclara.api.cg.setup.model.commonservice;

import lombok.Data;

@Data
public class SMS {

	private Long toNumber;
	private String textMessage;
}
