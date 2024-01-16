package com.mnrclara.wrapper.core.model.accounting;

import java.util.List;

import lombok.Data;

@Data
public class BillByGroup {
	
	private List<Long> classId;
	private List<String> clientId;
	private List<Long> caseCategory;
	private List<Long> caseSubCategory;
	private List<Long> billingMode;
	private List<Long> billingFrequency;
	private List<Long> billingFormatCode;
	private List<String> matterNumber;
	private List<String> originatingTimeKeeper;
	private List<String> responsibleTimeKeeper;
	private List<String> assignedTimeKeeper;
	
	private String preBillDate;
	private String startDate;
	private String feesCutoffDate;
	private String paymentCutoffDate;
//	private Date preBillDate;
//	private Date startDate;
//	private Date feesCutoffDate;
//	private Date paymentCutoffDate;
}
