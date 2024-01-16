package com.mnrclara.api.accounting.model.reports;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ClientCashReceipts {

	private List<String> clientId;
	private List<String> matterNumber;
	private Date fromDate;
	private Date toDate;

	private List<String> timeKeeperCode;
}
