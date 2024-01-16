package com.tekclover.wms.core.model.transaction;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class InboundOrder {
	
	protected String orderId;
	protected String refDocumentNo; 			// REF_DOC_NO
	protected String refDocumentType; 		// REF_DOC_TYPE
	protected String warehouseID; 			// WH_ID
	protected Long inboundOrderTypeId; 		// IB_ORD_TYP_ID
	protected Date orderReceivedOn;
	protected Date orderProcessedOn;
	protected Long processedStatusId;
    protected Set<InboundOrderLines> lines;
	
}