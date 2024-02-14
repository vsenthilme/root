package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class MobileDashboard {
	private InboundCount inboundCount;
	private OutboundCount outboundCount;
	private StockCount stockCount;
	
	@Data
	public class InboundCount {
		private Long cases;
		private Long putaway;
		private Long reversals;
	}
	
	@Data
	public class OutboundCount {
		private Long picking;
		private Long quality;
		private Long reversals;
		private Long tracking;
	}
	
	@Data
	public class StockCount {
		private Long perpertual;
		private Long periodic;
	}
}