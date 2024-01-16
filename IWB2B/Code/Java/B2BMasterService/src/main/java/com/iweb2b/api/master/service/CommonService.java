package com.iweb2b.api.master.service;

import java.util.Arrays;

import javax.validation.Valid;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.iweb2b.api.master.model.auth.AuthToken;
import com.iweb2b.api.master.model.auth.AuthTokenRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommonService {
	
//	@Autowired
//	PropertiesConfig propertiesConfig;
	
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
//		String accessTokenUrl = apiUrl;
		
		String accessTokenUrl = "http://localhost:5001/iwe/api/b2b-master-service";
		/*
		// AuthToken URL dynamically
		if (apiUrl.equalsIgnoreCase("wms-idmaster-service")) {
			accessTokenUrl = propertiesConfig.getIdmasterAccessTokenUrl();
			log.info("accessTokenUrl : " + accessTokenUrl);
		} else if (apiUrl.equalsIgnoreCase("wms-enterprise-service")) {
			accessTokenUrl = propertiesConfig.getEnterpriseAccessTokenUrl();
		} else if (apiUrl.equalsIgnoreCase("wms-transaction-service")) {
			accessTokenUrl = propertiesConfig.getTransactionAccessTokenUrl();
		} else if (apiUrl.equalsIgnoreCase("wms-masters-service")) {
			accessTokenUrl = propertiesConfig.getMastersAccessTokenUrl();
			log.info("------------###########------------> " + accessTokenUrl);
		}*/
		
		log.info("Access token url: " + accessTokenUrl);
		accessTokenUrl += "?grant_type=" + grantType
						+ "&username=" + oauthUserName
						+ "&password=" + oauthPassword;
		log.info("accessTokenUrl : " + accessTokenUrl);

		ResponseEntity<AuthToken> response = restTemplate.exchange(accessTokenUrl, HttpMethod.POST, request, AuthToken.class);
		log.info("Access Token Response ---------" + response.getBody());
		return response.getBody();
	}
	
	public AuthToken getAuthToken(@Valid AuthTokenRequest authTokenRequest) {
		return generateOAuthToken(authTokenRequest.getApiName(),
				authTokenRequest.getClientId(), 
				authTokenRequest.getClientSecretKey(),
				authTokenRequest.getGrantType(),
				authTokenRequest.getOauthUserName(),
				authTokenRequest.getOauthPassword());	
	}
}
