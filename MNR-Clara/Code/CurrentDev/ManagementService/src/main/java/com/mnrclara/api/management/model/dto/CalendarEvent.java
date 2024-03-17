package com.mnrclara.api.management.model.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CalendarEvent extends EMail {

	private String senderName;
	private LocalDateTime meetingStartTime;
	private LocalDateTime meetingEndTime;
}
