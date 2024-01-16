package com.mnrclara.api.accounting.model.reports;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BilledHoursPaid {

	private List<String> clientId;
	private List<String> matterNumber;
	private List<String> timeKeeperCode;
	private List<String> partner;				//PartnerBilledHoursPaidReport

	private Date fromPostingDate;
	private Date toPostingDate;
	private Date fromTimeTicketDate;
	private Date toTimeTicketDate;
}
