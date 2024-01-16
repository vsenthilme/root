package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class MetricsSummary {
	private Double totalOrder;
	private Double lineItems;
	private Double percentageShipped;
}
