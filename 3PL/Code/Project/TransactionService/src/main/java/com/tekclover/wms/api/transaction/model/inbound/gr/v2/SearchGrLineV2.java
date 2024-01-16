package com.tekclover.wms.api.transaction.model.inbound.gr.v2;

import com.tekclover.wms.api.transaction.model.inbound.gr.SearchGrLine;
import lombok.Data;

import java.util.List;

@Data
public class SearchGrLineV2 extends SearchGrLine {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;
	private List<String> warehouseId;

	private List<String> barcodeId;
	private List<String> manufacturerCode;
	private List<String> origin;
	private List<String> interimStorageBin;
	private List<String> manufacturerName;
	private List<String> brand;
	private List<String> rejectType;
	private List<String> rejectReason;
}
