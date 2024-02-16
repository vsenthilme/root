package com.mnrclara.wrapper.core.model.accounting.report;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ClientCashReceiptsModel {

	private List<String> clientId;
	private List<String> matterNumber;
	private Date fromDate;
	private Date toDate;

	private List<String> timeKeeperCode;
}
