package com.mnrclara.api.accounting.model.invoice;

import java.util.Date;

import lombok.Data;

@Data
public class UpdateInvoiceLine {

	private String languageId;

	private Long classId;
	
	private String matterNumber;
	
	private String clientId;
	
	private String invoiceNumber;

	private String invoicePeriod;
	
	private Long itemNumber;
	
	private String activityCode;
	
	private String invoicedetailDescription;
	
	private String taskCode;
	
	private Date timeTicketDate;
	
	private String timeKeeperCode;
	
	private String billType;
	
	private Double billableTimeinHours;
	
	private Double billableAmount;
	
	private String currency;
	
	private String glAccount;
	
	private Long statusId;
	
	private Long deletionIndicator;
	
	private String referenceField1;
	
	private String referenceField2;
	
	private String referenceField3;
	
	private String referenceField4;
	
	private String referenceField5;
	
	private String referenceField6;
	
	private String referenceField7;
	
	private String referenceField8;
	
	private String referenceField9;
	
	private String referenceField10;
	
	private String updatedBy;
}
