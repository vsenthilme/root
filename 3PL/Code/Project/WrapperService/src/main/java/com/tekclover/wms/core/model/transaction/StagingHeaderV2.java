package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class StagingHeaderV2 extends StagingHeader {

	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String statusDescription;
}
