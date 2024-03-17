package com.mnrclara.api.accounting.service;

import java.util.Collections;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.mnrclara.api.accounting.config.PropertiesConfig;
import com.mnrclara.api.accounting.model.management.ClientGeneral;
import com.mnrclara.api.accounting.model.management.MatterAssignment;
import com.mnrclara.api.accounting.model.management.MatterExpense;
import com.mnrclara.api.accounting.model.management.MatterGenAcc;
import com.mnrclara.api.accounting.model.management.MatterTimeTicket;
import com.mnrclara.api.accounting.model.prebill.BillByGroup;
import com.mnrclara.api.accounting.model.prebill.MatterTimeExpenseTicket;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ManagementService {

	@Autowired
	PropertiesConfig propertiesConfig;
	
	/**
	 * 
	 * @return
	 */
	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	/**
	 * 
	 * @return
	 */
	private String getManagementServiceUrl() {
		return propertiesConfig.getManagementServiceUrl();
	}
	
	// GET
	public MatterGenAcc getMatterGenAcc(String matterNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/" + matterNumber);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterGenAcc> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterGenAcc.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public MatterGenAcc[] getMatterGenAcc(List<String> matterNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/outputForm")
					.queryParam("matterNumber", matterNumber);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterGenAcc[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterGenAcc[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public MatterAssignment getMatterAssignment(String matterNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterAssignment-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterassignment/" + matterNumber);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterAssignment> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterAssignment.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET 
	public ClientGeneral getClientGeneral(String clientId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientgeneral/" + clientId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ClientGeneral> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ClientGeneral.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//---------------------AccountingService-----From------ManagementServce----------------------------
	// POST - /accounting/prebill/group
	public MatterTimeExpenseTicket[] getMatterTimeNExpenseTicketsByGroup (BillByGroup newBillByGroup, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/accounting/prebill/group");
			HttpEntity<?> entity = new HttpEntity<>(newBillByGroup, headers);
			ResponseEntity<MatterTimeExpenseTicket[]> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, MatterTimeExpenseTicket[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST - /accounting/prebill/individual
	public MatterTimeExpenseTicket[] getMatterTimeNExpenseTicketsByIndividual (BillByGroup newBillByGroup, String authToken) {
		try {
			log.info("\n----getMatterTimeNExpenseTicketsByIndividual::------> " + newBillByGroup);
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/accounting/prebill/individual");
			HttpEntity<?> entity = new HttpEntity<>(newBillByGroup, headers);
			ResponseEntity<MatterTimeExpenseTicket[]> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, MatterTimeExpenseTicket[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public MatterTimeTicket updateMatterTimeTicket (String timeTicketNumber, String loginUserID, 
			MatterTimeTicket modifiedMatterTimeTicket, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedMatterTimeTicket, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "/mattertimeticket/" + timeTicketNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<MatterTimeTicket> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, MatterTimeTicket.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public MatterTimeTicket updateMatterTimeTicket (String timeTicketNumber, String loginUserID, 
			com.mnrclara.api.accounting.model.prebill.MatterTimeTicket matterTimeTicket, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(matterTimeTicket, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "/mattertimeticket/" + timeTicketNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<MatterTimeTicket> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, MatterTimeTicket.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public MatterExpense updateMatterExpense (Long matterExpenseId, String loginUserID, 
			com.mnrclara.api.accounting.model.prebill.MatterExpense matterExpense, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(matterExpense, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "/matterexpense/" + matterExpenseId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<MatterExpense> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, MatterExpense.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public MatterExpense updateMatterExpense (String matterExpenseId, String loginUserID, 
			com.mnrclara.api.accounting.model.prebill.MatterExpense matterExpense, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(matterExpense, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "/matterexpense/" + matterExpenseId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<MatterExpense> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, MatterExpense.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public MatterExpense updateMatterGeneral(String matterNumber, String loginUserID, 
			MatterGenAcc modifiedMatterGeneral, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedMatterGeneral, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/" + matterNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<MatterExpense> result = 
					restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, MatterExpense.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
