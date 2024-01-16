package com.mnrclara.wrapper.core.model.accounting;

import java.util.Date;

import lombok.Data;

@Data
public class FinalSummary {

	private Double priorBalance;
	private Double paymentReceived;
	private Date dateOfLastPayment;
	private Double currentFees;
	private Double advancedCost;
}
