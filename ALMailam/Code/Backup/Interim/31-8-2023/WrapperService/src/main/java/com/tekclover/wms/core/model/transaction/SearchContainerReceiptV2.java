package com.tekclover.wms.core.model.transaction;

import lombok.Data;

import java.util.List;

@Data
public class SearchContainerReceiptV2 extends SearchContainerReceipt {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;
}
