package com.tekclover.wms.api.transaction.model.warehouse.outbound.v2;

import com.tekclover.wms.api.transaction.model.warehouse.outbound.ReturnPOLine;

import lombok.Data;

@Data
public class ReturnPOLineV2 extends ReturnPOLine{

	private String manufacturerCode;
	private String ManufacturerName;
	private String Brand;
}
