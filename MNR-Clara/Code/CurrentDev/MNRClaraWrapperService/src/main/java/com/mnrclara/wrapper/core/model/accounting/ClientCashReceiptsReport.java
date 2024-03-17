package com.mnrclara.wrapper.core.model.accounting;

import lombok.Data;

import java.util.Date;

@Data
public class ClientCashReceiptsReport {
	String matterNumber;
	String matterText;
	String clientId;
	String clientName;
	Date paymentDate;
	Double paymentAmount;
}
