package com.tekclover.wms.api.transaction.model.outbound;

import com.tekclover.wms.api.transaction.model.outbound.SearchOutboundLineReport;
import lombok.Data;

import java.util.List;

@Data
public class SearchOutboundLineReportV2 extends SearchOutboundLineReport {
	
	private List<String> companyCodeId;
	private List<String> plantId;
	private List<String> languageId;
}
