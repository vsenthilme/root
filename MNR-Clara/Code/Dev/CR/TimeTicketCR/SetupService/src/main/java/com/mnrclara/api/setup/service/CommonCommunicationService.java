package com.mnrclara.api.setup.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.mnrclara.api.setup.model.commonservice.SMS;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommonCommunicationService {
	
	@Autowired
	com.mnrclara.api.setup.config.PropertiesConfig propertiesConfig;
	
	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}
	
	private String getCommonServiceApiUrl () {
		return propertiesConfig.getCommonServiceUrl();
	}
	
	//--------------------------------------------------------------------------------------------------------------------
	// POST - Send OTP
	public Boolean sendOtp (String authToken, SMS sms) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			log.info("URL : " + getCommonServiceApiUrl() + "communication/sendSMS");
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCommonServiceApiUrl() + "communication/sendSMS");

			HttpEntity<?> entity = new HttpEntity<>(sms, headers);
			ResponseEntity<Boolean> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Boolean.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @param email
	 * @param authToken
	 * @return
	 */
	public Boolean sendEmail(com.mnrclara.api.setup.model.commonservice.EMail email, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(email, headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCommonServiceApiUrl() + "communication/sendEmail");
			ResponseEntity<Boolean> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Boolean.class);
			log.info("result : " + result);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
