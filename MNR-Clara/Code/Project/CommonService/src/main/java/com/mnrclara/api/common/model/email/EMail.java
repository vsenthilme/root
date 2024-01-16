package com.mnrclara.api.common.model.email;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class EMail {

	@NotEmpty (message = "EMail cannot be blank.")
	@Email (message = "Please correct EMail address.")
	private String fromAddress;
	
	@NotEmpty (message = "EMail cannot be blank.")
	private String toAddress;
	
	@Email (message = "Please correct EMail address.")
	private String ccAddress;
	
	@NotEmpty (message = "Subject cannot be blank.")
	private String subject;
	
	@NotEmpty (message = "body text message cannot be blank.")
	private String bodyText;
	
	private String uid = UUID.randomUUID().toString();
	private String senderName;
	private LocalDateTime meetingStartTime;
	private LocalDateTime meetingEndTime;
}
