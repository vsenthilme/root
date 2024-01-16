package com.tekclover.wms.core.model.warehouse.inbound.almailem;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class B2bTransferInHeader {

	@NotBlank(message = "Company Code is mandatory")
	private String companyCode;
	@NotBlank(message = "Transfer Order Number is mandatory")
	private String transferOrderNumber;
	@NotBlank(message = "Branch Code is mandatory")
	private String branchCode;
}
