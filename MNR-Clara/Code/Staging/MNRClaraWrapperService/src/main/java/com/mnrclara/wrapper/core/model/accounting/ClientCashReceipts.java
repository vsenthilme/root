package com.mnrclara.wrapper.core.model.accounting;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ClientCashReceipts {

	private List<String> clientId;
	private List<String> matterNumber;
	private String fromDate;
	private String toDate;

	private List<String> timeKeeperCode;
}
