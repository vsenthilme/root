package com.mnrclara.api.docusign.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.docusign.esign.api.EnvelopesApi;
import com.mnrclara.api.docusign.model.Document;
import com.mnrclara.api.docusign.model.Envelope;
import com.mnrclara.api.docusign.model.EnvelopeResponse;
import com.mnrclara.api.docusign.model.Recipients;
import com.mnrclara.api.docusign.model.Signer;
import com.mnrclara.api.docusign.model.UserInfo;
import com.mnrclara.api.docusign.model.oauth.AuthToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DocusignAuthTokenService2 {
	
//	@Autowired
//	PropertiesConfig propertiesConfig;
	
	public UserInfo generateOAuthToken () {
		
		RestTemplate restTemplate1 = new RestTemplate();
		HttpHeaders headers1 = new HttpHeaders();
		HttpEntity<String> request1 = new HttpEntity<String>(headers1);
		headers1.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		
		String url = "https://account-d.docusign.com/oauth/auth?\r\n"
				+ "   response_type=code\r\n"
				+ "   &scope=signature\r\n"
				+ "   &client_id=7b3ae65a-fd75-4a96-aadc-175597993fff\r\n"
				+ "   &redirect_uri=http://localhost:8080/docusign/callback";
		ResponseEntity<String> response1 = restTemplate1.exchange(url, HttpMethod.POST, request1, String.class);
		log.info("Access Token Response ---------" + response1.getBody());
		return null;
		
		//-----------------STEP1--------------------------------------------
		/*
		 * Integration Key (Client ID) - 37ae229e-aeb7-4039-ab97-f6afb4cc7263
		 * Secret Key - 26e6fe90-f1d6-45c5-9f48-e65afd735476
		 */
//		String clientId = "37ae229e-aeb7-4039-ab97-f6afb4cc7263";
//		String clientSecretKey = "26e6fe90-f1d6-45c5-9f48-e65afd735476";
//		
//		// Client Id and Client Secret Key to be sent as part of header for authentication
//		String credentials = clientId + ":" + clientSecretKey;
//		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
//
//		headers1.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//		headers1.add("Authorization", "Basic " + encodedCredentials);
//		
//		String accessTokenUrl = "https://account-d.docusign.com/oauth/token";
//				accessTokenUrl += "?grant_type=authorization_code&scope=extended"
//						+ "&code=eyJ0eXAiOiJNVCIsImFsZyI6IlJTMjU2Iiwia2lkIjoiNjgxODVmZjEtNGU1MS00Y2U5LWFmMWMtNjg5ODEyMjAzMzE3In0.AQoAAAABAAYABwAAVgbgtY_ZSAgAAOKMJ7aP2UgCAHasHT6Zf6JFhtw393A8MzMVAAEAAAAYAAEAAAAFAAAADQAkAAAAMzdhZTIyOWUtYWViNy00MDM5LWFiOTctZjZhZmI0Y2M3MjYzIgAkAAAAMzdhZTIyOWUtYWViNy00MDM5LWFiOTctZjZhZmI0Y2M3MjYzNwD0gpXlAEu8SKBFIQ1v_E8LMAAAMoIOso_ZSA.Gt92rgUCX0DacqdGzTZywCWOqCJzOHVBujwwES4NcK4czSqETes-6Q5g9-bzssyCYV2Zh5Zh8gxCpnP3W_6Fwn0khuU8Yw7JGySSzX04nDPt3TBPM3TxvZ9BbFeOdohNH-eWq5Cy-NA4LKSliBGlK5sVGYMcCHqB2FNp2w8vsPzr4Qt6xw2MvPGIVbk9ZFekuu5QJa558w83a-VTYDr-mlRZxbN8b0FarKTnmq15FkVXJi0W219DndCb-J_P4EhFmN-q4E59MRza45EzqzHl2A_yWKjtC1pROVc6UywI9f0CheAWjE08A8sc6HaTkMq2u4xQqVm2DHfhoP_188uHtA";
//		
//		log.info("Access token url: " + accessTokenUrl);
//
//		ResponseEntity<AuthToken> response22 = restTemplate1.exchange(accessTokenUrl, HttpMethod.POST, request1, AuthToken.class);
//		log.info("Access Token Response ---------" + response22.getBody());
		
//		
//		RestTemplate restTemplate2 = new RestTemplate();
//		HttpHeaders headers2 = new HttpHeaders();
//		HttpEntity<String> request2 = new HttpEntity<String>(headers1);
//		headers1.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//		headers1.add("Authorization", "Bearer eyJ0eXAiOiJNVCIsImFsZyI6IlJTMjU2Iiwia2lkIjoiNjgxODVmZjEtNGU1MS00Y2U5LWFmMWMtNjg5ODEyMjAzMzE3In0.AQoAAAABAAUABwCA42odto_ZSAgAgCOOK_mP2UgCAHasHT6Zf6JFhtw393A8MzMVAAEAAAAYAAEAAAAFAAAADQAkAAAAMzdhZTIyOWUtYWViNy00MDM5LWFiOTctZjZhZmI0Y2M3MjYzIgAkAAAAMzdhZTIyOWUtYWViNy00MDM5LWFiOTctZjZhZmI0Y2M3MjYzMAAAMoIOso_ZSDcA9IKV5QBLvEigRSENb_xPCw.NSs4L0wZomWkQyfCLCbwWkpLW9vjvU4IqmipFJ-DFsXaIc10oUqC47Q2il-Ipw7gUosAecfHxLVTGrY3eonXFBERVn4d6gZuFUzZlm4oQw5ikL7Y7HwxrBJwmbTt1oTeraKCwZpynpXOaaBspFwjRzxfhSIAwadg4KwkKETAVt8R-_FjIbIyfz4DU_UipYqFyH_ZsgVuCkPbezleXBhcXeMiJI5YwAWctjOJyWQf4nIbD5H0XsvyxtSO_-eZGwGSGUc00Y04NBZUB5TFbUNLzwJcicVhP93uJJD1IITmjjbBtEcA81SR9x3Pz5jBWlM71HJ05vixCAv7LxZPN1Zm2w");
//		
//		String userInfoUrl = "https://account-d.docusign.com/oauth/userinfo";
//		ResponseEntity<UserInfo> response2 = restTemplate2.exchange(userInfoUrl, HttpMethod.GET, request2, UserInfo.class);
//		log.info("Access Token Response ---------" + response2.getBody());
//		
//		return response2.getBody();
	}
	
	public EnvelopeResponse envelope () {
		/*
		 *  {
		  "documents": [
		    {
		      "documentBase64": "{{document}}",
		      "documentId": "123",
		      "fileExtension": "docx",
		      "name": "Test Docusign"
		    }
		  ],
		  "emailSubject": "Please sign the document",
		  "recipients": {
		    "signers": [
		      {
		        "email": "muruvel@gmail.com",
		        "name": "Murugavel",
		        "recipientId": "123"
		      }
		    ]
		  },
		  "status": "sent"
		}
		 */
		Envelope envelope = new Envelope();
		
		Document document = new Document();
		document.setDocumentBase64("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");
		document.setDocumentId("123");
		document.setFileExtension("docx");
		document.setName("Test Docusign");		
		List<Document> documents = new ArrayList<>();
		documents.add(document);
		
		envelope.setDocuments(documents); 						// documents
		envelope.setEmailSubject("Please sign the document");	// emailSubject
		
		Recipients recipients = new Recipients();
		List<Signer> signers = new ArrayList<>();
		
//		// Signer1
//		Signer signer = new Signer();
//		signer.setEmail("muruvel@gmail.com");
//		signer.setName("Murugavel");
//		signer.setRecipientId("123");
		
		// Signer2
		Signer signer2 = new Signer();
		signer2.setEmail("karthik@tekclover.com");
		signer2.setName("Karthik");
		signer2.setRecipientId("123");
		
//		// Signer3
//		Signer signer3 = new Signer();
//		signer3.setEmail("raj@tekclover.com");
//		signer3.setName("Raj");
//		signer3.setRecipientId("345");
		
//		signers.add(signer);
		signers.add(signer2);
//		signers.add(signer3);
		
		recipients.setSigners(signers);
		envelope.setRecipients(recipients);						// recipients
		
		envelope.setStatus("sent");								// status
		
		RestTemplate restTemplate2 = new RestTemplate();
		HttpHeaders headers2 = new HttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(envelope, headers2);
		headers2.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers2.add("Authorization", "Bearer eyJ0eXAiOiJNVCIsImFsZyI6IlJTMjU2Iiwia2lkIjoiNjgxODVmZjEtNGU1MS00Y2U5LWFmMWMtNjg5ODEyMjAzMzE3In0.AQoAAAABAAUABwCA42odto_ZSAgAgCOOK_mP2UgCAHasHT6Zf6JFhtw393A8MzMVAAEAAAAYAAEAAAAFAAAADQAkAAAAMzdhZTIyOWUtYWViNy00MDM5LWFiOTctZjZhZmI0Y2M3MjYzIgAkAAAAMzdhZTIyOWUtYWViNy00MDM5LWFiOTctZjZhZmI0Y2M3MjYzMAAAMoIOso_ZSDcA9IKV5QBLvEigRSENb_xPCw.NSs4L0wZomWkQyfCLCbwWkpLW9vjvU4IqmipFJ-DFsXaIc10oUqC47Q2il-Ipw7gUosAecfHxLVTGrY3eonXFBERVn4d6gZuFUzZlm4oQw5ikL7Y7HwxrBJwmbTt1oTeraKCwZpynpXOaaBspFwjRzxfhSIAwadg4KwkKETAVt8R-_FjIbIyfz4DU_UipYqFyH_ZsgVuCkPbezleXBhcXeMiJI5YwAWctjOJyWQf4nIbD5H0XsvyxtSO_-eZGwGSGUc00Y04NBZUB5TFbUNLzwJcicVhP93uJJD1IITmjjbBtEcA81SR9x3Pz5jBWlM71HJ05vixCAv7LxZPN1Zm2w");
		
		String envUrl = "https://demo.docusign.net/restapi/v2.1/accounts/cc83e9d5-71d4-4606-bcbc-815360cdf457/envelopes";
		ResponseEntity<EnvelopeResponse> response2 = restTemplate2.exchange(envUrl, HttpMethod.POST, entity, EnvelopeResponse.class);
		log.info("Access Token Response ---------" + response2.getBody());
		return response2.getBody();
	}
	
	public void envelopStatus () {
		// API - {{baseUrl}}/{{apiVersion}}/accounts/{{accountId}}/envelopes/{{envelopeId}}
	}
	
	public void listDocuments (String basePath, String accessToken) {
		// API - {{baseUrl}}/{{apiVersion}}/accounts/{{accountId}}/envelopes/{{envelopeId}}/documents
		/*
		 * {
		    "envelopeId": "16609626-7258-45af-bafb-11c9f15a3ccc",
		    "envelopeDocuments": [
		        {
		            "documentId": "123",
		            "documentIdGuid": "6119c003-509b-4fba-bbe9-b35fa87e35b5",
		            "name": "Test Docusign",
		            "type": "content",
		            "uri": "/envelopes/16609626-7258-45af-bafb-11c9f15a3ccc/documents/123",
		            "order": "1",
		            "pages": [
		                {
		                    "pageId": "fd507a5b-cd4f-4d85-b046-8907728349d9",
		                    "sequence": "1",
		                    "height": "842",
		                    "width": "595",
		                    "dpi": "72"
		                }
		            ],
		            "availableDocumentTypes": [
		                {
		                    "type": "electronic",
		                    "isDefault": "true"
		                }
		            ],
		            "display": "inline",
		            "includeInDownload": "true",
		            "signerMustAcknowledge": "no_interaction",
		            "templateRequired": "false",
		            "authoritativeCopy": "false"
		        },
		        {
		            "documentId": "certificate",
		            "documentIdGuid": "42292946-7ce8-409d-86dc-2174e600e2fc",
		            "name": "Summary",
		            "type": "summary",
		            "uri": "/envelopes/16609626-7258-45af-bafb-11c9f15a3ccc/documents/certificate",
		            "order": "999",
		            "availableDocumentTypes": [
		                {
		                    "type": "electronic",
		                    "isDefault": "true"
		                }
		            ],
		            "display": "inline",
		            "includeInDownload": "true",
		            "signerMustAcknowledge": "no_interaction",
		            "templateRequired": "false",
		            "authoritativeCopy": "false"
		        }
		    ]
		}
		 */
		
		
//		EnvelopesApi envelopesApi = createEnvelopesApi("https://demo.docusign.net/restapi/", user.getAccessToken());
//		EnvelopeDocumentsResult result = envelopesApi.listDocuments(session.getAccountId(), session.getEnvelopeId());
	}
	
	public void downloadEnvelop () {
		// API - {{baseUrl}}/{{apiVersion}}/accounts/{{accountId}}/envelopes/{{envelopeId}}/documents/{{documentId}}
	}
	
	public AuthToken genRefToekn (String refToken) {
		RestTemplate restTemplate1 = new RestTemplate();
		HttpHeaders headers1 = new HttpHeaders();
		HttpEntity<String> request1 = new HttpEntity<String>(headers1);
		headers1.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		
		String clientId = "37ae229e-aeb7-4039-ab97-f6afb4cc7263";
		String clientSecretKey = "26e6fe90-f1d6-45c5-9f48-e65afd735476";
		
		String credentials = clientId + ":" + clientSecretKey;
		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
		
		headers1.add("Authorization", "Basic " + encodedCredentials);
		
		String accessTokenUrl = "https://account-d.docusign.com/oauth/token";
		accessTokenUrl += "?grant_type=refresh_token"
				+ "&refresh_token=" + refToken;

		log.info("Access token url: " + accessTokenUrl);
		
		ResponseEntity<AuthToken> response1 = restTemplate1.exchange(accessTokenUrl, HttpMethod.POST, request1, AuthToken.class);
		log.info("Access Token Response ---------" + response1.getBody());
		return response1.getBody();
	}
}
