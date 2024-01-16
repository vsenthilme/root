package com.tekclover.wms.api.masters.model.handlingunit;

import java.io.Serializable;

import lombok.Data;

@Data
public class HandlingUnitCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `LANG_ID`, `C_ID`, `PLANT_ID`, `WH_ID`, `HU_UNIT`
	 */
	private String languageId;
	private String companyCodeId;
	private String plantId;
	private String warehouseId;
	private String handlingUnit;
}
