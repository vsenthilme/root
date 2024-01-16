package com.tekclover.wms.core.model.warehouse.outbound.almailem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class InterWarehouseTransferOutHeaderV2 {
	
	private String fromWhsID;							// WH_ID
	
	private String transferOrderNumber; 				// REF_DOC_NO
	
	private String toWhsID; 							// PARTNER_CODE
	
	private String storeName; 							// PARTNER_NM
		
	private String requiredDeliveryDate; 				//REQ_DEL_DATE

	private String companyCode;

	private String branchCode;
	private String languageId;
	
	@JsonIgnore
	private String id;
	
	@JsonIgnore
	private Date orderReceivedOn;
	
	@JsonIgnore
	private Long statusId;
}
