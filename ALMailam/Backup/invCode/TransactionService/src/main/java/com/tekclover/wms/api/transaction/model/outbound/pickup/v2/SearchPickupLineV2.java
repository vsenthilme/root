package com.tekclover.wms.api.transaction.model.outbound.pickup.v2;

import com.tekclover.wms.api.transaction.model.outbound.pickup.SearchPickupLine;
import lombok.Data;

import java.util.List;

@Data
public class SearchPickupLineV2 extends SearchPickupLine {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;

}