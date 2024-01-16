package com.mnrclara.api.cg.setup.model.commonservice;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

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
