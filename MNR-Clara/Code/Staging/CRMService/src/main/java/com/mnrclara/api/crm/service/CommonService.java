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
import com.mnrclara.api.crm.model.auth.AuthToken;
import com.mnrclara.api.crm.model.communication.SMS;
import com.mnrclara.api.crm.model.dto.EMail;
import com.mnrclara.api.crm.model.dto.EnvelopeRequest;
import com.mnrclara.api.crm.model.dto.EnvelopeResponse;
import com.mnrclara.api.crm.model.dto.EnvelopeStatus;
import com.mnrclara.api.crm.model.dto.MailMerge;
import com.mnrclara.api.crm.model.potentialclient.DownloadFileResponse;

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
	
	private String getCommonServiceApiUrl () {
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
	 * @param toNumber
	 * @param textMessage
	 * @param authToken
	 * @return
	 */
	public Boolean sendSMSForFeedback (Long toNumber, String textMessage, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			SMS sms = new SMS();
			sms.setToNumber(toNumber);
			sms.setTextMessage(textMessage);
			
			HttpEntity<?> entity = new HttpEntity<>(sms, headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCommonServiceApiUrl() + "communication/sendSMS/feedback");
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
	public String sendEmail(EMail email, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(email, headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCommonServiceApiUrl() + "communication/sendEmail");
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
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
	public MailMerge mailMerge (MailMerge mailMerge, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(mailMerge, headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCommonServiceApiUrl() + "communication/mailMerge");
			ResponseEntity<MailMerge> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, MailMerge.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET - "/dropbox/download"
	public DownloadFileResponse downloadFile (String fileUrl, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCommonServiceApiUrl() + "communication/dropbox/download")
					.queryParam("fileUrl", fileUrl);
			ResponseEntity<DownloadFileResponse> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, DownloadFileResponse.class);
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
			EnvelopeRequest envelopeRequest = new EnvelopeRequest();
			envelopeRequest.setFile(file);
			envelopeRequest.setDocumentId(documentId);
			envelopeRequest.setDocName(docName);
			envelopeRequest.setSignerName(signerName);
			envelopeRequest.setSignerEmail(signerEmail);
			envelopeRequest.setAgreementCode(agreementCode);
			envelopeRequest.setFilePath(filePath);
			
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getCommonServiceApiUrl() + "/communication/docusign/envelope");
//					.queryParam("file", file)
//					.queryParam("documentId", documentId)
//					.queryParam("docName", docName)
//					.queryParam("signerName", signerName)
//					.queryParam("signerEmail", signerEmail)
//					.queryParam("agreementCode", agreementCode)
//					.queryParam("filePath", filePath);
					
			HttpEntity<?> entity = new HttpEntity<>(envelopeRequest, headers);
			ResponseEntity<EnvelopeResponse> result = 
					restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, EnvelopeResponse.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET - "/communication/docusign/envelope/status"
	public EnvelopeStatus getDocusignEnvelopeStatus(String authToken, String potentialClientId) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getCommonServiceApiUrl() + "/communication/docusign/envelope/status")
					.queryParam("potentialClientId", potentialClientId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<EnvelopeStatus> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, EnvelopeStatus.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET - "/communication/docusign/envelope/download"
	public String downloadEnvelopeFromDocusign (String authToken, String potentialClientId, String filePath) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getCommonServiceApiUrl() + "/communication/docusign/envelope/download")
					.queryParam("potentialClientId", potentialClientId)
					.queryParam("filePath", filePath);
					
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<String> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * genToken
	 * @param code
	 * @param authToken
	 * @return
	 */
	public AuthToken genToken (String code, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCommonServiceApiUrl() + "communication/docusign/token")
					.queryParam("code", code);
			ResponseEntity<AuthToken> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, AuthToken.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
