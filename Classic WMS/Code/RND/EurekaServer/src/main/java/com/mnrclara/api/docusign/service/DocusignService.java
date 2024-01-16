package com.mnrclara.api.docusign.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mnrclara.api.docusign.config.PropertiesConfig;
import com.mnrclara.api.docusign.model.Document;
import com.mnrclara.api.docusign.model.Docusign;
import com.mnrclara.api.docusign.model.Envelope;
import com.mnrclara.api.docusign.model.EnvelopeRequest;
import com.mnrclara.api.docusign.model.EnvelopeResponse;
import com.mnrclara.api.docusign.model.EnvelopeStatus;
import com.mnrclara.api.docusign.model.Recipients;
import com.mnrclara.api.docusign.model.Signer;
import com.mnrclara.api.docusign.model.UserInfo;
import com.mnrclara.api.docusign.model.oauth.AuthToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DocusignService {
	
	@Autowired
	PropertiesConfig propertiesConfig;
	
	@Autowired
	DropboxService dropboxService;
	
	/**
	 * 
	 * @param key
	 * @param file
	 * @param documentId
	 * @param docName
	 * @param signerName
	 * @param signerEmail
	 * @param filePath 
	 * @param signerEmail2 
	 * @return
	 * @throws IOException
//	 */
//	public EnvelopeResponse envelope (String key, String file, String documentId, String agreementCode, String docName, 
//			String signerName, String signerEmail, String filePath) throws IOException {
////		String filePath = propertiesConfig.getDocStorageBasePath();
//		
//		Envelope envelope = new Envelope();
//		Document document = new Document();
//		
//		log.info("agreementCode : " + agreementCode);
//		log.info("documentId : " + documentId);
//		log.info("docName : " + docName);
//		log.info("signerEmail : " + signerEmail);
//		log.info("signerName : " + signerName);
//		
////		String processedFilePath1 = processedFilePath + "/" + agreementCode + "_" + documentId;
////		filePath = filePath + propertiesConfig.getDocStorageAgreementPath() + "/" + documentId;
//		log.info("filePath : " + filePath);
//		String fileToBase64String = encodeFileToBase64Binary (filePath + "/" + file);
//		
//		// Logic for handling extract Client Id from documentId - logic for handling Document Template
//		if (documentId.indexOf("-") > 0) {
//			documentId = documentId.substring(0, documentId.indexOf("-"));
//		}
//		
//		document.setDocumentBase64(fileToBase64String);
//		document.setDocumentId(documentId);
//		document.setFileExtension("docx");
//		document.setName(docName);
//		List<Document> documents = new ArrayList<>();
//		documents.add(document);
//		envelope.setDocuments(documents); 						// documents
//		envelope.setEmailSubject("Please sign the document");	// emailSubject
//		
//		Recipients recipients = new Recipients();
//		List<Signer> signers = new ArrayList<>();
//		
//		// Signer2
//		Signer signer2 = new Signer();
//		signer2.setEmail(signerEmail);
//		signer2.setName(signerName);
//		signer2.setRecipientId(documentId);
//		signers.add(signer2);
//		
//		recipients.setSigners(signers);
//		envelope.setRecipients(recipients);						// recipients
//		envelope.setStatus("sent");								// status
//		
//		RestTemplate restTemplate2 = new RestTemplate();
//		HttpHeaders headers2 = new HttpHeaders();
//		HttpEntity<?> entity = new HttpEntity<>(envelope, headers2);
//		headers2.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//		headers2.add("Authorization", "Bearer " + key);
//		
//		String baseUrl = propertiesConfig.getDocusignBasepath();
//		String accountId = propertiesConfig.getDocusignAccountId();
////		String envUrl = "https://demo.docusign.net/restapi/v2.1/accounts/" + accountId + "/envelopes";
//		String envUrl = baseUrl + "/restapi/v2.1/accounts/" + accountId + "/envelopes";
//		log.info("-----------envUrl : " + envUrl);
//		
//		ResponseEntity<EnvelopeResponse> response2 = restTemplate2.exchange(envUrl, HttpMethod.POST, entity, EnvelopeResponse.class);
//		log.info("Access Token Response ---------" + response2.getBody());
//		return response2.getBody();
//	}
	
	/**
	 * envelope
	 * @param envelopeRequest
	 * @return
	 * @throws IOException
	 */
	public EnvelopeResponse envelope (EnvelopeRequest envelopeRequest) throws IOException {
		Envelope envelope = new Envelope();
		Document document = new Document();
		
		log.info("EnvelopeRequest : " + envelopeRequest);
		String fileToBase64String = encodeFileToBase64Binary (envelopeRequest.getFilePath() + "/" + envelopeRequest.getFile());
		
		// Logic for handling extract Client Id from documentId - logic for handling Document Template
		String documentId = envelopeRequest.getDocumentId();
		if (documentId.indexOf("-") > 0) {
			documentId = documentId.substring(0, documentId.indexOf("-"));
		}
		
		document.setDocumentBase64(fileToBase64String);
		document.setDocumentId(documentId);
		document.setFileExtension("docx");
		document.setName(envelopeRequest.getDocName());
		
		List<Document> documents = new ArrayList<>();
		documents.add(document);
		envelope.setDocuments(documents); 						// documents
		envelope.setEmailSubject("Please sign the document");	// emailSubject
		
		Recipients recipients = new Recipients();
		List<Signer> signers = new ArrayList<>();
		
		// Signer2
		Signer signer2 = new Signer();
		signer2.setEmail(envelopeRequest.getSignerEmail());
		signer2.setName(envelopeRequest.getSignerName());
		signer2.setRecipientId(documentId);
		signers.add(signer2);
		
		recipients.setSigners(signers);
		envelope.setRecipients(recipients);						// recipients
		envelope.setStatus("sent");								// status
		
		RestTemplate restTemplate2 = new RestTemplate();
		HttpHeaders headers2 = new HttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(envelope, headers2);
		headers2.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers2.add("Authorization", "Bearer " + envelopeRequest.getKey());
		
		String baseUrl = propertiesConfig.getDocusignBasepath();
		String accountId = propertiesConfig.getDocusignAccountId();
		String envUrl = baseUrl + "/restapi/v2.1/accounts/" + accountId + "/envelopes";
		log.info("-----------envUrl : " + envUrl);
		
		ResponseEntity<EnvelopeResponse> response2 = restTemplate2.exchange(envUrl, HttpMethod.POST, entity, EnvelopeResponse.class);
		log.info("Access Token Response ---------" + response2.getBody());
		return response2.getBody();
	}
	
	/**
	 * 
	 * @param envelopeId
	 * @param key
	 * @return
	 */
	public EnvelopeStatus envelopStatus (String envelopeId, String key) {
		// API - {{baseUrl}}/{{apiVersion}}/accounts/{{accountId}}/envelopes/{{envelopeId}}
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Bearer " + key);
		
		// API - {{baseUrl}}/{{apiVersion}}/accounts/{{accountId}}/envelopes/{{envelopeId}}
		String baseUrl = propertiesConfig.getDocusignBasepath();
		String apiVersion = propertiesConfig.getDocusignApiVersion();
		String accountId = propertiesConfig.getDocusignAccountId();
		String envUrl = baseUrl + "/restapi/" + apiVersion + "/accounts/" + accountId + "/envelopes/" + envelopeId;
		log.info("EnvelopeStatus URL : " + envUrl);
		
		ResponseEntity<EnvelopeStatus> response = restTemplate.exchange(envUrl, HttpMethod.GET, entity, EnvelopeStatus.class);
		log.info("Access Token Response ---------" + response.getBody());
		return response.getBody();
	}
	
	/**
	 * documentEnvelopStatus
	 * @param envelopeId
	 * @param key
	 * @return
	 */
	public EnvelopeStatus documentEnvelopStatus (String envelopeId, String key) {
		// API - {{baseUrl}}/{{apiVersion}}/accounts/{{accountId}}/envelopes/{{envelopeId}}
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Bearer " + key);
		
		// API - {{baseUrl}}/{{apiVersion}}/accounts/{{accountId}}/envelopes/{{envelopeId}}
		String baseUrl = propertiesConfig.getDocusignBasepath();
		String apiVersion = propertiesConfig.getDocusignApiVersion();
		String accountId = propertiesConfig.getDocusignAccountId();
		String envUrl = baseUrl + "/restapi/" + apiVersion + "/accounts/" + accountId + "/envelopes/" + envelopeId;
		
		ResponseEntity<EnvelopeStatus> response = restTemplate.exchange(envUrl, HttpMethod.GET, entity, EnvelopeStatus.class);
		log.info("documentEnvelopStatus Response ---------" + response.getBody());
		return response.getBody();
	}
	
	/**
	 * 
	 * @param envelopeId
	 * @param documentId
	 * @param potentialClientId
	 * @param key
	 * @param filePath 
	 * @return
	 * @throws Exception
	 */
	public String downloadEnvelop (String envelopeId, String documentId, String potentialClientId, String key, String filePath) 
			throws Exception {
//		String downloadFilePath = propertiesConfig.getDownloadFilePath();
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + key);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
	    HttpEntity<String> entity = new HttpEntity<>(headers);
	    
		String baseUrl = propertiesConfig.getDocusignBasepath();
		String apiVersion = propertiesConfig.getDocusignApiVersion();
		String accountId = propertiesConfig.getDocusignAccountId();
		
		// API - {{baseUrl}}/{{apiVersion}}/accounts/{{accountId}}/envelopes/{{envelopeId}}/documents/{{documentId}}
		String envUrl = baseUrl + "/restapi/" + apiVersion + "/accounts/" + accountId + "/envelopes/" + envelopeId + "/documents/" + documentId;
		
		ResponseEntity<byte[]> response = restTemplate.exchange(envUrl, HttpMethod.GET, entity, byte[].class, "1");
		String downloadFileName = response.getHeaders().getContentDisposition().getFilename();
		log.info("downloadFileName : " + downloadFileName);
		
		// Save the document
		String fileExtn = downloadFileName.substring(downloadFileName.lastIndexOf('.'));
		downloadFileName = downloadFileName.substring(0, downloadFileName.lastIndexOf('.'));		// Removing extension of file
		downloadFileName = downloadFileName + "_signed" + fileExtn;	// Creating new filename
				
		// Writing into local drive
		File file = new File(downloadFileName);
		OutputStream os = new FileOutputStream(filePath + "/" + file);
	    os.write(response.getBody());
	    os.close();
	    
	    return file.getName(); //mapResponse.get("pathLower");
	}
	
	/**
	 * downloadDocumentEnvelope
	 * @param envelopeId
	 * @param documentId
	 * @param clientOrMatterId
	 * @param key
	 * @param filePath 
	 * @return
	 * @throws Exception
	 */
	public String downloadDocumentEnvelope (String envelopeId, String documentId, String clientOrMatterId, 
			String key, String filePath) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + key);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
	    HttpEntity<String> entity = new HttpEntity<>(headers);
	    
		String baseUrl = propertiesConfig.getDocusignBasepath();
		String apiVersion = propertiesConfig.getDocusignApiVersion();
		String accountId = propertiesConfig.getDocusignAccountId();
		
		// Logic for handling extract MatterNumber because documentId cannot have hyphen('-')
		if (documentId.indexOf("-") > 0) {
			documentId = documentId.substring(0, documentId.indexOf("-"));
		}
		
		// API - {{baseUrl}}/{{apiVersion}}/accounts/{{accountId}}/envelopes/{{envelopeId}}/documents/{{documentId}}
		String envUrl = baseUrl + "/restapi/" + apiVersion + "/accounts/" + accountId + "/envelopes/" + envelopeId + "/documents/" + documentId;
		
		ResponseEntity<byte[]> response = restTemplate.exchange(envUrl, HttpMethod.GET, entity, byte[].class, "1");
		String downloadFileName = response.getHeaders().getContentDisposition().getFilename();
		log.info("downloadFileName : " + downloadFileName);
		
		// Save the document
		String fileExtn = downloadFileName.substring(downloadFileName.lastIndexOf('.'));
		downloadFileName = downloadFileName.substring(0, downloadFileName.lastIndexOf('.'));	// Removing extension of file
		downloadFileName = downloadFileName + "_signed" + fileExtn;								// Creating new filename
				
		// Writing into local drive
		File file = new File(downloadFileName);
		OutputStream os = new FileOutputStream(filePath + "/" + file);
	    os.write(response.getBody());
	    os.close();
	    
	    return file.getName(); 
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private static String encodeFileToBase64Binary(String fileName) throws IOException {
	    File file = new File(fileName);
	    log.info("encodeFileToBase64Binary : " + file.getAbsolutePath());
	    byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
	    return new String(encoded, StandardCharsets.US_ASCII);
	}
}
