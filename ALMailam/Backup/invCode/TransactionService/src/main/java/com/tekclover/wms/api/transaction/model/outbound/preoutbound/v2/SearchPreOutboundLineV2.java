package com.tekclover.wms.api.transaction.model.outbound.preoutbound.v2;

import com.tekclover.wms.api.transaction.model.outbound.preoutbound.SearchPreOutboundLine;
import lombok.Data;

import java.util.List;

@Data
public class SearchPreOutboundLineV2 extends SearchPreOutboundLine {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;

}