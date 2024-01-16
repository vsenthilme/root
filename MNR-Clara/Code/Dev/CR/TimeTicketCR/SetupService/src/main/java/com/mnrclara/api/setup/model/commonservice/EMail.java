package com.mnrclara.api.setup.model.commonservice;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class EMail {

	private String fromAddress;
	private String toAddress;
	private String ccAddress;
	private String subject;
	private String bodyText;
	private String uid = UUID.randomUUID().toString();
	private String senderName;
	private LocalDateTime meetingStartTime;
	private LocalDateTime meetingEndTime;
}
