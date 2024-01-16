package com.mnrclara.api.accounting.service;

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

import com.mnrclara.api.accounting.config.PropertiesConfig;
import com.mnrclara.api.accounting.model.communication.SMS;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommonService {
	
	@Autowired
	PropertiesConfig propertiesConfig;
	
	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}
	
	private String getCommonServiceApiUrl () {
		return propertiesConfig.getCommonServiceUrl();
	}
	
	/**
	 * 
	 * @param toNumber
	 * @param textMessage
	 * @param authToken
	 * @return
	 */
	public Boolean sendSMS(Long toNumber, String textMessage, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			SMS sms = new SMS();
			sms.setToNumber(toNumber);
			sms.setTextMessage(textMessage);
			
			HttpEntity<?> entity = new HttpEntity<>(sms, headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCommonServiceApiUrl() + "communication/sendSMS");
			ResponseEntity<Boolean> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Boolean.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
