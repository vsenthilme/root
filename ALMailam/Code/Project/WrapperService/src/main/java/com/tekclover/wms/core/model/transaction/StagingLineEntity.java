package com.tekclover.wms.core.model.transaction;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class StagingLineEntity { 

	protected String languageId;
	protected String companyCode;
	protected String plantId;
	protected String warehouseId;
	protected String preInboundNo;
	protected String refDocNumber;
	protected String stagingNo;
	protected String palletCode;
	protected String caseCode;
	protected Long lineNo;
	protected String itemCode;
	protected Long inboundOrderTypeId;
	protected Long variantCode;
	protected String variantSubCode;
	protected String batchSerialNumber;
	protected Long stockTypeId;
	protected Long specialStockIndicatorId;
	protected String storageMethod;
	protected Long statusId;
	protected String businessPartnerCode;
	protected String containerNo;
	protected String invoiceNo;
	protected Double orderQty;
	protected String orderUom;
	protected Double itemQtyPerPallet;
	protected Double itemQtyPerCase;
	protected String assignedUserId;
	protected String itemDescription;
	protected String manufacturerPartNo;
	protected String hsnCode;
	protected String variantType;
	protected String specificationActual;
	protected String itemBarcode;
	protected String referenceOrderNo;
	protected Double referenceOrderQty;
	protected Double crossDockAllocationQty;
	protected String remarks;
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
	protected Long deletionIndicator = 0L;
	protected String createdBy;
	protected Date createdOn = new Date();
	protected String updatedBy;
	protected Date updatedOn = new Date();
	protected String confirmedBy;
	protected Date confirmedOn = new Date();
}
