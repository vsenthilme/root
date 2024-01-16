package com.tekclover.wms.core.model.warehouse.inbound;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class B2bTransferInHeader {

	@NotBlank(message = "Company Code is mandatory")
	private String companyCode;

	@NotBlank(message = "Branch Code is mandatory")
	private String branchCode;

	@NotBlank(message = "Warehouse Id is mandatory")
	private String warehouseId;

	@NotBlank(message = "languageId is mandatory")
	private String languageId;

	@NotBlank(message = "Transfer Order Number is mandatory")
	private String transferOrderNumber;


}
