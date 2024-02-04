package com.mnrclara.api.management.model.mattertimeticket;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchMatterTimeTicket {
	
	/*
	 * TIME_TICKET_NO
	 * BILL_TYPE
	 * TK_CODE/TK_NAME
	 * CTD_ON
	 * STATUS_ID/STATUS_TEXT
	 * -----------------------
	 * multisearch
	 * ===========
	 * TIME_TICKET_NO
	 * BILL_TYPE
	 * TK_CODE/TK_NAME
	 * STATUS_ID
	 */
	private List<String> timeTicketNumber;
	private List<String> billType;
	private List<String> timeKeeperCode;
	private List<Long> statusId;
	private List<String> matterNumber;
	
	private Date startCreatedOn;
	private Date endCreatedOn;

	private Date startTimeTicketDate;
	private Date endTimeTicketDate;
	private String sStartTimeTicketDate;
	private String sEndTimeTicketDate;
}
