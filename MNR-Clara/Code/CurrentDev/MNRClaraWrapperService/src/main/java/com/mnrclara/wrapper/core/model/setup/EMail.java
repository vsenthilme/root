package com.mnrclara.wrapper.core.model.setup;

import lombok.Data;

@Data
public class EMail {

	private String fromAddress;
	private String toAddress;
	private String ccAddress;
	private String subject;
	private String bodyText;
	private String senderName;
}
