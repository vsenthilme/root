package com.mnrclara.api.accounting.model.prebill;

import java.util.Date;
import java.util.List;

import com.mnrclara.api.accounting.model.management.MatterExpense;
import com.mnrclara.api.accounting.model.management.MatterTimeTicket;

import lombok.Data;

@Data
public class AddPreBillDetails {
	
	private String clientId;
	private String preBillBatchNumber;
	private String preBillNumber;
	private String matterNumber;
	private String languageId;
	private Long classId;
	private String partnerAssigned;
	private Long numberOfTimeTickets;
	private Double totalAmount;
	private Date preBillDate;
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
	private String createdBy;
	private Date createdOn = new Date();
	private String updatedBy;
	private Date updatedOn = new Date();

	private List<MatterTimeTicket> matterTimeTickets;
	private List<MatterExpense> matterExpenses;
}
