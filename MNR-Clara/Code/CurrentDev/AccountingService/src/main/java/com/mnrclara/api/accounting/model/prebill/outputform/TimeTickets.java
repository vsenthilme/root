package com.mnrclara.api.accounting.model.prebill.outputform;

import java.util.Date;

import lombok.Data;

@Data
public class TimeTickets {

	private String timeTicketNumber;
	private Date createdOn;				// Ticket Date
	private String originatingTimeKeeper;
	private String responsibleTimeKeeper;
	private String assignedTimeKeeper;
	private String ticketDescription;	// referenceField4 <- MatterTimeticket
	private Double approvedBillableTimeInHours;
	private Double approvedBillableAmount;
	private String billType;
	private String activityCode;
	private String activityText;
	private String taskCode;
	private String taskText;
	
}

