package com.mnrclara.wrapper.core.model.accounting;

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
