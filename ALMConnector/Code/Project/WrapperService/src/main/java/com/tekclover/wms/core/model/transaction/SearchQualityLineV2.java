package com.tekclover.wms.core.model.transaction;

import lombok.Data;

import java.util.List;

@Data
public class SearchQualityLineV2 extends SearchQualityLine {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;

}