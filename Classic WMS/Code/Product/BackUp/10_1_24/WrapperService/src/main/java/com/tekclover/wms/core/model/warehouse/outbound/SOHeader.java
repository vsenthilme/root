package com.tekclover.wms.core.model.warehouse.outbound;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class SOHeader { 
	
	@NotBlank(message = "Warehouse Id is mandatory")
	private String wareHouseId;								// WH_ID
	
	@NotBlank(message = "Transfer Order Number is mandatory")
	private String transferOrderNumber; 					// REF_DOC_NO;
	
	@NotBlank(message = "Store ID is mandatory")
	private String storeID; 								// PARTNER_CODE;
	
	private String storeName; 								// PARTNER_NM;
		
	@NotBlank(message = "Required Delivery Date is mandatory")
	private String requiredDeliveryDate; 					//REQ_DEL_DATE

	@NotBlank(message = "CompanyCode is mandatory")
	private String companyCode;

	@NotBlank(message = "BranchCode is mandatory")
	private String branchCode;

	private String languageId;

	@JsonIgnore
	private String id;
	
	@JsonIgnore
	private Date orderReceivedOn;
	
	@JsonIgnore
	private Long statusId;
}
