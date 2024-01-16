package com.tekclover.wms.api.transaction.model.cyclecount.perpetual.v2;

import com.tekclover.wms.api.transaction.model.cyclecount.perpetual.SearchPerpetualHeader;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
public class SearchPerpetualHeaderV2 extends SearchPerpetualHeader {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;
	
}