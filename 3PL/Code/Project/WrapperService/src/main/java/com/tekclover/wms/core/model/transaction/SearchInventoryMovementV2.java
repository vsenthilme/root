package com.tekclover.wms.core.model.transaction;

import lombok.Data;

import java.util.List;

@Data
public class SearchInventoryMovementV2 extends SearchInventoryMovement {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;

}
