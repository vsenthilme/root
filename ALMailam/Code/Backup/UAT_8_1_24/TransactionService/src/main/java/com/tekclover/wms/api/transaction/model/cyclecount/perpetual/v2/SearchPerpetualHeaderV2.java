package com.tekclover.wms.api.transaction.model.cyclecount.perpetual.v2;

import com.tekclover.wms.api.transaction.model.cyclecount.perpetual.SearchPerpetualHeader;
import lombok.Data;

import java.util.List;

@Data
public class SearchPerpetualHeaderV2 extends SearchPerpetualHeader {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;
	
}