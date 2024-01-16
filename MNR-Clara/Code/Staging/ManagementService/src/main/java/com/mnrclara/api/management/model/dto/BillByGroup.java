package com.mnrclara.api.management.model.dto;

import java.util.Date;
import java.util.List;

import com.mnrclara.api.management.model.mattergeneral.MatterGenAcc;

import lombok.Data;

@Data
public class BillByGroup {
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
	
	// For other purpose added
	private Boolean isIndividual;
	private List<MatterGenAcc> matterGeneral;
}
