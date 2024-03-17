package com.mnrclara.wrapper.core.model.accounting.report;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ExpirationDateRequestModel {

	private List<Long> caseCategoryId;
	private List<Long> caseSubCategoryId;
	private List<String> timeKeeperCode;
	private String documentType;

	private Date fromExpirationDate;
	private Date toExpirationDate;
	private Date fromEligibilityDate;
	private Date toEligibilityDate;
}
