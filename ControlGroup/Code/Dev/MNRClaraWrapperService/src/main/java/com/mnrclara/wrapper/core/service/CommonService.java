package com.mnrclara.wrapper.core.service;

import java.util.Arrays;
import java.util.Collections;

import com.mnrclara.wrapper.core.model.crm.EMail;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mnrclara.wrapper.core.config.PropertiesConfig;
import com.mnrclara.wrapper.core.model.auth.AuthToken;
import com.mnrclara.wrapper.core.util.CheckMail;
import com.mnrclara.wrapper.core.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class CommonService {
	
	@Autowired
	PropertiesConfig propertiesConfig;

	private String getCommonServiceApiUrl(){
		return propertiesConfig.getCommonServiceUrl();
	}

	/**
	 * Returns RestTemplate Object
	 * @return
	 */
	public RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();

		// Object Convertor
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter
				.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
		restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
		return restTemplate;
	}
	
	/**
	 * generateOAuthToken
	 * @param apiUrl
	 * @param clientId
	 * @param clientSecretKey
	 * @param grantType
	 * @param oauthUserName
	 * @param oauthPassword
	 * @return
	 */
	public AuthToken generateOAuthToken (String apiUrl, String clientId, String clientSecretKey, String grantType, String oauthUserName, String oauthPassword) {
		// Client Id and Client Secret Key to be sent as part of header for authentication
		String credentials = clientId + ":" + clientSecretKey;
		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Basic " + encodedCredentials);

		HttpEntity<String> request = new HttpEntity<String>(headers);
		String accessTokenUrl = apiUrl;
		log.info("accessTokenUrl; " + accessTokenUrl);
		
		// AuthToken URL dynamically
		if (apiUrl.equalsIgnoreCase("mnr-setup-service")) {
			accessTokenUrl = propertiesConfig.getSetupAccessTokenUrl();
		} else if (apiUrl.equalsIgnoreCase("mnr-crm-service")) {
			accessTokenUrl = propertiesConfig.getCrmAccessTokenUrl();
		} else if (apiUrl.equalsIgnoreCase("mnr-management-service")) {
			accessTokenUrl = propertiesConfig.getManagementAccessTokenUrl();
		} else if (apiUrl.equalsIgnoreCase("mnr-accounting-service")) {
			accessTokenUrl = propertiesConfig.getAccountingAccessTokenUrl();
		} else if (apiUrl.equalsIgnoreCase("mnr-cg-setup-service")) {
			accessTokenUrl = propertiesConfig.getCgSetupAccessTokenUrl();
		} else if (apiUrl.equalsIgnoreCase("mnr-cg-transaction-service")) {
			accessTokenUrl = propertiesConfig.getCgTransactionAccessTokenUrl();
			log.info("accessTokenUrl Acc::---------> : " + accessTokenUrl);
		}
		
		log.info("Access token url: " + accessTokenUrl);
		accessTokenUrl += "?grant_type=" + grantType
						+ "&username=" + oauthUserName
						+ "&password=" + oauthPassword;
		log.info("accessTokenUrl : " + accessTokenUrl);

		ResponseEntity<AuthToken> response = restTemplate.exchange(accessTokenUrl, HttpMethod.POST, request, AuthToken.class);
		log.info("Access Token Response ---------" + response.getBody());
		return response.getBody();
	}
	
	/**
	 * 
	 * @return
	 */
	public String generateUUID () {
		CommonUtils cUtils = new CommonUtils ();
		return cUtils.randomUUID();
	}
	
	/**
	 * 
	 */
	public void readEmails() {
		String host = "smtp.office365.com";// change accordingly
		String mailStoreType = "pop3";
		String username = "intake@montyramirezlaw.com";// change accordingly
		String password = "TestPass@123";// change accordingly
		CheckMail.check(host, mailStoreType, username, password);
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

	public String sendEmailWithAttachment(EMail email, String file, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(email, headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCommonServiceApiUrl() + "communication/cg/sendEmailWithAttachment")
					.queryParam("file", file);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public Boolean sendEmail(com.mnrclara.wrapper.core.model.setup.EMail email, String fileName, String location, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(email, headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCommonServiceApiUrl() + "communication/sendEmailWithAttachment")
					.queryParam("fileName", fileName)
					.queryParam("location", location);
			ResponseEntity<Boolean> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Boolean.class);
			log.info("result : " + result);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
