package com.mnrclara.api.accounting.model.quotation;

import java.io.Serializable;

import lombok.Data;

@Data
public class QuotationHeaderCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `QUTOE_NO` , `QUOTE_REV_NO`
	 */
	private String quotationNo;
	private Long quotationRevisionNo;
}
