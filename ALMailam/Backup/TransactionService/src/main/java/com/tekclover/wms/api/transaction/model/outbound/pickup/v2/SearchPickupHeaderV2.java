package com.tekclover.wms.api.transaction.model.outbound.pickup.v2;

import com.tekclover.wms.api.transaction.model.outbound.pickup.SearchPickupHeader;
import lombok.Data;

import java.util.List;

@Data
public class SearchPickupHeaderV2 extends SearchPickupHeader {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;

}