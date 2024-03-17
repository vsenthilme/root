package com.mnrclara.api.management.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

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
