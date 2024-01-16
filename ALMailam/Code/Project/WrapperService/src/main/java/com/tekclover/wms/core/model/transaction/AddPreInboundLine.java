package com.tekclover.wms.core.model.transaction;

import lombok.Data;

import java.util.Date;

@Data
public class AddPreInboundLine {

	protected String languageId;
	protected String companyCodeId;
	protected String plantId;
	protected String warehouseId;
	protected String preInboundNo;
	protected String refDocNumber;
	protected Long lineNo;
	protected String itemCode;
	protected Long variantCode;
	protected String variantSubCode;
	protected Long inboundOrderTypeId;
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
	protected String referenceField11;
	protected String referenceField12;
	protected String referenceField13;
	protected String referenceField14;
	protected String referenceField15;
	protected String referenceField16;
	protected String referenceField17;
	protected String referenceField18;
	protected String referenceField19;
	protected String referenceField20;
	protected Long deletionIndicator;
	protected String createdBy;
	protected Date createdOn;
	protected String updatedBy;
	protected Date updatedOn;
}
