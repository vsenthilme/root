package com.tekclover.wms.api.transaction.model.inbound.inventory.v2;

import com.tekclover.wms.api.transaction.model.inbound.inventory.SearchInventoryMovement;
import lombok.Data;

import java.util.List;

@Data
public class SearchInventoryMovementV2 extends SearchInventoryMovement {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;

}
