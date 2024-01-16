package com.tekclover.wms.api.transaction.model.outbound.preoutbound.v2;

import com.tekclover.wms.api.transaction.model.outbound.preoutbound.SearchPreOutboundHeader;
import lombok.Data;

import java.util.List;

@Data
public class SearchPreOutboundHeaderV2 extends SearchPreOutboundHeader {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;

}