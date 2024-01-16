package com.tekclover.wms.api.transaction.model.outbound.v2;

import com.tekclover.wms.api.transaction.model.outbound.SearchOutboundHeader;
import lombok.Data;

import java.util.List;

@Data
public class SearchOutboundHeaderV2 extends SearchOutboundHeader {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;

}