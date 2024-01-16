package com.tekclover.wms.core.model.transaction;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class InboundHeader { 

	protected String languageId;
	protected String companyCode;
	protected String plantId;
	protected String warehouseId;
	protected String refDocNumber;
	protected String preInboundNo;
	protected Long statusId;
	protected Long inboundOrderTypeId;
	protected String containerNo;
	protected String vechicleNo;
	protected String headerText;
	protected Long deletionIndicator = 0L;
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
	protected String createdBy;
	protected Date createdOn = new Date();
	protected String confirmedBy;
	protected Date confirmedOn = new Date();
	
	protected List<InboundLine> inboundLine;
}
