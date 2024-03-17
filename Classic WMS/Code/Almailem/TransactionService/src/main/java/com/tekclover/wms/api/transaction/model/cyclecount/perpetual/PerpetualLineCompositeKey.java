package com.tekclover.wms.api.transaction.model.cyclecount.perpetual;

import java.io.Serializable;

import lombok.Data;

@Data
public class PerpetualLineCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `C_ID`, `PLANT_ID`, `WH_ID`, `CC_NO`, `ST_BIN`, `ITM_CODE`, `PACK_BARCODE`
	 */
	private String languageId;
	private String companyCodeId;
	private String plantId;
	private String warehouseId;
	private String cycleCountNo;
	private String storageBin;
	private String itemCode;
	private String packBarcodes;
	private String manufacturerName;
	private Long lineNo;
}
