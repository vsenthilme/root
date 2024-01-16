package com.tekclover.wms.api.transaction.model.inbound.staging.v2;

import com.tekclover.wms.api.transaction.model.inbound.staging.SearchStagingHeader;
import lombok.Data;

import java.util.List;

@Data
public class SearchStagingHeaderV2 extends SearchStagingHeader {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;
}
