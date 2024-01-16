package com.tekclover.wms.api.idmaster.model.rowid;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class AddRowId {

	private String companyCodeId;
	private String plantId;
	private String warehouseId;
	@NotNull(message = "Floor Id is mandatory")
	private Long floorId;
	private String storageSectionId;
	private String rowId;
	private String aisleId;
	private String languageId;
	private String rowNumber;
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

}
