package com.tekclover.wms.api.transaction.model.outbound.ordermangement.v2;

import com.tekclover.wms.api.transaction.model.outbound.ordermangement.AssignPicker;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class AssignPickerV2 extends AssignPicker {

	private String languageId;
	private String companyCodeId;
	private String plantId;

}