package com.mnrclara.api.accounting.model.paymentplan;

import java.io.Serializable;

import lombok.Data;

@Data
public class PaymentPlanLineCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `PAYMENT_PLAN_NO` , `PLAN_REV_NO` , `ITEM_NO`
	 */
	private String paymentPlanNumber;
	private Long paymentPlanRevisionNo;
	private Long itemNumber;
}
