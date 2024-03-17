package com.mnrclara.api.accounting.model.invoice;

import java.util.List;

import lombok.Data;

@Data
public class SearchInvoiceHeaderARAgingReport {
	
	private Long classId;
	private List<String> clientId;
	private List<String> matterNumber;
	private List<Long> statusId;
}
