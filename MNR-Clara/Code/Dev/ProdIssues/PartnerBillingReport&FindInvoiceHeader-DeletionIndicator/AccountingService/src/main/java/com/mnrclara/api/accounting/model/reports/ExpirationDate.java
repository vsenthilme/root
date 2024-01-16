package com.mnrclara.api.accounting.model.reports;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ExpirationDate {

	private List<Long> caseCategoryId;
	private List<Long> caseSubCategoryId;
	private List<String> timeKeeperCode;
	private List<String> documentType;
	private List<String> receiptNumber;

	private Date fromExpirationDate;
	private Date toExpirationDate;
	private Date fromEligibilityDate;
	private Date toEligibilityDate;
}
