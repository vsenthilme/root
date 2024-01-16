package com.tekclover.wms.core.model.transaction;

import lombok.Data;

import java.util.List;

@Data
public class SearchStagingLineV2 extends SearchStagingLine {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;

	private List<String> manufacturerCode;
	private List<String> manufacturerName;
	private List<String> origin;
	private List<String> brand;
}
