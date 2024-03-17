package com.mnrclara.api.management.service;

import java.util.Collections;

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

import com.mnrclara.api.management.config.PropertiesConfig;
import com.mnrclara.api.management.model.auth.AuthToken;
import com.mnrclara.api.management.model.communication.SMS;
import com.mnrclara.api.management.model.dto.CalendarEvent;
import com.mnrclara.api.management.model.dto.DownloadFileResponse;
import com.mnrclara.api.management.model.dto.EMail;
import com.mnrclara.api.management.model.dto.EnvelopeResponse;
import com.mnrclara.api.management.model.dto.EnvelopeStatus;
import com.mnrclara.api.management.model.dto.MailMerge;
import com.mnrclara.api.management.model.dto.docketwise.Contact;
import com.mnrclara.api.management.model.dto.docketwise.CreateContact;
import com.mnrclara.api.management.model.dto.docketwise.Matter;
import com.mnrclara.api.management.model.dto.docketwise.UpdateContact;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommonService {

	@Autowired
	PropertiesConfig propertiesConfig;

	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	private String getCommonServiceApiUrl() {
		return propertiesConfig.getCommonServiceUrl();
	}
	
	/**
	 * 
	 * @param toNumber
	 * @param textMessage
	 * @param authToken
	 * @return
	 */
	public Boolean sendSMS(Long toNumber, String textMessage, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			SMS sms = new SMS();
			sms.setToNumber(toNumber);
			sms.setTextMessage(textMessage);
			
			HttpEntity<?> entity = new HttpEntity<>(sms, headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCommonServiceApiUrl() + "communication/sendSMS");
			ResponseEntity<Boolean> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Boolean.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 
	 * @param email
	 * @param authToken
	 * @return
	 */
	public Boolean sendEmail(EMail email, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(email, headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCommonServiceApiUrl() + "communication/sendEmail");
			ResponseEntity<Boolean> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity,
					Boolean.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 
	 * @param mailMerge
	 * @param authToken
	 * @return
	 */
	public MailMerge mailMerge(MailMerge mailMerge, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(mailMerge, headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCommonServiceApiUrl() + "communication/mailMerge");
			ResponseEntity<MailMerge> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MailMerge.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 
	 * @param calendarEvent
	 * @param authToken
	 * @return
	 */
	public boolean sendCalendar(CalendarEvent calendarEvent, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(calendarEvent, headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCommonServiceApiUrl() + "communication/calendar");
			ResponseEntity<Boolean> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity,
					Boolean.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - "/dropbox/download"
	public DownloadFileResponse downloadFile(String fileUrl, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCommonServiceApiUrl() + "communication/dropbox/download")
					.queryParam("fileUrl", fileUrl);
			ResponseEntity<DownloadFileResponse> result = getRestTemplate().exchange(builder.toUriString(),
					HttpMethod.GET, entity, DownloadFileResponse.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - "/dropbox/download"
	public DownloadFileResponse downloadFileWithPath(String filePath, String fileUrl, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCommonServiceApiUrl() + "communication/dropbox/" + filePath + "/download")
					.queryParam("fileUrl", fileUrl);
			ResponseEntity<DownloadFileResponse> result = getRestTemplate().exchange(builder.toUriString(),
					HttpMethod.GET, entity, DownloadFileResponse.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST - "/communication/docusign/envelope"
	public EnvelopeResponse callDocusignEnvelope(String authToken, String agreementCode, String documentId, String file, 
			String docName, String signerName, String signerEmail, String filePath) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCommonServiceApiUrl() + "/communication/docusign/envelope")
					.queryParam("file", file)
					.queryParam("documentId", documentId)
					.queryParam("docName", docName)
					.queryParam("signerName", signerName)
					.queryParam("signerEmail", signerEmail)
					.queryParam("agreementCode", agreementCode)
					.queryParam("filePath", filePath);
					
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<EnvelopeResponse> result = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, EnvelopeResponse.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - "/communication/docusign/envelope/status"
	public EnvelopeStatus getDocusignEnvelopeStatus(String authToken, String clientMatterId) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCommonServiceApiUrl() + "/communication/docusign/envelope/clientmatter/status")
					.queryParam("clientMatterId", clientMatterId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<EnvelopeStatus> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
					EnvelopeStatus.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - "/communication/docusign/envelope/download"
	public String downloadEnvelopeFromDocusign(String authToken, String potentialClientId) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCommonServiceApiUrl() + "/communication/docusign/envelope/download")
					.queryParam("potentialClientId", potentialClientId);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<String> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
					String.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET - "/communication/docusign/envelope/download"
	public String downloadClientMatterDocumentEnvelopeFromDocusign(String clientMatterId, String authToken, String filePath) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCommonServiceApiUrl() + "/communication/docusign/envelope/clientmatter/download")
					.queryParam("clientMatterId", clientMatterId)
					.queryParam("filePath", filePath);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<String> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
					String.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * genToken
	 * 
	 * @param code
	 * @param authToken
	 * @return
	 */
	public AuthToken genToken(String code, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCommonServiceApiUrl() + "communication/docusign/token").queryParam("code", code);
			ResponseEntity<AuthToken> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					AuthToken.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-----------------------Docketwise------------------------------------------------
	
	// GET /conatcts
	public Contact[] getDocketwiseContacts(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCommonServiceApiUrl() + "communication/docketwise/contacts");
			ResponseEntity<Contact[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					Contact[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST - "/communication/docketwise/contacts"
	public Contact createDocketwiseContact(CreateContact createContact, String authToken) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCommonServiceApiUrl() + "/communication/docketwise/contacts");
					
			HttpEntity<?> entity = new HttpEntity<>(createContact, headers);
			ResponseEntity<Contact> result = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, Contact.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public Contact updateContact (String contactId, UpdateContact updateContact, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(updateContact, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = new RestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCommonServiceApiUrl() + "/communication/docketwise/contacts/" + contactId);
			ResponseEntity<Contact> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT, entity, Contact.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
//			e.printStackTrace();
			throw e;
		}
	}
	
	// GET /matters
	public Matter[] getDocketwiseMatters(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCommonServiceApiUrl() + "communication/docketwise/matters");
			ResponseEntity<Matter[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					Matter[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET /matters
	public Matter getDocketwiseMatter(String matterId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCommonServiceApiUrl() + "communication/docketwise/matters/" + matterId);
			ResponseEntity<Matter> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					Matter.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST - "/communication/docketwise/matters"
	public Matter createDocketwiseMatter(Matter matter, String authToken) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCommonServiceApiUrl() + "/communication/docketwise/matters");
					
			HttpEntity<?> entity = new HttpEntity<>(matter, headers);
			ResponseEntity<Matter> result = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, Matter.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// PUT - "/communication/docketwise/matters"
	public Matter updateMatter (String matterNumber, Matter modifiedMatter, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedMatter, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = new RestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCommonServiceApiUrl() + "/communication/docketwise/matters/" + matterNumber);
			ResponseEntity<Matter> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT, entity, Matter.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
//			e.printStackTrace();
			throw e;
		}
	}
}
