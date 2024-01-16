package com.mnrclara.api.crm.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.mnrclara.api.crm.config.PropertiesConfig;
import com.mnrclara.api.crm.model.dto.MatterGenAcc;
import com.mnrclara.api.crm.model.potentialclient.AddClientGeneral;
import com.mnrclara.api.crm.model.potentialclient.ClientGeneral;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ManagementService {

	@Autowired
	PropertiesConfig propertiesConfig;

	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	private String getManagementServiceUrl() {
		return propertiesConfig.getManagementServiceUrl();
	}

	/**
	 * 
	 * @param addClientGeneral
	 * @param loginUserID
	 * @param authToken
	 * @return
	 */
	public ClientGeneral createClientGeneral (AddClientGeneral addClientGeneral, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(addClientGeneral, headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "clientgeneral")
					.queryParam("loginUserID", loginUserID)
					.queryParam("isFromPotentialEndpoint", Boolean.TRUE);
			ResponseEntity<ClientGeneral> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity,
					ClientGeneral.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// FIND - ClientGeneral
	public ClientGeneral[] findClientGeneralRecords (String fullTextSearch, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "clientgeneral/findRecords")
						.queryParam("fullTextSearch", fullTextSearch);
			ResponseEntity<ClientGeneral[]> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ClientGeneral[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public ClientGeneral getClientGeneral(String clientgeneralId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "clientgeneral/" + clientgeneralId);
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
	
	// FIND - MatterGeneral
	public MatterGenAcc[] findMatterGeneralRecords (String fullTextSearch, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "mattergenacc/findRecords")
						.queryParam("fullTextSearch", fullTextSearch);
			ResponseEntity<MatterGenAcc[]> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, MatterGenAcc[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public MatterGenAcc getMatterGenAcc(String matterNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterGenAcc-Agent", "MNRClara RestTemplate");
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
}
