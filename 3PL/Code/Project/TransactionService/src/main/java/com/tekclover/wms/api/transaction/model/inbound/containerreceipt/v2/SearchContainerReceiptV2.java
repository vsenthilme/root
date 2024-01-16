package com.tekclover.wms.api.transaction.model.inbound.containerreceipt.v2;

import com.tekclover.wms.api.transaction.model.inbound.containerreceipt.SearchContainerReceipt;
import lombok.Data;

import java.util.List;

@Data
public class SearchContainerReceiptV2 extends SearchContainerReceipt {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;
}
