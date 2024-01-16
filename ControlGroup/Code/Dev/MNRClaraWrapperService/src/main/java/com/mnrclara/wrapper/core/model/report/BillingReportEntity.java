package com.mnrclara.wrapper.core.model.report;

import java.util.Date;

import lombok.Data;

@Data
public class BillingReportEntity {

	private Long classId;
	private String clientId;
	private String matterNumber;
	private Date billingDate;
	private Date postingDate;
	private String invoiceNumber;
	private Double feeBilled;
	private Double remainingBalance;
	private Double totalBilled;
	private String matterName;
	private String billingMode;
	private String clientName;
	private Double hardCost;
	private Double softCost;
	private Double adminCost;
	private Double totalHours;
	private Double miscDebits;
	private String classIdDescription;
	private String billingModeDescription;
	private String partnerAssigned;
}
