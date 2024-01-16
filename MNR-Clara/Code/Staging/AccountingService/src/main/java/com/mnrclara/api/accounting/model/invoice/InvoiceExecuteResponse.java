package com.mnrclara.api.accounting.model.invoice;

import lombok.Data;

@Data
public class InvoiceExecuteResponse {

	/*
	 * S.No
	 * Client Name
	 * Matter No
	 * Pre Bill No
	 * Partner Approved
	 * Invoice Date
	 * Total Amount
	 * Invoice Amount	Bill To client Id	Bill to Client %
	 */
	private Integer sNo;
	private String clientName;
	private String matterNumber;
	private String preBillNumber;
	private String partnerAssigned;
	private Long totalAmount;
	
}
