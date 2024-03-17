package com.mnrclara.api.management.model.dto;

import java.util.Date;
import java.util.List;

import com.mnrclara.api.management.model.matterexpense.MatterExpense;
import com.mnrclara.api.management.model.mattertimeticket.MatterTimeTicket;

import lombok.Data;

@Data
public class MatterTimeExpenseTicket { 
	
	/*
	 * Pre Bill Date,
	 * Client Name,
	 * Matter No,
	 * No of Time Tickets
	 * Total Amount
	 */
	private Date preBillDate;
	private String clientId;
	private String matterNo;
	private Long timeTicketCount;
	private Double totalAmount;
	private List<MatterTimeTicket> matterTimeTicket;
	private List<MatterExpense> matterExpense;	
}
