package com.mnrclara.api.accounting.model.invoice;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchPaymentUpdate {
	private List<Long> paymentId;
	private List<String> invoiceNumber;
	private List<String> clientId;
	private List<String> matterNumber;
	private List<String> paymentNumber;
	private List<String> transactionType;
	
	private Date fromPostingDate;
	private Date toPostingDate;
	
	private Date fromPaymentDate;
	private Date toPaymentDate;
}
