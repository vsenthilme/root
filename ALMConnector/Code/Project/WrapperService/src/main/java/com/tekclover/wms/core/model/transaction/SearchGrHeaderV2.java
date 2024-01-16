package com.tekclover.wms.core.model.transaction;

import lombok.Data;

import java.util.List;

@Data
public class SearchGrHeaderV2 extends SearchGrHeader {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;
}
