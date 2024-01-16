package com.mnrclara.api.cg.transaction.service;

import com.mnrclara.api.cg.transaction.config.PropertiesConfig;
import com.mnrclara.api.cg.transaction.exception.BadRequestException;
import com.mnrclara.api.cg.transaction.model.auth.AuthToken;
import com.mnrclara.api.cg.transaction.model.auth.AuthTokenRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

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
		if (apiUrl.equalsIgnoreCase("mnr-cg-setup-service")) {
			accessTokenUrl = propertiesConfig.getCgSetupAccessTokenUrl();
		} else {
			log.info("The given URL is not available. Quiting.");
			throw new BadRequestException("The given URL is not available. Quiting");
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
		authTokenRequest.setApiName("mnr-cg-setup-service");
		authTokenRequest.setClientId(propertiesConfig.getClientId());
		authTokenRequest.setClientSecretKey(propertiesConfig.getClientSecretKey());
		authTokenRequest.setGrantType(propertiesConfig.getGrantType());
		authTokenRequest.setOauthUserName(propertiesConfig.getUsername());
		authTokenRequest.setOauthPassword(propertiesConfig.getPassword());

		return getAuthToken(authTokenRequest);
	}


}
