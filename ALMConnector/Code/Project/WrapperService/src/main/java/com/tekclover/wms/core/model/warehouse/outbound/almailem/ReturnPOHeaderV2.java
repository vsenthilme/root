package com.tekclover.wms.core.model.warehouse.outbound.almailem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class ReturnPOHeaderV2 {
	
	private String wareHouseId;							// WH_ID
	
	private String poNumber; 							// REF_DOC_NO;
	
	private String storeID; 							// PARTNER_CODE;
	
	private String storeName; 							// PARTNER_NM;
		
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
