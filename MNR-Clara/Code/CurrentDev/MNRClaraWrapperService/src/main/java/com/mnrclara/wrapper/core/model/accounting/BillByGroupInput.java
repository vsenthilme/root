package com.mnrclara.wrapper.core.model.accounting;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class BillByGroupInput {
	
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
	private Date preBillDate;
	private Date startDate;
	private Date feesCutoffDate;
	private Date paymentCutoffDate;
}
