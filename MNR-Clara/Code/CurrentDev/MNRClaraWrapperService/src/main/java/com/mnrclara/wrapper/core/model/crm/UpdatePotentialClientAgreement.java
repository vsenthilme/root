package com.mnrclara.wrapper.core.model.crm;

import lombok.Data;

@Data
public class UpdatePotentialClientAgreement {
	private String inquiryNumber;
	private Long intakeFormId;
	private Long caseCategoryId;
	private String agreementCode;
}
