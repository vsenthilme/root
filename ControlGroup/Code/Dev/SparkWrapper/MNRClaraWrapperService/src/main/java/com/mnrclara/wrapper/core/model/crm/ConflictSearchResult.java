package com.mnrclara.wrapper.core.model.crm;

import java.util.List;

import com.mnrclara.wrapper.core.model.accounting.InvoiceHeader;
import com.mnrclara.wrapper.core.model.management.MatterGenAcc;

import lombok.Data;

@Data
public class ConflictSearchResult {

	private List<PotentialClient> potentialClients;
	private List<Agreement> agreements;
	private List<ClientGeneral> clientGenerals;
	private List<MatterGenAcc> matterGenerals;
	private List<InvoiceHeader> invoices;
}
