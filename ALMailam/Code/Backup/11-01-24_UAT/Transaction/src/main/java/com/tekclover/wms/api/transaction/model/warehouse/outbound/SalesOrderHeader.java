package com.tekclover.wms.api.transaction.model.warehouse.outbound;

import java.util.Date;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class SalesOrderHeader {

	@Column(nullable = false)
	@NotBlank(message = "Warehouse Id is mandatory")
	private String wareHouseId;								// WH_ID

	@Column(nullable = false)
	@NotBlank(message = "Sales Order Number is mandatory")
	private String salesOrderNumber; 						// REF_DOC_NO;

	@Column(nullable = false)
	@NotBlank(message = "Store ID is mandatory")
	private String storeID; 								// PARTNER_CODE;
	
	private String storeName; 								// PARTNER_NM;

	@Column(nullable = false)
	@NotBlank(message = "Required Delivery Date is mandatory")
	private String requiredDeliveryDate; 					//REQ_DEL_DATE
	
	@JsonIgnore
	private String id;
	
	@JsonIgnore
	private Date orderReceivedOn;
	
	@JsonIgnore
	private Long statusId;
	
}
