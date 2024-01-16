package com.tekclover.wms.api.transaction.model.warehouse.inbound.v2;

import com.tekclover.wms.api.transaction.model.warehouse.inbound.SOReturnHeader;
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
