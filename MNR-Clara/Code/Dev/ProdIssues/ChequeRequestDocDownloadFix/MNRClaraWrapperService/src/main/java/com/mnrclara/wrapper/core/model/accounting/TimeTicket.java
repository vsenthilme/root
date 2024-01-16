package com.mnrclara.wrapper.core.model.accounting;

import java.util.Date;

import lombok.Data;

@Data
public class TimeTicket {

	private String timeTicketNumber;
	private String timeTicketName;
	private Date createdOn;				// Ticket Date
	private Date timeTicketDate;
	private String originatingTimeKeeper;
	private String responsibleTimeKeeper;
	private String assignedTimeKeeper;
	private String ticketDescription;	// referenceField4 <- MatterTimeticket
	private Double billableTimeInHours;
	private Double billableAmount;
	private String billType;
	private String activityCode;
	private String activityText;
	private String taskCode;
	private String taskText;
	
}

