package com.tekclover.wms.api.transaction.model.inbound.v2;

import com.tekclover.wms.api.transaction.model.inbound.SearchInboundLine;
import lombok.Data;

import java.util.List;

@Data
public class SearchInboundLineV2 extends SearchInboundLine {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;
}
