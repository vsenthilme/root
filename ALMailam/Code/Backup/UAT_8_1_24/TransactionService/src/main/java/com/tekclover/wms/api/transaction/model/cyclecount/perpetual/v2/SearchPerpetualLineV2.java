package com.tekclover.wms.api.transaction.model.cyclecount.perpetual.v2;

import com.tekclover.wms.api.transaction.model.cyclecount.perpetual.SearchPerpetualLine;
import lombok.Data;

import java.util.List;

@Data
public class SearchPerpetualLineV2 extends SearchPerpetualLine {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;
}