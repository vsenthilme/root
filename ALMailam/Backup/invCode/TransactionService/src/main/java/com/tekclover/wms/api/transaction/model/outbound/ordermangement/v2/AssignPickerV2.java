package com.tekclover.wms.api.transaction.model.outbound.ordermangement.v2;

import com.tekclover.wms.api.transaction.model.outbound.ordermangement.AssignPicker;
import lombok.Data;

@Data
public class AssignPickerV2 extends AssignPicker {

	private String languageId;
	private String companyCodeId;
	private String plantId;

}