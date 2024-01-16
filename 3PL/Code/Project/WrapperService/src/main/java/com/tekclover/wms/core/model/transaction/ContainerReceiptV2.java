package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class ContainerReceiptV2 extends ContainerReceipt {

	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String statusDescription;
}
