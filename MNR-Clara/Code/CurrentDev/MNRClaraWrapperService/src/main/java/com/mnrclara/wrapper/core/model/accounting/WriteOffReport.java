package com.mnrclara.wrapper.core.model.accounting;

import lombok.Data;

import java.util.Date;

@Data
public class WriteOffReport {
	String clientId;
	String clientName;
	String matterNumber;
	String matterText;
	Date date;
	String responsibleTimeKeeper;
	Double fees;
	Double hardCosts;
	Double softCosts;
	Double taxes;
	Double lateCharges;
	Double total;
}
