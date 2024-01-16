package com.tekclover.wms.api.transaction.model.warehouse.inbound.v2;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class InterWarehouseTransferInLineV2 {

	@NotBlank(message = "From Company Code is Mandatory")
	private String fromCompanyCode;
	
	@NotBlank(message = "Origin is mandatory")
	private String origin;
	
	private String supplierName;
	
	@NotBlank(message = "Manufacturer Code is mandatory")
	private String manufacturerCode;
	
	private String Brand;
	
	@NotBlank(message = "From Branch Code is mandatory")
	private String fromBranchCode;
	
	@NotNull(message = "Line Reference is mandatory")
	private Long lineReference;
	
	@NotBlank(message = "sku is mandatory")
	private String sku;
	
	@NotBlank(message = "sku Description is mandatory")
	private String skuDescription;
	
	private String supplierPartNumber;
	
	@NotBlank(message = "Manufacturer Name is mandatory")
	private String manufacturerName;
	
	@NotBlank(message = "Excepted Date is mandatory")
	private String expectedDate;
	
	@NotNull(message = "Excepted Qty is mandatory")
	private Double expectedQty;
	
	@NotBlank(message = "Uom is mandatory")
	private String uom;
	
	private Double packQty;

}
