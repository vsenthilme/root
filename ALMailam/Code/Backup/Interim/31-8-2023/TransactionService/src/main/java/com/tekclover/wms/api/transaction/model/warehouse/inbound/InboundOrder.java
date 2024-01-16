package com.tekclover.wms.api.transaction.model.warehouse.inbound;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import lombok.Data;
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Table(name = "tbliborder")
@Data
public class InboundOrder {
	
	@Id
	@Column(name = "order_id", nullable = false)
	protected String orderId;

	protected String refDocumentNo; 			// REF_DOC_NO
	protected String refDocumentType; 		// REF_DOC_TYPE
	protected String warehouseID; 			// WH_ID
	protected Long inboundOrderTypeId; 		// IB_ORD_TYP_ID
	protected Date orderReceivedOn;
	protected Date orderProcessedOn;
	protected Long processedStatusId;
	
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "orderId",fetch = FetchType.EAGER)
    protected Set<InboundOrderLines> lines;
	
	// For ALM Orders
//	protected String branchCode;
//    protected String companyCode;
	
}