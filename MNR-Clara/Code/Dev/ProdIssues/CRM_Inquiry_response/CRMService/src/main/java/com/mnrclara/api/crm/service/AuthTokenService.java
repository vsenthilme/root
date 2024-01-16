package com.mnrclara.api.crm.service;

import java.util.Arrays;

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

import com.mnrclara.api.crm.config.PropertiesConfig;
import com.mnrclara.api.crm.controller.exception.BadRequestException;
import com.mnrclara.api.crm.model.auth.AuthToken;
import com.mnrclara.api.crm.model.auth.AuthTokenRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthTokenService {
	
	@Autowired
	PropertiesConfig propertiesConfig;
	
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
	 * 
	 * @param apiUrl
	 * @param clientId
	 * @param clientSecretKey
	 * @param grantType
	 * @param oauthUserName
	 * @param oauthPassword
	 * @return
	 */
	private AuthToken generateOAuthToken (String apiUrl, String clientId, String clientSecretKey, String grantType, String oauthUserName, String oauthPassword) {
		// Client Id and Client Secret Key to be sent as part of header for authentication
		String credentials = clientId + ":" + clientSecretKey;
		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Basic " + encodedCredentials);

		HttpEntity<String> request = new HttpEntity<String>(headers);
		String accessTokenUrl = apiUrl;
		
		// AuthToken URL dynamically
		if (apiUrl.equalsIgnoreCase("mnr-setup-service")) {
			accessTokenUrl = propertiesConfig.getSetupAccessTokenUrl();
		} else if (apiUrl.equalsIgnoreCase("mnr-common-service")) {
			accessTokenUrl = propertiesConfig.getCommonAccessTokenUrl(); 
		} else if (apiUrl.equalsIgnoreCase("mnr-management-service")) { ///mnr-management-service
			accessTokenUrl = propertiesConfig.getManagementAccessTokenUrl(); 
		} else if (apiUrl.equalsIgnoreCase("mnr-accounting-service")) { ///mnr-management-service
			accessTokenUrl = propertiesConfig.getAccountingAccessTokenUrl(); 
		} else {
			log.info("The givem URL is not available. Quiting.");
			throw new BadRequestException("The givem URL is not available. Quiting");
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
	 * @param authTokenRequest
	 * @return
	 */
	public AuthToken getAuthToken(AuthTokenRequest authTokenRequest) {
		return generateOAuthToken(authTokenRequest.getApiName(),
				authTokenRequest.getClientId(), 
				authTokenRequest.getClientSecretKey(),
				authTokenRequest.getGrantType(),
				authTokenRequest.getOauthUserName(),
				authTokenRequest.getOauthPassword());	
	}
	
	/**
	 * 
	 * @return
	 */
	public AuthToken getSetupServiceAuthToken () {
		// Generate AuthToken for SetupService
		AuthTokenRequest authTokenRequest = new AuthTokenRequest();
		authTokenRequest.setApiName("mnr-setup-service");
		authTokenRequest.setClientId(propertiesConfig.getClientId());
		authTokenRequest.setClientSecretKey(propertiesConfig.getClientSecretKey());
		authTokenRequest.setGrantType(propertiesConfig.getGrantType());
		authTokenRequest.setOauthUserName(propertiesConfig.getUsername());
		authTokenRequest.setOauthPassword(propertiesConfig.getPassword());
		
		return getAuthToken(authTokenRequest);
	}
		
	/**
	 * 
	 * @return
	 */
	public AuthToken getCommonServiceAuthToken () {
		// Generate AuthToken for SetupService
		AuthTokenRequest authTokenRequest = new AuthTokenRequest();
		authTokenRequest.setApiName("mnr-common-service");
		authTokenRequest.setClientId(propertiesConfig.getClientId());
		authTokenRequest.setClientSecretKey(propertiesConfig.getClientSecretKey());
		authTokenRequest.setGrantType(propertiesConfig.getGrantType());
		authTokenRequest.setOauthUserName(propertiesConfig.getUsername());
		authTokenRequest.setOauthPassword(propertiesConfig.getPassword());
		return getAuthToken(authTokenRequest);
	}
			
	/**
	 * 
	 * @return
	 */
	public AuthToken getManagementServiceAuthToken () {
		// Generate AuthToken for SetupService
		AuthTokenRequest authTokenRequest = new AuthTokenRequest();
		authTokenRequest.setApiName("mnr-management-service");
		authTokenRequest.setClientId(propertiesConfig.getClientId());
		authTokenRequest.setClientSecretKey(propertiesConfig.getClientSecretKey());
		authTokenRequest.setGrantType(propertiesConfig.getGrantType());
		authTokenRequest.setOauthUserName(propertiesConfig.getUsername());
		authTokenRequest.setOauthPassword(propertiesConfig.getPassword());
		return getAuthToken(authTokenRequest);
	}
	
	/**
	 * 
	 * @return
	 */
	public AuthToken getAccountingServiceAuthToken() {
		// Generate AuthToken for CRMService
		AuthTokenRequest authTokenRequest = new AuthTokenRequest();
		authTokenRequest.setApiName("mnr-accounting-service");
		authTokenRequest.setClientId(propertiesConfig.getClientId());
		authTokenRequest.setClientSecretKey(propertiesConfig.getClientSecretKey());
		authTokenRequest.setGrantType(propertiesConfig.getGrantType());
		authTokenRequest.setOauthUserName(propertiesConfig.getUsername());
		authTokenRequest.setOauthPassword(propertiesConfig.getPassword());
		return getAuthToken(authTokenRequest);
	}
}
