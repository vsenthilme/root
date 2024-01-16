package com.mnrclara.api.accounting.model.prebill.outputform;

import java.util.Date;

import lombok.Data;

@Data
public class Header {

	private Long classId;		
	private String classDescription;	
	private String clientId;
	private String clientName;
	private String addressLine3;
	private String partnerAssigned;
	private String matterNumber;
	private String matterDescription;
	private Long caseCategoryId;	// Matter Type
	private String billingRemarks;
	private Date caseOpenedDate;
	private String preBillBatchNumber;
	private String preBillNumber;
	private Date preBillDate;
	private String createdBy;
	private Long statusId;
	private String statusText;
	private String billingModeId;
	private String billingFrequencyId;
	private String billingFormatId;
	private Date startDateForPreBill;
	private Date feesCostCutoffDate;
	private Date paymentCutoffDate;
	private String originatingTimeKeeper;
	private String responsibleTimeKeeper;
	private String assignedTimeKeeper;
	private String comments;	// REF_FIELD_7 <- BILLFORMAT
	private String message;		// REF_FIELD_8 <- BILLFORMAT
	private Double flatFeeAmount; 

}
