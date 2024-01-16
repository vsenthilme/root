package com.tekclover.wms.api.transaction.model.inbound.v2;

import com.tekclover.wms.api.transaction.model.inbound.SearchInboundHeader;
import lombok.Data;

import java.util.List;

@Data
public class SearchInboundHeaderV2 extends SearchInboundHeader {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;
}
