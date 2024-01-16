package com.tekclover.wms.api.transaction.model.cyclecount.periodic.v2;

import com.tekclover.wms.api.transaction.model.cyclecount.periodic.SearchPeriodicLine;
import lombok.Data;

import java.util.List;

@Data
public class SearchPeriodicLineV2 extends SearchPeriodicLine {

	private List<String> languageId;
	private List<String> companyCodeId;
//	private List<String> plantId;
}