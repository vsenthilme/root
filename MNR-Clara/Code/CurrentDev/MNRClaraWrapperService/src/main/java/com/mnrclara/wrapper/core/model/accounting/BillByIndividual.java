package com.mnrclara.wrapper.core.model.accounting;

import java.util.Date;

import lombok.Data;

@Data
public class BillByIndividual {

	/*
	 * Client_Id
	 * MATTER_NO
	 * Pre_Bill_Date
	 * Start_Date
	 * Fees_Cutoff
	 * Payment_Cutoff
	 */
	private String clientId;
	private String matterNumber;
	private Date preBillDate;
	private Date startDate;
	private Date feesCutoffDate;
	private Date paymentCutoffDate;
}
