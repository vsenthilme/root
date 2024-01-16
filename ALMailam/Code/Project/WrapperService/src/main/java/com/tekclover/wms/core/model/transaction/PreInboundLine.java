package com.tekclover.wms.core.model.transaction;

import java.util.Date;

import lombok.Data;

@Data
public class PreInboundLine { 
	
	protected String languageId;
	protected String companyCode;
	protected String plantId;
	protected String warehouseId;
	protected String preInboundNo;
	protected String refDocNumber;
	protected Long lineNo;
	protected String itemCode;
	protected Long inboundOrderTypeId;
	protected Long variantCode;
	protected String variantSubCode;
	protected Long statusId;
	protected String itemDescription;
	protected String containerNo;
	protected String invoiceNo;
	protected String businessPartnerCode;
	protected String partnerItemNo;
	protected String brandName;
	protected String manufacturerPartNo;
	protected String hsnCode;
	protected Date expectedArrivalDate;
	protected Double orderQty;
	protected String orderUom;
	protected Long stockTypeId;
	protected Long specialStockIndicatorId;
	protected String numberOfPallets;
	protected String numberOfCases;
	protected Double itemPerPalletQty;
	protected Double itemCaseQty;
	protected String referenceField1;
	protected String referenceField2;
	protected String referenceField3;
	protected String referenceField4;
	protected String referenceField5;
	protected String referenceField6;
	protected String referenceField7;
	protected String referenceField8;
	protected String referenceField9;
	protected String referenceField10;
	protected Long deletionIndicator;
	protected String createdBy;
	protected Date createdOn = new Date();
	protected String updatedBy;
	protected Date updatedOn = new Date();
}
