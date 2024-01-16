package com.mnrclara.api.accounting.model.prebill;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class PreBillDetailsCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `PRE_BILL_BATCH_NO` , `PRE_BILL_NO` , `PRE_BILL_DATE` , `MATTER_NO`
	 */
	private String preBillBatchNumber;
	private String preBillNumber;
	private Date preBillDate;
	private String matterNumber;
}
