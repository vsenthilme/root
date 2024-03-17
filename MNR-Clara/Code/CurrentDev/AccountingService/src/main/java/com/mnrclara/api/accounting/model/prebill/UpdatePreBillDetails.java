package com.mnrclara.api.accounting.model.prebill;

import java.util.Date;

import lombok.Data;

@Data
public class UpdatePreBillDetails {

	private String preBillBatchNumber;
	private String preBillNumber;
	private Date preBillDate;
	private String matterNumber;
	private String clientId;
	private String languageId;
	private Long classId;
	private String partnerAssigned;
	private Long numberOfTimeTickets;
	private Double totalAmount;
	private Date startDateForPreBill;
	private Date feesCostCutoffDate;
	private Date paymentCutoffDate;
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
	private Date updatedOn = new Date();
}
