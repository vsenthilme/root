package com.mnrclara.spark.core.model.wmscore;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class StagingLine {
	
	private String languageId;
	private String companyCode;
	private String plantId;
	private String warehouseId;
	private String preInboundNo;
	private String refDocNumber;
	private String stagingNo;
	private String palletCode;
	private String caseCode;
	private Long lineNo;
	private String itemCode;
	private Long inboundOrderTypeId;
	private Long variantCode;
	private String variantSubCode;
	private String batchSerialNumber;
	private Long stockTypeId;
	private Long specialStockIndicatorId;
	private String storageMethod;
	private Long statusId;
	private String businessPartnerCode;
	private String containerNo;
	private String invoiceNo;
	private Double orderQty;
	private String orderUom;
	private Double itemQtyPerPallet;
	private Double itemQtyPerCase;
	private String assignedUserId;
	private String itemDescription;
	private String manufacturerPartNo;
	private String hsnCode;
	private String variantType;
	private String specificationActual;
	private String itemBarcode;
	private String referenceOrderNo;
	private Double referenceOrderQty;
	private Double crossDockAllocationQty;
	private String remarks;
	private String referenceField1;
	private String referenceField2;
	private String referenceField3;
	private String referenceField4;
	private String referenceField5;
	private String referenceField6;
	private String referenceField7;
	private String referenceField8;
	private String referenceField9;
	private String referenceField10;
	private Long deletionIndicator;
	private String createdBy;
	private Timestamp createdOn;
	private String updatedBy;
	private Timestamp updatedOn;
	private String confirmedBy;
	private Timestamp confirmedOn;
}
