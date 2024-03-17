package com.mnrclara.wrapper.core.model.accounting;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class PreBillDetailsEntity { 
	
	private String clientId;
	private String matterNumber;
	private String partnerAssigned;
	private Long numberOfTimeTickets;
	private Double totalAmount;
	private Date startDateForPreBill;
	private Date feesCostCutoffDate;
	private Date paymentCutoffDate;
	private Date preBillDate;
	private String preBillBatchNumber;
	private String preBillNumber;
	private String languageId;
	private Long classId;
	private Long statusId;
	private Long deletionIndicator = 0L;
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
	private String createdBy;
	private Date createdOn = new Date();
	private String updatedBy;
	private Date updatedOn = new Date();
}
