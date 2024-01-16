package com.tekclover.wms.core.model.warehouse.inbound.almailem;

import com.tekclover.wms.core.model.warehouse.inbound.ASNHeader;
import lombok.Data;

@Data
public class ASNHeaderV2 extends ASNHeader {

	private String branchCode;
	private String companyCode;
}
