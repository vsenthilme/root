package com.tekclover.wms.api.transaction.model.outbound.preoutbound;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class AddPreOutboundHeader {

	private String languageId;
	private String companyCodeId;
	private String plantId;
	private String warehouseId;
	private String refDocNumber;
	private String preOutboundNo;
	private String partnerCode;
	private Long outboundOrderTypeId;
	private String referenceDocumentType;
	private Long statusId;
	private Date refDocDate = new Date();
	private Date requiredDeliveryDate;
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
	private String remarks;
	private String createdBy;
	private Date createdOn = new Date();
	private String updatedBy;
	private Date updatedOn = new Date();

	private List<AddPreOutboundHeader> addPreOutboundHeader;
}
