package com.tekclover.wms.api.transaction.model.warehouse.outbound.v2;

import com.tekclover.wms.api.transaction.model.warehouse.outbound.ReturnPOHeader;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.ReturnPOLine;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class ReturnPOV2 {
	
	@Valid
	private ReturnPOHeaderV2 returnPOHeader;
	
	@Valid
	private List<ReturnPOLineV2> returnPOLine;
}
