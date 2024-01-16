package com.tekclover.wms.api.transaction.model.warehouse.inbound;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class SOReturnHeader { 
	
	@NotBlank(message = "Warehouse Id is mandatory")
	private String wareHouseId;
	
	@NotBlank(message = "Return Order Reference is mandatory")
	private String returnOrderReference;

	@NotBlank(message = "CompanyCode is mandatory")
	private String companyCode;

	@NotBlank(message = "BranchCode is mandatory")
	private String branchCode;

	@NotBlank(message = "LanguageId is mandatory")
	private String languageId;

	@JsonIgnore
	private String id;
	
	@JsonIgnore
	private Date returnReceivedOn;
	
	@JsonIgnore
	private Long statusId;
	
}
