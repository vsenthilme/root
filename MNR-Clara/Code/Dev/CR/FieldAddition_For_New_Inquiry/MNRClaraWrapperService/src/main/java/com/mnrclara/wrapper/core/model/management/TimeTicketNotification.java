package com.mnrclara.wrapper.core.model.management;

import lombok.Data;
import java.util.Date;

@Data
public class TimeTicketNotification {
	
	private Long notificationId;
	
	private Long weekOfYear;
	
	private Long classId;
	
	private String timeKeeperCode;
	
	private String timeKeeperName;
	
	private Date fromDate;
	
	private Date toDate;
	
	private Long targetHours = 35L;
	
	private Double timeKeeperHours;
	
	private Double timeKeeperAmount;
	
	private Double hoursCurrentWeek;
	
	private Double hoursPreviousWeek;
	
	private Double amountCurrentWeek;
	
	private Double amountPreviousWeek;
	
	private Date createdOn;
	
	private Long complianceStatus;

	private String sfromDate;
	private String stoDate;
	private String screatedOn;
}
