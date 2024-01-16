package com.mnrclara.wrapper.core.model.accounting;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BilledHoursPaid {

	private List<String> clientId;
	private List<String> matterNumber;
	private List<String> timeKeeperCode;
	private List<String> partner;				//PartnerBilledHoursPaidReport

	private String fromPostingDate;
	private String toPostingDate;
	private String fromTimeTicketDate;
	private String toTimeTicketDate;
}
