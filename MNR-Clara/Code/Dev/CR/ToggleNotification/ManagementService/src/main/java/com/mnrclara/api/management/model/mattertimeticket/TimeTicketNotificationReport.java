package com.mnrclara.api.management.model.mattertimeticket;

import lombok.Data;

import java.util.Date;

@Data
public class TimeTicketNotificationReport {
	
	private Long notificationId;
	private Long weekOfYear;
	private Long year;
	private Long classId;
	private String timeKeeperCode;
	private String timeKeeperName;
	private Date fromDate;
	private Date toDate;
	private String sfromDate;
	private String stoDate;
	private Long targetHours;
	private Double timeKeeperHours;
	private Double timeKeeperAmount;
	private Double hoursCurrentWeek;
	private Double hoursPreviousWeek;
	private Double amountCurrentWeek;
	private Double amountPreviousWeek;
	private Date createdOn;
	private String screatedOn;
	private Long complianceStatus;
}
