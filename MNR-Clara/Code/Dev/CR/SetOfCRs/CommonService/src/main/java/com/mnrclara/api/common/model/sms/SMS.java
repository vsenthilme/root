package com.mnrclara.api.common.model.sms;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SMS {

	@NotNull (message = "To Number cannot be left blank")
	private Long toNumber;
	
	@NotEmpty (message = "Text meesage cannot be blank.")
	private String textMessage;
}
