package com.tekclover.wms.api.transaction.model.cyclecount.periodic.v2;

import com.tekclover.wms.api.transaction.model.cyclecount.periodic.SearchPeriodicHeader;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
public class SearchPeriodicHeaderV2 extends SearchPeriodicHeader {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;
}