package com.tekclover.wms.core.model.warehouse.inbound.almailem;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class InterWarehouseTransferInHeaderV2  {

	@NotBlank(message = "To CompanyCode is mandatory")
	private String toCompanyCode;
	
	@NotBlank(message = "To Branch Code is mandatory")
	private String toBranchCode;
	
	@NotBlank(message = "Transfer Order Number is mandatory")
	private String transferOrderNumber;
}
