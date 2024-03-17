package com.mnrclara.wrapper.core.model.accounting;

import java.util.Date;

import lombok.Data;

@Data
public class MatterCountAndSum { 
	/*
	 * 
	 */
	private String clientId;
	private String matterNumber;
	private Date preBillDate;
	private Long countOfTimeTickets;
	private Long sumOfAmount;
}
