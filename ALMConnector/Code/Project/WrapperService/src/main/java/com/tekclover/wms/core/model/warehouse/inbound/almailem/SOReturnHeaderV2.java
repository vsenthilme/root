package com.tekclover.wms.core.model.warehouse.inbound.almailem;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SOReturnHeaderV2  {

	@NotBlank(message = "Company Code is mandatory")
	private String companyCode;
	@NotBlank(message = "Branch Code is mandatory")
	private String branchCode;
	@NotBlank(message = "Transfer Order Number is mandatory")
	private String transferOrderNumber;

}
