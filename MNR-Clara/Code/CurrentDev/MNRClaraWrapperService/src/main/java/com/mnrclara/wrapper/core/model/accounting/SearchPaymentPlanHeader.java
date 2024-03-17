package com.mnrclara.wrapper.core.model.accounting;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchPaymentPlanHeader {
	/*
	 * CLIENT_ID
	 * MATTER_NO
	 * QUTOE_NO
	 * PAYMENT_PLAN_NO
	 * PAYMENT_PLAN_DATE
	 * STATUS_ID
	 * CTD_BY
	 * CTD_ON
	 */
	private List<Long> clientId;	
	private List<String> matterNumber;
	private List<String> quotationNo;
	private List<String> paymentPlanNumber;	
	private List<Long> statusId;
	
	private Date startPaymentPlanDate;
	private Date endPaymentPlanDate;
	
	private List<String> createdBy;
	
	private Date startCreatedOn;
	private Date endCreatedOn;	
}
