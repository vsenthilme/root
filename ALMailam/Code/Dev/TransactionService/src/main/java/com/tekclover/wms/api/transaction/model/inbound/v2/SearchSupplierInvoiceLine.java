package com.tekclover.wms.api.transaction.model.inbound.v2;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SearchSupplierInvoiceLine {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;

	private List<String> warehouseId;
	private String referenceField1;
	private Date startConfirmedOn;
	private Date endConfirmedOn;

	private List<Long> statusId;
	private List<String> itemCode;
	private List<String> manufacturerPartNo;
	private List<String> refDocNumber;
}