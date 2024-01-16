package com.tekclover.wms.api.transaction.model.warehouse.outbound.v2;

import com.tekclover.wms.api.transaction.model.warehouse.outbound.ReturnPOHeader;
import lombok.Data;

@Data
public class ReturnPOHeaderV2 extends ReturnPOHeader {

	private String companyCode;
}
