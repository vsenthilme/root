package com.tekclover.wms.api.transaction.model.warehouse.inbound.v2;

import com.tekclover.wms.api.transaction.model.warehouse.inbound.InboundOrder;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.Set;
@Entity
@Data
public class InboundOrderV2 extends InboundOrder {

    private String branchCode;
    private String companyCode;
	private String returnOrderReference;

    private String transferOrderNumber;

    //MiddleWare Fields
    private Long middlewareId;
    private String middlewareTable;

    private String isCompleted;
    private Date updatedOn;
    private String isCancelled;

	@OneToMany(cascade = CascadeType.ALL,mappedBy = "orderId",fetch = FetchType.EAGER)
    private Set<InboundOrderLinesV2> line;
	
}