package com.mnrclara.api.management.model.receiptappnotice;

import java.io.Serializable;

import lombok.Data;

@Data
public class ReceiptAppNoticeCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	private String languageId;
	private Long classId;
	private String matterNumber;
	private String receiptNo;
}
