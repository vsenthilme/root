package com.tekclover.wms.api.transaction.model.outbound.quality.v2;

import com.tekclover.wms.api.transaction.model.outbound.quality.SearchQualityLine;
import lombok.Data;

import java.util.List;

@Data
public class SearchQualityLineV2 extends SearchQualityLine {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;

}