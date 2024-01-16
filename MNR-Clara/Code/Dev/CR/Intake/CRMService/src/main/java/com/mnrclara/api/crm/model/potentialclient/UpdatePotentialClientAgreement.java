package com.mnrclara.api.crm.model.potentialclient;

import lombok.Data;

@Data
public class UpdatePotentialClientAgreement {
	private String inquiryNumber;
	private Long intakeFormId;
	private Long caseCategoryId;
	private String agreementCode;
	private Long statusId;
}
