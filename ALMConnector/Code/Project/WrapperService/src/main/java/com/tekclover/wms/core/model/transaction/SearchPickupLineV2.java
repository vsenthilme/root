package com.tekclover.wms.core.model.transaction;

import lombok.Data;

import java.util.List;

@Data
public class SearchPickupLineV2 extends SearchPickupLine {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;

}