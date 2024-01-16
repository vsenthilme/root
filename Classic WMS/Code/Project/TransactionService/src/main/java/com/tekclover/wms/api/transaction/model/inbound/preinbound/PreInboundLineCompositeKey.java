package com.tekclover.wms.api.transaction.model.inbound.preinbound;

import java.io.Serializable;

import lombok.Data;

@Data
public class PreInboundLineCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `LANG_ID`, `C_ID`, `PLANT_ID`, `WH_ID`, `PRE_IB_NO`, `REF_DOC_NO`, `IB_LINE_NO`, `ITM_CODE`
	 */
		private String languageId;
		private String companyCode;
		private String plantId;
		private String warehouseId;
		private String preInboundNo;
		private String refDocNumber;
		private Long lineNo;
		private String itemCode;
}
