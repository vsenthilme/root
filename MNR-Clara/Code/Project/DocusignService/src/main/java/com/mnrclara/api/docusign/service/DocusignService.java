package com.mnrclara.api.docusign.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mnrclara.api.docusign.config.PropertiesConfig;
import com.mnrclara.api.docusign.model.Document;
import com.mnrclara.api.docusign.model.Envelope;
import com.mnrclara.api.docusign.model.EnvelopeRequest;
import com.mnrclara.api.docusign.model.EnvelopeResponse;
import com.mnrclara.api.docusign.model.EnvelopeStatus;
import com.mnrclara.api.docusign.model.EnvelopeTemplates;
import com.mnrclara.api.docusign.model.Recipients;
import com.mnrclara.api.docusign.model.SignHereTabs;
import com.mnrclara.api.docusign.model.Signer;
import com.mnrclara.api.docusign.model.Tabs;
import com.mnrclara.api.docusign.model.TextCustomFields;
import com.mnrclara.api.docusign.model.TextTabs;

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
		
		/*-----------------------TABS---------------------------------------------*/
		
//		SignHereTabs signHereTabs = new SignHereTabs();
//		signHereTabs.setDocumentId(documentId);
//		signHereTabs.setPageNumber("1");
//		signHereTabs.setXPosition("191");
//		signHereTabs.setYPosition("148");
		SignHereTabs signHereTabsPG1 = getSignHereTabs (documentId, "1", "400", "700");
		SignHereTabs signHereTabsPG2 = getSignHereTabs (documentId, "2", "400", "700");
		SignHereTabs signHereTabsPG3 = getSignHereTabs (documentId, "3", "400", "700");
		SignHereTabs signHereTabsPG4 = getSignHereTabs (documentId, "4", "400", "700");
		SignHereTabs signHereTabsPG5 = getSignHereTabs (documentId, "5", "400", "700");
		SignHereTabs signHereTabsPG6 = getSignHereTabs (documentId, "6", "200", "700");
		
//		// TextTabs
//		// documentId, pageNumber, xPosition, yPosition, tabLabel, value
//		// Date1
		TextTabs date1 = getTextTabs(documentId, "1", "10", "20", "Date1", "31-Aug-2023");
//		
//		// First_Name_1
//		TextTabs firstName1 = getTextTabs(documentId, "1", "10", "20", "First_Name1", "Fazil");
//		
//		// First_Name_2
//		TextTabs firstName2 = getTextTabs(documentId, "6", "10", "20", "First_Name2", "Arun");
//		
//		// Address_Line_1
//		TextTabs addressLine1 = getTextTabs(documentId, "6", "20", "30", "Address_Line_1", "2/630, Madipakkam.");
//		
//		// Home_Phone
//		TextTabs homePhone = getTextTabs(documentId, "6", "30", "40", "Home_Phone", "9840407848");
//		
//		// Work_Home
//		TextTabs workHome = getTextTabs(documentId, "6", "40", "50", "Work_Home", "8057486541");
//		
//		// Email_Address
//		TextTabs emailAddress = getTextTabs(documentId, "6", "50", "60", "Email_Address", "murugavel@tekclover.com");
		
		Tabs tabs = new Tabs();
		List<SignHereTabs> listSignHereTabs = new ArrayList<>();
		List<TextTabs> listTextTabs = new ArrayList<>();
		List<TextCustomFields> listTextCustomFields = new ArrayList<>();
		
//		TextCustomFields date1 = new TextCustomFields();
//		date1.setName("date1");
//		date1.setRequired("false");
//		date1.setShow("true");
//		date1.setValue("DOCUMENTID");
//		date1.setXPosition("70");
//		date1.setYPosition("120");
//		listTextCustomFields.add(date1);
		
		listSignHereTabs.add(signHereTabsPG1);
		listSignHereTabs.add(signHereTabsPG2);
		listSignHereTabs.add(signHereTabsPG3);
		listSignHereTabs.add(signHereTabsPG4);
//		listSignHereTabs.add(signHereTabsPG5);
//		listSignHereTabs.add(signHereTabsPG6);
		
//		listTextTabs.add(date1);
//		listTextTabs.add(firstName1);
//		listTextTabs.add(firstName2);
//		listTextTabs.add(addressLine1);
//		listTextTabs.add(homePhone);
//		listTextTabs.add(workHome);
//		listTextTabs.add(emailAddress);
		
		tabs.setSignHereTabs(listSignHereTabs);
