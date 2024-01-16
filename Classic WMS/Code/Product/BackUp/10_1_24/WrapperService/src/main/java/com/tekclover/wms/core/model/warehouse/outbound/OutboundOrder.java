package com.tekclover.wms.core.model.warehouse.outbound;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "tbloborder")
@Data
public class OutboundOrder {

	@Id
	@Column(name = "order_id", nullable = false)
	private String orderId;
	
	private String warehouseID; 			// WH_ID
	private String refDocumentNo; 			// REF_DOCument_NO
	private String refDocumentType; 		// REF_DOC_TYPE
	private String partnerCode; 			// PARTNER_CODE
	private String partnerName; 			// PARTNER_NM
	private Date requiredDeliveryDate;		// REQ_DEL_DATE
	private String branchCode;
	private String companyCode;
	private String languageId;
	// Additional Fields
	private Date orderReceivedOn; 			// order_received_on
	private Date orderProcessedOn;
	private Long processedStatusId;			// processed_status_id
	private Long outboundOrderTypeID;
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "orderId",fetch = FetchType.EAGER)
    private Set<OutboundOrderLine> lines;
}


