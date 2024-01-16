package com.tekclover.wms.api.transaction.model.warehouse.inbound;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class InterWarehouseTransferInHeader { 
	
	@NotBlank(message = "To Warehouse Id is mandatory")
	private String warehouseId;
	
	@NotBlank(message = "Transfer Order Number is mandatory")
	private String transferOrderNumber;

	@NotBlank(message = "CompanyCode is mandatory")
	private String companyCode;

	@NotBlank(message = "BranchCode is mandatory")
	private String branchCode;

	@NotBlank(message = "LanguageId is mandatory")
	private String languageId;

	@JsonIgnore
	private String id;
	
	@JsonIgnore
	private Date orderReceivedOn;
	
	@JsonIgnore
	private Long statusId;
}
