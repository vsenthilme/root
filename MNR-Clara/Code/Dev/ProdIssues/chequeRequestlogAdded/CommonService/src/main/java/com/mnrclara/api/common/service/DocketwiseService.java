package com.mnrclara.api.common.service;

import java.util.Arrays;
import java.util.Collections;

import javax.validation.Valid;

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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.mnrclara.api.common.config.PropertiesConfig;
import com.mnrclara.api.common.model.docketwise.Contact;
import com.mnrclara.api.common.model.docketwise.CreateContact;
import com.mnrclara.api.common.model.docketwise.Matter;
import com.mnrclara.api.common.model.docketwise.UpdateContact;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DocketwiseService {
	
	@Autowired
	PropertiesConfig propertiesConfig;

	/**
	 * 
	 * @return
	 */
	public Contact[] getContacts () {
		try {
			String docketwiseBaseUri = propertiesConfig.getDocketwiseBaseUri();
			String docketwiseAccessToken = propertiesConfig.getDocketwiseAccessToken();
			
			String getContactsUrl = docketwiseBaseUri + "/contacts"; // "https://app.docketwise.com/api/v1/contacts";
			
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.add("Authorization", "Bearer " + docketwiseAccessToken);
			
			ResponseEntity<Contact[]> response = restTemplate.exchange(getContactsUrl, HttpMethod.GET, entity, Contact[].class);
			Contact[] contacts = response.getBody();
			log.info("Contacts Response ---------" + contacts[0].getFirst_name());
			return contacts;
		} catch (RestClientException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * createContact
	 * @param createContact
	 * @return
	 */
	public Contact createContact (@Valid CreateContact createContact) {
		try {
			String docketwiseBaseUri = propertiesConfig.getDocketwiseBaseUri();
			String docketwiseAccessToken = propertiesConfig.getDocketwiseAccessToken();
			String createContactUrl = docketwiseBaseUri + "/contacts"; // "https://app.docketwise.com/api/v1/contact";
			log.info("docketwiseAccessToken : " + docketwiseAccessToken);
			log.info("createContactUrl : " + createContactUrl);
			log.info("createContact : " + createContact);
			
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();			
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + docketwiseAccessToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createContactUrl);
			HttpEntity<?> entity = new HttpEntity<>(createContact, headers);
			
			ResponseEntity<Contact> result = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, Contact.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (RestClientException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public Contact updateContact (String contactId, UpdateContact updateContact) {
		try {
			String docketwiseBaseUri = propertiesConfig.getDocketwiseBaseUri();
			String docketwiseAccessToken = propertiesConfig.getDocketwiseAccessToken();
			String url = docketwiseBaseUri + "/contacts/" + contactId; // "https://app.docketwise.com/api/v1/contact";
			
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + docketwiseAccessToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
			HttpEntity<?> entity = new HttpEntity<>(updateContact, headers);
			
			log.info("contactId : " + contactId);
			log.info("modifiedContact : " + updateContact);
			
			ResponseEntity<Contact> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT, entity, Contact.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Matter[] getMatters () {
		try {
			String docketwiseBaseUri = propertiesConfig.getDocketwiseBaseUri();
			String docketwiseAccessToken = propertiesConfig.getDocketwiseAccessToken();
			String getMattersUrl = docketwiseBaseUri + "/matters"; // "https://app.docketwise.com/api/v1/matters";
			
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.add("Authorization", "Bearer " + docketwiseAccessToken);
			
			ResponseEntity<Matter[]> response = restTemplate.exchange(getMattersUrl, HttpMethod.GET, entity, Matter[].class);
			Matter[] matters = response.getBody();
			log.info("Matters Response ---------" + matters[0].getNumber());
			return matters;
		} catch (RestClientException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Matter getMatter (String matterId) {
		try {
			String docketwiseBaseUri = propertiesConfig.getDocketwiseBaseUri();
			String docketwiseAccessToken = propertiesConfig.getDocketwiseAccessToken();
			String getMattersUrl = docketwiseBaseUri + "/matters/" + matterId; // "https://app.docketwise.com/api/v1/matters/id";
			
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.add("Authorization", "Bearer " + docketwiseAccessToken);
			
			ResponseEntity<Matter> response = restTemplate.exchange(getMattersUrl, HttpMethod.GET, entity, Matter.class);
			Matter matter = response.getBody();
			log.info("Matters Response ---------" + matter);
			return matter;
		} catch (RestClientException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @param matter
	 * @return
	 */
	public Matter createMatter (@Valid Matter matter) {
		try {
			String docketwiseBaseUri = propertiesConfig.getDocketwiseBaseUri();
			String docketwiseAccessToken = propertiesConfig.getDocketwiseAccessToken();
			String createMattersUrl = docketwiseBaseUri + "/matters"; // "https://app.docketwise.com/api/v1/matter";
			
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + docketwiseAccessToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createMattersUrl);
			HttpEntity<?> entity = new HttpEntity<>(matter, headers);
			
			ResponseEntity<Matter> result = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, Matter.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (RestClientException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	/**
	 * updateMatter
	 * @param matterNumber
	 * @param modifiedMatter
	 * @return
	 */
	public Matter updateMatter (String matterId, Matter modifiedMatter) {
		try {
			String docketwiseBaseUri = propertiesConfig.getDocketwiseBaseUri();
			String docketwiseAccessToken = propertiesConfig.getDocketwiseAccessToken();
			String url = docketwiseBaseUri + "/matters/" + matterId; // "https://app.docketwise.com/api/v1/contact";
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + docketwiseAccessToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedMatter, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = new RestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
			ResponseEntity<Matter> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT, entity, Matter.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
