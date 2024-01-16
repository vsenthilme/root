package com.tekclover.wms.api.transaction.model.inbound.staging;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class AddStagingHeader {

	private String languageId;
	private String companyCodeId;
	private String plantId;
	private String warehouseId;
	private String preInboundNo;
	private String refDocNumber;
	private String stagingNo;
	private Long inboundOrderTypeId;
	private Long statusId;
	private String containerReceiptNo;
	private String dockAllocationNo;
	private String containerNo;
	private String vechicleNo;
	private String grMtd;
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
	private String confirmedBy;
	private Date confirmedOn = new Date();

	private List<AddStagingHeader> addStagingHeader;
}
