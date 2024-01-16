package com.tekclover.wms.core.model.warehouse.outbound;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class ReturnPO {
	
	@Valid
	private ReturnPOHeader returnPOHeader;
	
	@Valid
	private List<ReturnPOLine> returnPOLine;
}
