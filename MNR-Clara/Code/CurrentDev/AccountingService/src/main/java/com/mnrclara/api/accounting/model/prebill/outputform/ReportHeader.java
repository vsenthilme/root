package com.mnrclara.api.accounting.model.prebill.outputform;

import java.util.Date;

import lombok.Data;

@Data
public class ReportHeader {

	private Long classId;		
	private String classDescription;	
	private String clientId;
	private String clientName;
	private String addressLine15;				// REF_FIELD_15, 16, 17, 18
	private String addressLine16;
	private String addressLine17;
	private String addressLine18;
	private String addressLine19;
	private String addressLine20;
	private String partnerAssigned;
	private String matterNumber;
	private String matterDescription;
	private String caseCategory;
	private String caseSubCategory;
	private String billingRemarks;			// MatterGenAcc - Bill_remarks
	private Date caseOpenedDate;			// ?
	private String preBillBatchNumber;		// ?
	private String preBillNumber;			// ?
	private Date preBillDate;				// ?
	private String createdBy;
	private Long statusId;					// Matter - Status ID ?
	private String statusText;				// Matter - Status Text ?
	private String billingModeId;			// ?
	private String billingFrequencyId;		// ?
	private String billingFormatId;			// ?
	private Date startDateForPreBill;
	private Date feesCostCutoffDate;		// ?
	private Date paymentCutoffDate;			// ?
	private String originatingTimeKeeper;
	private String responsibleTimeKeeper;
	private String assignedTimeKeeper;
	private String comments;				// REF_FIELD_7 <- BILLFORMAT
	private String message;					// REF_FIELD_8 <- BILLFORMAT
	private Double flatFeeAmount; 
	private String invoiceRemarks;
	private String referenceField3;
	private String referenceField4;
	private String referenceField5;
	private String referenceField6;
	private String referenceField7;
	private String referenceField8;
	private Double administrativeCost;
}
