package com.tekclover.wms.api.transaction.model.inbound.putaway.v2;

import com.tekclover.wms.api.transaction.model.inbound.putaway.SearchPutAwayLine;
import lombok.Data;

import java.util.List;

@Data
public class SearchPutAwayLineV2 extends SearchPutAwayLine {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;

	private List<String> barcodeId;
	private List<String> manufacturerCode;
	private List<String> manufacturerName;
	private List<String> origin;
	private List<String> brand;
}
