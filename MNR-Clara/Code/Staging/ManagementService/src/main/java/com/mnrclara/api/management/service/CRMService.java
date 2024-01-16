package com.mnrclara.api.management.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.mnrclara.api.management.model.mattertask.NotificationSave;
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
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.mnrclara.api.management.config.PropertiesConfig;
import com.mnrclara.api.management.model.dto.AddNotes;
import com.mnrclara.api.management.model.dto.Agreement;
import com.mnrclara.api.management.model.dto.Notes;
import com.mnrclara.api.management.model.dto.PotentialClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CRMService {

	@Autowired
	PropertiesConfig propertiesConfig;

	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	private String getCRMServiceApiUrl() {
		return propertiesConfig.getCrmServiceUrl();
	}
	
	/**
	 * 
	 * @param potentialClientId
	 * @param authToken
	 * @return
	 */
	public PotentialClient getPotentialClient (String potentialClientId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCRMServiceApiUrl() + "potentialClient/" + potentialClientId);
					
			ResponseEntity<PotentialClient> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, PotentialClient.class);
			log.info("result : " + result);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	} 
	
	/**
	 * 
	 * @param agreementCode
	 * @return
	 */
	public Agreement getAgreementByPotentialClientId(String potentialClientId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCRMServiceApiUrl() + "agreement/" + potentialClientId + "/potentialClientId");
					
			ResponseEntity<Agreement> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, Agreement.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 
	 * @param potentialClientId
	 * @param loginUserID
	 * @param status
	 * @param authToken
	 * @return
	 */
	public PotentialClient updatePotentialClientStatus(String potentialClientId, String loginUserID, Long status,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCRMServiceApiUrl() + "/potentialClient/" + potentialClientId + "/status")
					.queryParam("loginUserID", loginUserID).queryParam("status", status);

			ResponseEntity<PotentialClient> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
					entity, PotentialClient.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public Notes createNotes(AddNotes addNotes, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceApiUrl() + "/notes")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(addNotes, headers);
			ResponseEntity<Notes> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity,
					Notes.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public Notes getNotes (String pcNoteNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCRMServiceApiUrl() + "/notes/" + pcNoteNumber);
			ResponseEntity<Notes> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, Notes.class);
//			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
//			e.printStackTrace();
			throw e;
		}
	}

	public void setNotificationMessage(String topic, String message, List<String> userId,
									   String userType, Date createdOn,String createdBy, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			NotificationSave notificationSave = new NotificationSave();
			notificationSave.setMessage(message);
			notificationSave.setTopic(topic);
			notificationSave.setUserId(userId);
			notificationSave.setUserType(userType);
			notificationSave.setCreatedOn(createdOn);
			notificationSave.setCreatedBy(createdBy);

			HttpEntity<?> entity = new HttpEntity<>(notificationSave,headers);

			UriComponents builder =
					UriComponentsBuilder.fromHttpUrl(getCRMServiceApiUrl() + "notification-message/create").build();

			getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,entity, Optional.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
