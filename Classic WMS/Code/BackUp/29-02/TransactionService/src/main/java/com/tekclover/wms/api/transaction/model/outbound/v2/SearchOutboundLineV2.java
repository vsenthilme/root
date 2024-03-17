package com.tekclover.wms.api.transaction.model.outbound.v2;

import com.tekclover.wms.api.transaction.model.outbound.SearchOutboundLine;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
public class SearchOutboundLineV2 extends SearchOutboundLine {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;
	private List<String> manufacturerName;
	private List<String> targetBranchCode;
	private List<String> salesOrderNumber;
}