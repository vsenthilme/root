package com.tekclover.wms.core.model.transaction;

import java.util.Date;

import lombok.Data;

@Data
public class ContainerReceipt { 

	protected String languageId;
	protected String companyCodeId;
	protected String plantId;
	protected String warehouseId;
	protected String preInboundNo;
	protected String refDocNumber;
	protected String containerReceiptNo;
	protected Date containerReceivedDate;
	protected String containerNo;
	protected Long statusId;
	protected String containerType;
	protected String partnerCode;
	protected String invoiceNo;
	protected String consignmentType;
	protected String origin;
	protected String numberOfPallets;
	protected String numberOfCases;
	protected String dockAllocationNo;
	protected String remarks;
	protected Long deletionIndicator;
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
	protected String updatedBy;
}
