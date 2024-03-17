package com.mnrclara.api.common.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.mnrclara.api.common.controller.exception.BadRequestException;
import com.mnrclara.api.common.model.docusign.DocusignEnvelope;
import com.mnrclara.api.common.model.docusign.DocusignToken;
import com.mnrclara.api.common.model.docusign.DocusignTransaction;
import com.mnrclara.api.common.model.docusign.envelope.EnvelopeRequest;
import com.mnrclara.api.common.model.docusign.envelope.EnvelopeResponse;
import com.mnrclara.api.common.model.docusign.listDocs.ListDocument;
import com.mnrclara.api.common.model.docusign.status.EnvelopeStatus;
import com.mnrclara.api.common.model.docusign.userinfo.UserInfo;
import com.mnrclara.api.common.repository.DocusignTokenRepository;
import com.mnrclara.api.common.repository.DocusignTransactionRepository;
import com.mnrclara.api.common.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DocusignService extends DocusignAuthService {

	@Value("${dropbox.access.token}")
	private String ACCESS_TOKEN;
	
	@Autowired
	DocusignTokenRepository docusignTokenRepository;
	
	@Autowired
	DocusignTransactionRepository docusignTransactionRepository;
	
	@Autowired
	DropboxService dropboxService;
	
	/**
	 * getDocusign
	 * @param clientId
	 * @return
	 */
	public DocusignToken getDocusignByClientId (String clientId) {
		Optional<DocusignToken> optDocusign = docusignTokenRepository.findByClientId(clientId);
		if (optDocusign.isEmpty()) {
			throw new BadRequestException("Client Id doesn't exist.");
		}
		
		DocusignToken docusignToken = optDocusign.get();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		// Stored Date Time
		String strTokenGeneratedOn = dateFormat.format(docusignToken.getTokenGeneratedOn());  
		LocalDateTime tokenGeneratedOn = LocalDateTime.parse(strTokenGeneratedOn, formatter); 
		
		// Current Date Time
		LocalDateTime now = LocalDateTime.now();
		String strCurrentDatetime = now.format(formatter);
		LocalDateTime currentDatetime = LocalDateTime.parse(strCurrentDatetime, formatter);

		long toHours = java.time.Duration.between(tokenGeneratedOn, currentDatetime).toHours();
		log.info("toHours :" + toHours);
		
		long toMinutes = java.time.Duration.between(tokenGeneratedOn, currentDatetime).toMinutes();
		log.info("toMinutes :" + toMinutes);
		
		long toSeconds = java.time.Duration.between(tokenGeneratedOn, currentDatetime).toSeconds();
		log.info("toSeconds :" + toSeconds);
		
		if (toHours > 7) { // Max hours is 8
			log.info("Calling generateRefreshToken() since the time expires...");
			generateRefreshToken();
		}
		
		return optDocusign.get();
	}
	
	/**
	 * 
	 * @param clientId
	 * @return
	 */
	public DocusignTransaction getDocusignTransactionByClientId (String clientId) {
		Optional<DocusignTransaction> optDocusignTrans = docusignTransactionRepository.findByClientId(clientId);
		if (optDocusignTrans.isEmpty()) {
			throw new BadRequestException("Client Id doesn't exist.");
		}
		return optDocusignTrans.get();
	}
	
	/**
	 * https://{{hostenv}}/oauth/userinfo
	 * @return 
	 */
	public UserInfo getUserInfo () {
		try {
			String hostenv = propertiesConfig.getDocusignHostenv();
			String clientId = propertiesConfig.getDocusignClientIid();
			DocusignToken docusignToken = getDocusignByClientId (clientId);
			
			String userInfoUrl = "https://" + hostenv + "/oauth/userinfo";
			
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.add("Authorization", "Bearer " + docusignToken.getAuthToken());
			
			ResponseEntity<UserInfo> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, UserInfo.class);
			log.info("Access Token Response ---------" + response.getBody());
			
			UserInfo userInfo = response.getBody();
			String accountId = userInfo.getAccounts().stream().findFirst().get().getAccount_id();
			 
			// Update Table with AccountId
			DocusignTransaction docusignTransaction = new DocusignTransaction();
			docusignTransaction.setClientId(clientId);
			docusignTransaction.setAccountId(accountId);			
			docusignTransactionRepository.save(docusignTransaction);
			return response.getBody();
		} catch (RestClientException e) {
			e.printStackTrace();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param file
	 * @param documentId
	 * @param agreementCode
	 * @param docName
	 * @param signerName
	 * @param signerEmail
	 * @param filePath 
	 * @return
	 */
//	public EnvelopeResponse callDocusignEnvelope(String file, String documentId, String agreementCode, String docName, 
//			String signerName, String signerEmail, String filePath) {
	public EnvelopeResponse callDocusignEnvelope(EnvelopeRequest envelopeRequest) {
		try {
			String clientId = propertiesConfig.getDocusignClientIid();
			String docusignServiceUrl = propertiesConfig.getDocusignServiceUrl();
			
			DocusignToken docusignToken = getDocusignByClientId (clientId);
			envelopeRequest.setKey(docusignToken.getAuthToken());
			
			RestTemplate restTemplate2 = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8083/mnrclara/docuservice/docuSign")
					UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(docusignServiceUrl);
//					.queryParam("file", file)
//					.queryParam("key", docusignToken.getAuthToken())
//					.queryParam("documentId", documentId)
//					.queryParam("docName", docName)
//					.queryParam("signerName", signerName)
//					.queryParam("signerEmail", signerEmail)
//					.queryParam("agreementCode", agreementCode)
//					.queryParam("filePath", filePath);
					
			HttpEntity<?> entity = new HttpEntity<>(envelopeRequest, headers);
			ResponseEntity<EnvelopeResponse> result = 
					restTemplate2.exchange(builder.toUriString(), HttpMethod.POST, entity, EnvelopeResponse.class);
			log.info("result : " + result.getBody());
			
			if (result.getBody() != null) {
				// Store the EnvelopeID in DB
				DocusignTransaction docusignTransaction = new DocusignTransaction();
				docusignTransaction.setClientId(clientId);
				docusignTransaction.setDocumentId(envelopeRequest.getDocumentId());
				docusignTransaction.setSignerName(envelopeRequest.getSignerName());
				docusignTransaction.setSignerEmail(envelopeRequest.getSignerEmail());
				docusignTransaction.setSentOn(DateUtils.getCurrentDateTime());
				docusignTransaction.setEnvelopeId((result.getBody().getEnvelopeId()));
				docusignTransactionRepository.save(docusignTransaction);
			}
			return result.getBody();
		} catch (HttpClientErrorException e) {
			log.info("HttpClientErrorException ##### : " + e.getStatusCode());
			if (e.getStatusCode() == HttpStatus.UNAUTHORIZED || e.getStatusCode() == HttpStatus.BAD_REQUEST) {
				generateRefreshToken ();
			}
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return null;
	}
	
	/**
	 * Envelope Status
	 * -------------------
	 * @param potentialClientId
	 * @return
	 */
	public EnvelopeStatus getDocusignEnvelopeStatus(String potentialClientId) {
		try {
			String clientId = propertiesConfig.getDocusignClientIid();
			DocusignToken docusignToken = getDocusignByClientId (clientId);
			log.info("--------clientId----------> : " + clientId);
			log.info("--------potentialClientId----------> : " + potentialClientId);
			
			DocusignEnvelope docusignEnvelope = 
					docusignTransactionRepository.findLatestEnvId(clientId, potentialClientId);
			if (docusignEnvelope == null) {
				throw new BadRequestException("DocusignEnvelope could not find for the given PotentialClient ID : " + potentialClientId);
			}
			
			log.info("getEnvelopeId : " + docusignEnvelope.getEnvelopeId());
			log.info("Id : " + docusignEnvelope.getId());	
			RestTemplate restTemplate2 = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			
			String docusignServiceUrl = propertiesConfig.getDocusignServiceUrl();
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(docusignServiceUrl + "/envelop/status")
					.queryParam("envelopeId", docusignEnvelope.getEnvelopeId())
					.queryParam("key", docusignToken.getAuthToken());
			HttpEntity<?> entity = new HttpEntity<>(headers);
			
			log.info("docusignServiceUrl----> : " + builder.toUriString());
			ResponseEntity<EnvelopeStatus> result = 
					restTemplate2.exchange(builder.toUriString(), HttpMethod.GET, entity, EnvelopeStatus.class);
			log.info("result : " + result.getBody());
			if (result.getBody() != null) {
				// Store the EnvelopeID in DB
				Optional<DocusignTransaction> docusignTransactionOpt = docusignTransactionRepository.findById(docusignEnvelope.getId());
				DocusignTransaction docusignTransaction = docusignTransactionOpt.get();
				docusignTransaction.setStatus(result.getBody().getStatus());
				docusignTransactionRepository.save(docusignTransaction);
			}
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * DocumentEnvelopeStatus
	 * ------------------------
	 * @param clientMatterId
	 * @return
	 */
	public EnvelopeStatus getDocusignDocumentEnvelopeStatus(String clientMatterId) {
		try {
			String clientId = propertiesConfig.getDocusignClientIid();
			DocusignToken docusignToken = getDocusignByClientId (clientId);
			
			DocusignEnvelope docusignEnvelope = docusignTransactionRepository.findLatestEnvId(clientId, clientMatterId);
			log.info("getEnvelopeId : " + docusignEnvelope.getEnvelopeId());	
			log.info("Id : " + docusignEnvelope.getId());
			
			RestTemplate restTemplate2 = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			
			String docusignServiceUrl = propertiesConfig.getDocusignServiceUrl();
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(docusignServiceUrl + "/envelop/clientmatter/status")
					.queryParam("envelopeId", docusignEnvelope.getEnvelopeId())
					.queryParam("key", docusignToken.getAuthToken());
			
//			UriComponentsBuilder builder = 
//					UriComponentsBuilder.fromHttpUrl("http://localhost:8083/mnrclara/docuservice/docuSign/envelop/clientmatter/status")
//					.queryParam("envelopeId", docusignEnvelope.getEnvelopeId())
//					.queryParam("key", docusignToken.getAuthToken());
					
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<EnvelopeStatus> result = restTemplate2.exchange(builder.toUriString(), HttpMethod.GET, entity, EnvelopeStatus.class);
			log.info("result : " + result.getBody());
			if (result.getBody() != null) {
				// Store the EnvelopeID in DB
				Optional<DocusignTransaction> docusignTransactionOpt = docusignTransactionRepository.findById(docusignEnvelope.getId());
				DocusignTransaction docusignTransaction = docusignTransactionOpt.get();
				docusignTransaction.setStatus(result.getBody().getStatus());
				docusignTransactionRepository.save(docusignTransaction);
			}
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
		
	/**
	 * downloadEnvelope
	 * @param potentialClientId
	 * @param filePath 
	 * @return
	 */
	public String downloadEnvelope (String potentialClientId, String filePath) {
		try {
			String clientId = propertiesConfig.getDocusignClientIid();
			DocusignToken docusignToken = getDocusignByClientId (clientId);
			DocusignEnvelope docusignEnvelope = 
					docusignTransactionRepository.findLatestEnvId(clientId, potentialClientId);
			
			RestTemplate restTemplate2 = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			
			String docusignServiceUrl = propertiesConfig.getDocusignServiceUrl();
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(docusignServiceUrl + "/envelop/download")
					.queryParam("envelopeId", docusignEnvelope.getEnvelopeId())
					.queryParam("documentId", potentialClientId)
					.queryParam("potentialClientId", potentialClientId)
					.queryParam("key", docusignToken.getAuthToken())
					.queryParam("filePath", filePath);
					
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<String> result = restTemplate2.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
			
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * downloadClientMatterDocumentEnvelope
	 * @param clientOrMatterId
	 * @param filePath 
	 * @return
	 */
	public String downloadClientMatterDocumentEnvelope (String clientOrMatterId, String filePath) {
		try {
			String clientId = propertiesConfig.getDocusignClientIid();
			DocusignToken docusignToken = getDocusignByClientId (clientId);
			DocusignEnvelope docusignEnvelope = 
					docusignTransactionRepository.findLatestEnvId(clientId, clientOrMatterId);
			
			RestTemplate restTemplate2 = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			
			String docusignServiceUrl = propertiesConfig.getDocusignServiceUrl();
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(docusignServiceUrl + "/envelop/clientmatter/download")
					.queryParam("envelopeId", docusignEnvelope.getEnvelopeId())
					.queryParam("documentId", clientOrMatterId)
					.queryParam("clientMatterId", clientOrMatterId)
					.queryParam("key", docusignToken.getAuthToken())
					.queryParam("filePath", filePath);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<String> result = restTemplate2.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * listDocuments
	 * @param envelopeId
	 * @param accessToken
	 * @return
	 */
	public ListDocument listDocuments (String envelopeId, String accessToken) {
		// API - {{baseUrl}}/{{apiVersion}}/accounts/{{accountId}}/envelopes/{{envelopeId}}/documents
		
		String clientId = propertiesConfig.getDocusignClientIid();
		DocusignToken docusignToken = getDocusignByClientId (clientId);
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Bearer " + docusignToken.getAuthToken());
		
		// API - {{baseUrl}}/{{apiVersion}}/accounts/{{accountId}}/envelopes/{{envelopeId}}/documents
		String baseUrl = propertiesConfig.getDocusignBasepath();
		String apiVersion = propertiesConfig.getDocusignApiVersion();
		String accountId = propertiesConfig.getDocusignAccountId();
		String envUrl = baseUrl + "/" + apiVersion + "/accounts/" + accountId + "/envelopes/" + envelopeId + "/documents";
		
		ResponseEntity<ListDocument> response = restTemplate.exchange(envUrl, HttpMethod.POST, entity, ListDocument.class);
		log.info("Access Token Response ---------" + response.getBody());
		return response.getBody();
	}
}