//		tabs.setTextCustomFields(listTextCustomFields);
//		tabs.setTextTabs(listTextTabs);
		
		signer2.setTabs(tabs);
		
		/*-----------------------TABS---------------------------------------------*/

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
		
//		listTemplates (envelopeRequest.getKey());
		
		//060f6053-13c1-4870-9d89-f2f7ba1af1a0
		String envelopeId = "060f6053-13c1-4870-9d89-f2f7ba1af1a0";
		applyTemplate (envelopeId, envelopeRequest.getKey());
		return response2.getBody();
	}
	
	/**
	 * 
	 * @param documentId
	 * @param pageNumber
	 * @param xPos
	 * @param yPos
	 * @return
	 */
	private SignHereTabs getSignHereTabs (String documentId, String pageNumber, String xPos, String yPos) {
		SignHereTabs signHereTabs = new SignHereTabs();
		signHereTabs.setDocumentId(documentId);
		signHereTabs.setPageNumber(pageNumber);
		signHereTabs.setXPosition(xPos);
		signHereTabs.setYPosition(yPos);
		return signHereTabs;
	}
	
	/**
	 * 
	 * "anchorString": "/legal/",
	"anchorUnits": "pixels",
	"anchorXOffset": "5",
	"anchorYOffset": "-9",
	"bold": "true",
	"font": "helvetica",
	"fontSize": "size11",
	"locked": "false",
	"tabId": "legal_name",
	"tabLabel": "Legal name",
	"value": "'"${SIGNER_NAME}"'"
	 * @return
	 */
	private TextTabs getTextTabs(String documentId, String pageNumber, String xPosition, String yPosition, String tabLabel, String value) {
		TextTabs textTabs = new TextTabs();
		textTabs.setAnchorString("/familiar/");
		textTabs.setAnchorUnits("pixels");
		textTabs.setAnchorXOffset("5");
		textTabs.setAnchorYOffset("10");
		textTabs.setBold("true");
		textTabs.setLocked("false");
		textTabs.setTabId("Date1");
		textTabs.setTabLabel("PIXels123");
		textTabs.setValue("PPPPPIXels123");
		
//		textTabs.setDocumentId(documentId);
//		textTabs.setPageNumber(pageNumber);
//		textTabs.setXPosition(xPosition);
//		textTabs.setYPosition(yPosition);
//		textTabs.setTabLabel(tabLabel);
//		textTabs.setValue(value);
		return textTabs;
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
	 * @param key
	 */
	private void listTemplates (String key) {
		// /restapi/v2.1/accounts/{accountId}/templates
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Bearer " + key);
		
		// API - {{baseUrl}}/{{apiVersion}}/accounts/{{accountId}}/envelopes/{{envelopeId}}
		String baseUrl = propertiesConfig.getDocusignBasepath();
		String apiVersion = propertiesConfig.getDocusignApiVersion();
		String accountId = propertiesConfig.getDocusignAccountId();
		String envUrl = baseUrl + "/restapi/" + apiVersion + "/accounts/" + accountId + "/templates";
		
		ResponseEntity<EnvelopeTemplates> response = restTemplate.exchange(envUrl, HttpMethod.GET, entity, EnvelopeTemplates.class);
		log.info("EnvelopeTemplates Response ---------" + response.getBody());		
	}
	
	/**
	 * 
	 * @param envelopeId
	 * @param key
	 */
	private void applyTemplate (String envelopeId, String key) {
		// /restapi/v2.1/accounts/{accountId}/templates
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Bearer " + key);
		
		// API - {{baseUrl}}/{{apiVersion}}/accounts/{{accountId}}/envelopes/{{envelopeId}}
		String baseUrl = propertiesConfig.getDocusignBasepath();
		String apiVersion = propertiesConfig.getDocusignApiVersion();
		String accountId = propertiesConfig.getDocusignAccountId();
		String envUrl = baseUrl + "/restapi/" + apiVersion + "/accounts/" + accountId + "/envelopes/" + envelopeId + "/templates";
		
		ResponseEntity<String> response = restTemplate.exchange(envUrl, HttpMethod.POST, entity, String.class);
		log.info("applyTemplate Response ---------" + response.getBody());
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
