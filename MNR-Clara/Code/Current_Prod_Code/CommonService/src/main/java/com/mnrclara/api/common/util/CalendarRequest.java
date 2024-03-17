package com.mnrclara.api.common.util;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class CalendarRequest {
	CalendarRequest() {}
	private String uid = UUID.randomUUID().toString();
	private String toEmail;
	private String subject;
	private String body;
	private LocalDateTime meetingStartTime;
	private LocalDateTime meetingEndTime;
}
