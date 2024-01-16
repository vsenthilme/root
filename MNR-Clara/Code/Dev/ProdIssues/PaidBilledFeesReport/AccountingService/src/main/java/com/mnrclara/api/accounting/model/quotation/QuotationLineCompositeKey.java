package com.mnrclara.api.accounting.model.quotation;

import java.io.Serializable;

import lombok.Data;

@Data
public class QuotationLineCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `QUTOE_NO` , `QUTO_REV_NO` , `SERIAL_NO`
	 */
	private String quotationNo;
	private Long quotationRevisionNo;
	private Long serialNumber;
}
