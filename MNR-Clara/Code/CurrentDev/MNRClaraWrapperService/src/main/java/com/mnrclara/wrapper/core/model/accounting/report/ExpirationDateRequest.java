package com.mnrclara.wrapper.core.model.accounting.report;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ExpirationDateRequest {

	private List<Long> caseCategoryId;
	private List<Long> caseSubCategoryId;
	private List<String> timeKeeperCode;
	private String documentType;

	private String fromExpirationDate;
	private String toExpirationDate;
	private String fromEligibilityDate;
	private String toEligibilityDate;
}
