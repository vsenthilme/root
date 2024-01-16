package com.mnrclara.api.crm.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.crm.config.PropertiesConfig;
import com.mnrclara.api.crm.model.agreement.Agreement;
import com.mnrclara.api.crm.model.auth.AuthToken;
import com.mnrclara.api.crm.model.dto.ConflictSearchResult;
import com.mnrclara.api.crm.model.dto.InvoiceHeader;
import com.mnrclara.api.crm.model.dto.MatterGenAcc;
import com.mnrclara.api.crm.model.potentialclient.ClientGeneral;
import com.mnrclara.api.crm.model.potentialclient.PotentialClient;
import com.mnrclara.api.crm.repository.InquiryRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConflictCheckService {
	
	@Autowired
	PropertiesConfig propertiesConfig;
	
	@Autowired
	InquiryRepository inquiryRepository;
	
	@Autowired
	NotesService notesService;
	
	@Autowired
	PotentialClientService potentialClientService;
	
	@Autowired
	AgreementService agreementService;
	
	@Autowired
	ManagementService managementService;
	
	@Autowired
	AccountingService accountingService;
	
	@Autowired
	AuthTokenService authTokenService;
	
	private enum SearchTableNames {
		POTENTIALCLIENT,
		AGREEMENT,
		CLIENT,
		MATTER,
		INVOICE,
		ALL;
	};
	
	/**
	 * getInquirys
	 * @param searchFieldValue 
	 * @param searchTableNames 
	 * @return
	 */
	public ConflictSearchResult findRecords (List<String> searchTableNames, String searchFieldValue) {
		ConflictSearchResult searchResult = new ConflictSearchResult();
		log.info("searchTableNames : " + searchTableNames);
		
		if (searchTableNames.contains("ALL".toLowerCase())) {
			searchResult = searchPotentialClient (searchResult, searchFieldValue);
			log.info("searchPotentialClient : " + searchResult);
			
			searchResult = searchAgreement (searchResult, searchFieldValue);
			log.info("searchAgreement : " + searchResult);
			
			searchResult = searchClientGeneral (searchResult, searchFieldValue);
			log.info("searchClientGeneral : " + searchResult);
			
			searchResult = searchMatterGeneral (searchResult, searchFieldValue);
			log.info("searchMatterGeneral : " + searchResult);
			
			searchResult = searchInvoiceHeader (searchResult, searchFieldValue);
			log.info("searchInvoiceHeader : " + searchResult);
			return searchResult;
		}
		
		for (String tableName : searchTableNames) {
			log.info("tableName : " + searchTableNames);
			
			if (tableName.equalsIgnoreCase(SearchTableNames.POTENTIALCLIENT.name())) {
				searchResult = searchPotentialClient (searchResult, searchFieldValue);
			} else if (tableName.equalsIgnoreCase(SearchTableNames.AGREEMENT.name())) {
				searchResult = searchAgreement (searchResult, searchFieldValue);
			} else if (tableName.equalsIgnoreCase(SearchTableNames.CLIENT.name())) {
				searchResult = searchClientGeneral (searchResult, searchFieldValue);
			} else if (tableName.equalsIgnoreCase(SearchTableNames.MATTER.name())) {
				searchResult = searchMatterGeneral (searchResult, searchFieldValue);
			} else if (tableName.equalsIgnoreCase(SearchTableNames.INVOICE.name())) {
				searchResult = searchInvoiceHeader (searchResult, searchFieldValue);
			}
		}
		return searchResult;
	}
	
	/**
	 * searchPotentialClient
	 * @param searchResult
	 * @return
	 */
	private ConflictSearchResult searchPotentialClient (ConflictSearchResult searchResult, String searchFieldValue) {
		List<PotentialClient> potentialClientList = potentialClientService.findRecords(searchFieldValue);
		searchResult.setPotentialClients(potentialClientList);
		log.info("searchPotentialClient : " + potentialClientList);
		return searchResult;
	}
	
	/**
	 * 
	 * @param searchResult
	 * @param searchFieldValue
	 * @return
	 */
	private ConflictSearchResult searchAgreement (ConflictSearchResult searchResult, String searchFieldValue) {
		List<Agreement> agreements = agreementService.findRecords(searchFieldValue);
		searchResult.setAgreements(agreements);
		log.info("searchAgreement : " + agreements);
		return searchResult;
	}
	
	/**
	 * 
	 * @param searchResult
	 * @param searchFieldValue
	 * @return
	 */
	private ConflictSearchResult searchClientGeneral (ConflictSearchResult searchResult, String searchFieldValue) {
		AuthToken authToken = authTokenService.getManagementServiceAuthToken();
		ClientGeneral[] clientGenerals = 
				managementService.findClientGeneralRecords(searchFieldValue, authToken.getAccess_token());
		searchResult.setClientGenerals(Arrays.asList(clientGenerals));
		log.info("clientGenerals : " + clientGenerals);
		return searchResult;
	}
	
	/**
	 * 
	 * @param searchResult
	 * @param searchFieldValue
	 * @return
	 */
	private ConflictSearchResult searchMatterGeneral (ConflictSearchResult searchResult, String searchFieldValue) {
		try {
			AuthToken authToken = authTokenService.getManagementServiceAuthToken();
			ClientGeneral[] clientGenerals = 
					managementService.findClientGeneralRecords(searchFieldValue, authToken.getAccess_token());
			List<MatterGenAcc> matterGenAccList = new ArrayList<>();
			for (ClientGeneral clientGeneral : clientGenerals) {
				MatterGenAcc[] matterGenAccs = 
						managementService.findMatterGeneralRecords(clientGeneral.getClientId(), authToken.getAccess_token());
				log.info("matterGenAccs : " + Arrays.asList(matterGenAccs));
				if (matterGenAccs != null && matterGenAccs.length > 0 ) {
					for (MatterGenAcc matterGenAcc : matterGenAccs) {
//						authToken = authTokenService.getManagementServiceAuthToken();
//						ClientGeneral clientGeneral = 
//								managementService.getClientGeneral(matterGenAcc.getClientId(), authToken.getAccess_token());
						/*
						 * first name, last name, first last name and emailID 
						 */
						matterGenAcc.setReferenceField21(clientGeneral.getFirstName());
						matterGenAcc.setReferenceField22(clientGeneral.getLastName());
						matterGenAcc.setReferenceField23(clientGeneral.getFirstNameLastName());
						matterGenAcc.setReferenceField24(clientGeneral.getEmailId());
						matterGenAccList.add(matterGenAcc);
					}
				}
			}
			searchResult.setMatterGenerals(matterGenAccList);
			return searchResult;
		} catch (Exception e) {
			log.info("searchMatterGeneral error: " + e);
			e.printStackTrace();
		}
		return searchResult;
	}
	
	/**
	 * 
	 * @param searchResult
	 * @param searchFieldValue
	 * @return
	 */
	private ConflictSearchResult searchInvoiceHeader (ConflictSearchResult searchResult, String searchFieldValue) {
		AuthToken authToken = authTokenService.getAccountingServiceAuthToken();
		InvoiceHeader[] invoiceHeaders = accountingService.findInvoiceHeaderRecords(searchFieldValue, authToken.getAccess_token());
		
		List<InvoiceHeader> invoiceHeaderList = new ArrayList<>();
		for (InvoiceHeader invoiceHeader : invoiceHeaders) {
			authToken = authTokenService.getManagementServiceAuthToken();
			ClientGeneral clientGeneral = 
					managementService.getClientGeneral(invoiceHeader.getClientId(), authToken.getAccess_token());
			/*
			 * first name, last name, first last name and emailID 
			 */
			invoiceHeader.setReferenceField21(clientGeneral.getFirstName());
			invoiceHeader.setReferenceField22(clientGeneral.getLastName());
			invoiceHeader.setReferenceField23(clientGeneral.getFirstNameLastName());
			invoiceHeader.setReferenceField24(clientGeneral.getEmailId());
			invoiceHeaderList.add(invoiceHeader);
		}
		searchResult.setInvoices(invoiceHeaderList);
		return searchResult;
	}
}
