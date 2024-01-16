package com.flourish.b2b.api.gateway.util;

import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.flourish.b2b.api.gateway.model.auth.OAuthToken;

public class PasswordEncoder {
	
	static BCryptPasswordEncoder passwordEncoder;
	
	public PasswordEncoder () {
		passwordEncoder = new BCryptPasswordEncoder();
	}
	
	public BCryptPasswordEncoder getEncoder () {
		return passwordEncoder;
	}
	
	public static String encodePassword (String password) {
		String hashedPassword = passwordEncoder.encode(password);
		return hashedPassword;
	}
	
	public static void main(String[] args) {
		RestTemplate restTemplate = new RestTemplate();

		// According OAuth documentation we need to send the client id and secret key in the header for authentication
		String credentials = "pixeltrice:pixeltrice-secret-key";
		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Basic " + encodedCredentials);

		HttpEntity<String> request = new HttpEntity<String>(headers);

		String access_token_url = "http://localhost:8085/oauth/token";
		access_token_url += "?grant_type=password&username=muru&password=welcome";
//		access_token_url += "&redirect_uri=http://localhost:8090/showEmployees";

		ResponseEntity<OAuthToken> response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, OAuthToken.class);

		System.out.println("Access Token Response ---------" + response.getBody());
	}
}
