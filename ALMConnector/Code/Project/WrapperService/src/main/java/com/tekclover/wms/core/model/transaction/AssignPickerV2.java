package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class AssignPickerV2 extends AssignPicker {

	private String languageId;
	private String companyCodeId;
	private String plantId;

}