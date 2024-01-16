package com.tekclover.wms.api.transaction.model.warehouse.inbound.v2;

import lombok.Data;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.InboundOrderLines;

import javax.persistence.*;

@Entity
@Data
public class InboundOrderLinesV2 extends InboundOrderLines{

	private String manufacturerCode;
	private String origin;
	private String supplierName;
	private String brand;
	private Double packQty;
	private String fromCompanyCode;
	private Double expectedQty;

	private String sourceBranchCode;
	private String countryOfOrigin;
}
