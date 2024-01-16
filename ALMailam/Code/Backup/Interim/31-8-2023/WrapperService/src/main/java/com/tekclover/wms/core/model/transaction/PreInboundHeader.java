package com.tekclover.wms.core.model.transaction;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class PreInboundHeader { 
	
	protected String languageId;
	protected String companyCode;
	protected String plantId;
	protected String warehouseId;
	protected String preInboundNo;
	protected String refDocNumber;
	protected Long inboundOrderTypeId;
	protected String referenceDocumentType;
	protected Long statusId;
	protected String containerNo;
	protected Long noOfContainers;
	protected String containerType;
	protected Date refDocDate;
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
	
//	protected List<PreInboundLine> preInboundLine;
}
