package com.mnrclara.api.management.model.mattergeneral;

import lombok.Data;

@Data
public class WIPAgedPBReport {

	private Long classId;
	private String billingModeId;
	private String clientNumber;
	private String clientName;
	private String matterNumber;
	private String matterName;
	private Double priorBalance;
	private String partners;
	private String responsibleAttorneys;
	private Current current;
	private From30To60Days from30To60Days;
	private From61To90Days from61To90Days;
	private From91To120DDays from91To120DDays;
	private Over120Days over120Days;
	
	@Data
	public class Current {
		private Double fees;
		private Double timeTicketHours;
		private Double hardCost;
		private Double softCosts;
	}
	
	@Data
	public class From30To60Days {
		private Double fees;
		private Double timeTicketHours;
		private Double hardCost;
		private Double softCosts;
	}
	
	@Data
	public class From61To90Days {
		private Double fees;
		private Double timeTicketHours;
		private Double hardCost;
		private Double softCosts;
	}
	
	@Data
	public class From91To120DDays {
		private Double fees;
		private Double timeTicketHours;
		private Double hardCost;
		private Double softCosts;
	}
	
	@Data
	public class Over120Days {
		private Double fees;
		private Double timeTicketHours;
		private Double hardCost;
		private Double softCosts;
	}
}
