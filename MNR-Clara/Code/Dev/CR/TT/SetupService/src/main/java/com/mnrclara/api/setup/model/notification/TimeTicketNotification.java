package com.mnrclara.api.setup.model.notification;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbltimeticketnotification")
public class TimeTicketNotification {
	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	@Column(name = "NOTIFICATION_ID")
	private Long notificationId;
	
	@Column(name = "WEEK_OF_YR")
	private Long weekOfYear;
	
	@Column(name = "CLASS_ID")
	private Long classId;
	
	@Column(name = "TK_CODE")
	private String timeKeeperCode;
	
	@Column(name = "TK_NAME")
	private String timeKeeperName;
	
	@Column(name = "FROM_DATE")
	private Date fromDate;
	
	@Column(name = "TO_DATE")
	private Date toDate;
	
	@Column(name = "TARGET_HOURS")
	private Long targetHours = 35L;
	
	@Column(name = "TK_HOURS")
	private Double timeKeeperHours;
	
	@Column(name = "TK_AMOUNT")
	private Double timeKeeperAmount;
	
	@Column(name = "HOURS_CURRENT_WEEK")
	private Double hoursCurrentWeek;
	
	@Column(name = "HOURS_PRE_WEEK")
	private Double hoursPreviousWeek;
	
	@Column(name = "AMT_CURRENT_WEEK")
	private Double amountCurrentWeek;
	
	@Column(name = "AMT_PRE_WEEK")
	private Double amountPreviousWeek;
	
	@Column(name = "CTD_ON")
	private Date createdOn;
	
	@Column(name = "COMP_STATUS")
	private Long complianceStatus;
}
