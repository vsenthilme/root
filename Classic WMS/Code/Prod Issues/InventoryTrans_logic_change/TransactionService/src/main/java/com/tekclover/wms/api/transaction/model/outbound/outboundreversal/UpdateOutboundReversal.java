package com.tekclover.wms.api.transaction.model.outbound.outboundreversal;

import java.util.Date;

import lombok.Data;

@Data
public class UpdateOutboundReversal {

	private String languageId;
	private String companyCodeId;
	private String plantId;
	private String warehouseId;
	private String outboundReversalNo;
	private String reversalType;
	private String refDocNumber;
	private String partnerCode;
	private String itemCode;
	private String packBarcode;
	private Double reversedQty;
	private Long statusId;
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
	private String reversedBy;
	private Date reversedOn = new Date();
}
