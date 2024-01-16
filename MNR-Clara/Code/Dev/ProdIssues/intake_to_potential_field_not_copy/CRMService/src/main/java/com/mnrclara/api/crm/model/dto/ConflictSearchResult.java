package com.mnrclara.api.crm.model.dto;

import java.util.List;

import com.mnrclara.api.crm.model.potentialclient.ClientGeneral;
import com.mnrclara.api.crm.model.potentialclient.PotentialClient;

import lombok.Data;

@Data
public class ConflictSearchResult {

	private List<PotentialClient> potentialClients;
	private List<com.mnrclara.api.crm.model.agreement.Agreement> agreements;
	private List<ClientGeneral> clientGenerals;
	private List<MatterGenAcc> matterGenerals;
	private List<InvoiceHeader> invoices;
}
