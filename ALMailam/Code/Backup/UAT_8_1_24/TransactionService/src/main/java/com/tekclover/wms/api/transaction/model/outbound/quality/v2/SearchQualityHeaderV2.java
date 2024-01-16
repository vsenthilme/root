package com.tekclover.wms.api.transaction.model.outbound.quality.v2;

import com.tekclover.wms.api.transaction.model.outbound.quality.SearchQualityHeader;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
public class SearchQualityHeaderV2 extends SearchQualityHeader {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;
	
}