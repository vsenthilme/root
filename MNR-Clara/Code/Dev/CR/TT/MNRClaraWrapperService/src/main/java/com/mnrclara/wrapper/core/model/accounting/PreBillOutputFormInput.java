package com.mnrclara.wrapper.core.model.accounting;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class PreBillOutputFormInput {

	 private List<String> matterNumbers;  
	 private Date startDateForPreBill;
	 private Date feesCostCutoffDate;  
	 private Date paymentCutoffDate;
}
