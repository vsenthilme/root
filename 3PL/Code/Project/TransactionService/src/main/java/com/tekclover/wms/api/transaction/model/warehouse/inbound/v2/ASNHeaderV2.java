package com.tekclover.wms.api.transaction.model.warehouse.inbound.v2;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ASNHeaderV2  {

	@NotBlank(message = "Branch Code is mandatory")
	private String branchCode;
	@NotBlank(message = "Company Code is mandatory")
	private String companyCode;
	@NotBlank(message = "ASN number is mandatory")
	private String asnNumber;
}
