package com.tekclover.wms.core.model.warehouse.inbound;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

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

