package com.mnrclara.wrapper.core.model.accounting;

import java.util.Date;
import java.util.List;

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
	private String sPreBillDate;
	private String clientId;
	private String matterNo;
	private Long timeTicketCount;
	private Double totalAmount;
	private List<MatterTimeTicket> matterTimeTicket;
	private List<MatterExpense> matterExpense;
	
	// dummy 
	private Double totalTimeExpenseAmount;
}
