package com.mnrclara.api.accounting.model.prebill.outputform;

import java.util.List;

import lombok.Data;

@Data
public class TimeTicketDetail {

	private List<TimeTicket> timeTickets;					// 2. Time Tickets
	private Double sumOfTotalHours;
	private Double sumOfTotalAmount;
	private Double sumOfTotalBillableHours;
	private Double sumOfTotalBillableAmount;
}
