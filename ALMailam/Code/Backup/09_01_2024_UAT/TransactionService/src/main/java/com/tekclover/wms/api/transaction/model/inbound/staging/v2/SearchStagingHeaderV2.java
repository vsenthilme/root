package com.tekclover.wms.api.transaction.model.inbound.staging.v2;

import com.tekclover.wms.api.transaction.model.inbound.staging.SearchStagingHeader;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
public class SearchStagingHeaderV2 extends SearchStagingHeader {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;
}
