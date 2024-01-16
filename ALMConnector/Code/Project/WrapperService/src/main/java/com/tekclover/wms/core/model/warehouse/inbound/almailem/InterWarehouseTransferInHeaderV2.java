package com.tekclover.wms.core.model.warehouse.inbound.almailem;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class InterWarehouseTransferInHeaderV2  {

	@NotBlank(message = "To CompanyCode is mandatory")
	private String toCompanyCode;
	
	@NotBlank(message = "To Branch Code is mandatory")
	private String toBranchCode;
	
	@NotBlank(message = "Transfer Order Number is mandatory")
	private String transferOrderNumber;
}
