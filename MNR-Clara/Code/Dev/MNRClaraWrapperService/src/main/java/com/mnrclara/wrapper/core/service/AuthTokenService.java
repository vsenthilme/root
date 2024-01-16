package com.mnrclara.wrapper.core.service;

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

import com.mnrclara.wrapper.core.config.PropertiesConfig;
import com.mnrclara.wrapper.core.exception.BadRequestException;
import com.mnrclara.wrapper.core.model.auth.AuthToken;
import com.mnrclara.wrapper.core.model.auth.AuthTokenRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthTokenService {

	@Autowired
	PropertiesConfig propertiesConfig;

	/**
	 * Returns RestTemplate Object
	 * 
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

	private AuthToken generateOAuthToken(String apiUrl, String clientId, String clientSecretKey, String grantType,
			String oauthUserName, String oauthPassword) {
		// Client Id and Client Secret Key to be sent as part of header for
		// authentication
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
		} else if (apiUrl.equalsIgnoreCase("mnr-crm-service")) {
			accessTokenUrl = propertiesConfig.getCrmAccessTokenUrl();
		} else if (apiUrl.equalsIgnoreCase("mnr-management-service")) {
			accessTokenUrl = propertiesConfig.getManagementAccessTokenUrl();
		} else if (apiUrl.equalsIgnoreCase("mnr-accounting-service")) {
			accessTokenUrl = propertiesConfig.getAccountingAccessTokenUrl();
		} else {
			log.info("The givem URL is not available. Quiting.");
			throw new BadRequestException("The givem URL is not available. Quiting");
		}

		log.info("Access token url: " + accessTokenUrl);
		accessTokenUrl += "?grant_type=" + grantType + "&username=" + oauthUserName + "&password=" + oauthPassword;
		log.info("accessTokenUrl : " + accessTokenUrl);

		ResponseEntity<AuthToken> response = restTemplate.exchange(accessTokenUrl, HttpMethod.POST, request,
				AuthToken.class);
		log.info("Access Token Response ---------" + response.getBody());
		return response.getBody();
	}

	public AuthToken getAuthToken(AuthTokenRequest authTokenRequest) {
		return generateOAuthToken(authTokenRequest.getApiName(), authTokenRequest.getClientId(),
				authTokenRequest.getClientSecretKey(), authTokenRequest.getGrantType(),
				authTokenRequest.getOauthUserName(), authTokenRequest.getOauthPassword());
	}

	/**
	 * getSetupServiceAuthToken
	 * 
	 * @return
	 */
	public AuthToken getSetupServiceAuthToken() {
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
	 * getCommonServiceAuthToken
	 * 
	 * @return
	 */
	public AuthToken getCommonServiceAuthToken() {
		// Generate AuthToken for CommonService
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
	public AuthToken getManagementServiceAuthToken() {
		// Generate AuthToken for ManagementService
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
	 * getCrmServiceAuthToken
	 * 
	 * @return
	 */
	public AuthToken getCrmServiceAuthToken() {
		// Generate AuthToken for CRMService
		AuthTokenRequest authTokenRequest = new AuthTokenRequest();
		authTokenRequest.setApiName("mnr-crm-service");
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
