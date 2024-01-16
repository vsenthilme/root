package com.flourish.b2b.api.gateway.service;

import java.util.Arrays;

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

import com.flourish.b2b.api.gateway.config.PropertiesConfig;
import com.flourish.b2b.api.gateway.model.auth.OAuthToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommonService {
	
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
	 * generateOAuthToken
	 * @return
	 */
	public String generateOAuthToken () {
		// Client Id and Client Secret Key to be sent as part of header for authentication
		String credentials = propertiesConfig.getClientId() + ":" + propertiesConfig.getClientSecretKey();
		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Basic " + encodedCredentials);

		HttpEntity<String> request = new HttpEntity<String>(headers);

//		String access_token_url = "http://localhost:8085/oauth/token";
//		access_token_url += "?grant_type=password&username=muru&password=welcome";
//		access_token_url += "&redirect_uri=http://localhost:8090/showEmployees";
		
		String accessTokenUrl = propertiesConfig.getAccessTokenUrl();
		accessTokenUrl += "?grant_type=" + propertiesConfig.getGrantType()
						+ "&username=" + propertiesConfig.getUsername()
						+ "&password=" + propertiesConfig.getPassword();
		log.info("accessTokenUrl : " + accessTokenUrl);

		ResponseEntity<OAuthToken> response = restTemplate.exchange(accessTokenUrl, HttpMethod.POST, request, OAuthToken.class);
		log.info("Access Token Response ---------" + response.getBody());
		return response.getBody().getAccess_token();
	}
}
