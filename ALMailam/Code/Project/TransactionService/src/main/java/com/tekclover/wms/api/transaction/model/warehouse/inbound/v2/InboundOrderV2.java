package com.tekclover.wms.api.transaction.model.warehouse.inbound.v2;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.tekclover.wms.api.transaction.model.warehouse.inbound.InboundOrder;

import lombok.Data;
@Entity
@Data
public class InboundOrderV2 extends InboundOrder {

    private String branchCode;
    private String companyCode;
	private String returnOrderReference;

    private String transferOrderNumber;

	@OneToMany(cascade = CascadeType.ALL,mappedBy = "orderId",fetch = FetchType.EAGER)
    private Set<InboundOrderLinesV2> line;
	
}