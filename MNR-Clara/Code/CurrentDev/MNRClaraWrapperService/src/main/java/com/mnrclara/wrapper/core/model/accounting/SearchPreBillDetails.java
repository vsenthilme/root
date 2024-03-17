package com.mnrclara.wrapper.core.model.accounting;

import java.util.List;

import lombok.Data;

@Data
public class SearchPreBillDetails {
	/*
	 * CLASS_ID
	 * CLIENT_ID
	 * PARTNER_ASSIGNED
	 * MATTER_NO
	 * PRE_BILL_BATCH_NO
	 * PRE_BILL_NO
	 * PRE_BILL_DATE
	 * CTD_BY
	 * STATUS_ID
	 */
	private List<Long> classId;	
	private List<String> clientId;
	private List<String> partnerAssigned;
	private List<String> matterNumber;
	private List<String> preBillBatchNumber;
	private List<String> preBillNumber;
	
//	private Date startPreBillDate;	
//	private Date endPreBillDate;
	
	private String startPreBillDate;	
	private String endPreBillDate;
	
	private List<String> createdBy;
	private List<Long> statusId;
}
