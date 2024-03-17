package com.mnrclara.api.accounting.model.quotation;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchQuotationHeader {
	/*
	 * Client_ID,
	 * FIRST_LAST_NM
	 * Client_Category
	 * Matter_No
	 * Quote_No
	 * Quote_Date
	 * CTD_BY
	 * STATUS_ID
	 */
	private List<String> clientId;
	private List<String> firstNameLastName;
	private List<Long> caseCategoryId;
	private List<String> matterNumber;
	private List<String> quotationNo;
	
	private Date startQuotationDate;
	private Date endQuotationDate;
	
	private List<String> createdBy;
	private List<Long> statusId;
}
