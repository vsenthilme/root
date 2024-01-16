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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

		// mnr-spark-service
		// AuthToken URL dynamically
		if (apiUrl.equalsIgnoreCase("mnr-setup-service")) {
			accessTokenUrl = propertiesConfig.getSetupAccessTokenUrl();
		} else if (apiUrl.equalsIgnoreCase("mnr-crm-service")) {
			accessTokenUrl = propertiesConfig.getCrmAccessTokenUrl();
		} else if (apiUrl.equalsIgnoreCase("mnr-management-service")) {
			accessTokenUrl = propertiesConfig.getManagementAccessTokenUrl();
		} else if (apiUrl.equalsIgnoreCase("mnr-accounting-service")) {
			accessTokenUrl = propertiesConfig.getAccountingAccessTokenUrl();
		} else if (apiUrl.equalsIgnoreCase("mnr-common-service")) {
			accessTokenUrl = propertiesConfig.getCommonAccessTokenUrl();
		} else if (apiUrl.equalsIgnoreCase("mnr-spark-service")) {
			accessTokenUrl = propertiesConfig.getSparkAccessTokenUrl();
		} else {
			log.info("The givem URL is not available. Quiting.");
			throw new BadRequestException("The givem URL is not available. Quiting");
		}

		log.info("Access token url: " + accessTokenUrl);
		accessTokenUrl += "?grant_type=" + grantType + "&username=" + oauthUserName + "&password=" + oauthPassword;
		log.info("accessTokenUrl : " + accessTokenUrl);

		ResponseEntity<AuthToken> response = restTemplate.exchange(accessTokenUrl, HttpMethod.POST, request, AuthToken.class);
		log.info("Access Token Response ---------" + response.getBody());
		return response.getBody();
	}
	
	//--------------------SharePoint-Token----------------------------------------------------------
	/**
	 * 
	 * @return
	 */
	public AuthToken getSharePointOAuthToken() {
		// Client Id and Client Secret Key to be sent as part of header for authentication
		/*
		 * 	sp.tenant.id=0b198ef5-3ea1-4bc9-91a6-a6874d0dee1e
			sp.client.id=46650881-943c-4904-a1c6-9150788b49ae
			sp.client.secret=iiz8Q~O_noWHZKa-5fI4DUlvwbZjLlX-4eGwCaj1
			sp.grant_type=client_credentials
			sp.scope=https://graph.microsoft.com/.default
			sp.token.url=https://login.microsoftonline.com/0b198ef5-3ea1-4bc9-91a6-a6874d0dee1e/oauth2/v2.0/token
		 */
		String clientId = propertiesConfig.getSpClientId();
		String clientSecretKey = propertiesConfig.getSpClientSecret();
		String grantType = propertiesConfig.getSpGrantType();
		String scope = propertiesConfig.getSpScope();
		String accessTokenUrl = propertiesConfig.getSpTokenUrl();
		log.info("accessTokenUrl : " + accessTokenUrl);
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("grant_type", grantType);
		map.add("client_id", clientId);
		map.add("client_secret", clientSecretKey);
		map.add("scope", scope);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
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
	
	public AuthToken getSparkServiceAuthToken() {
		// Generate AuthToken for CRMService
		AuthTokenRequest authTokenRequest = new AuthTokenRequest();
		authTokenRequest.setApiName("mnr-spark-service");
		authTokenRequest.setClientId(propertiesConfig.getClientId());
		authTokenRequest.setClientSecretKey(propertiesConfig.getClientSecretKey());
		authTokenRequest.setGrantType(propertiesConfig.getGrantType());
		authTokenRequest.setOauthUserName(propertiesConfig.getUsername());
		authTokenRequest.setOauthPassword(propertiesConfig.getPassword());

		return getAuthToken(authTokenRequest);
	}
}
