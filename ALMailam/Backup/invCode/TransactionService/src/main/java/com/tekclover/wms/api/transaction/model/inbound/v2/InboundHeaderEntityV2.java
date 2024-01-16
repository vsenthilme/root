package com.tekclover.wms.api.transaction.model.inbound.v2;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@Data
public class InboundHeaderEntityV2 {

	private String languageId;
	private String companyCodeId;
	private String plantId;
	private String warehouseId;
	private String refDocNumber;
	private String preInboundNo;
	private Long statusId;
	private Long inboundOrderTypeId;
	private String containerNo;
	private String vechicleNo;
	private String headerText;
	private Long deletionIndicator;
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
	private String createdBy;
	private Date createdOn = new Date();
	private String updatedBy;
	private Date updatedOn = new Date();

	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String statusDescription;

	private String middlewareId;
	private String middlewareTable;
	private String manufacturerFullName;
	private String referenceDocumentType;

	private List<InboundLineV2> inboundLine;
}
