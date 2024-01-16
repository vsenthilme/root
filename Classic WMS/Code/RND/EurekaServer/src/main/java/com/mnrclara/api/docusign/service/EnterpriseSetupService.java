package com.mnrclara.api.docusign.service;

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

import com.mnrclara.api.docusign.config.PropertiesConfig;
import com.mnrclara.api.docusign.exception.BadRequestException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EnterpriseSetupService {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	PropertiesConfig propertiesConfig;
	
	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}
	
	private String getEnterpriseSetupServiceApiUrl () {
		return "";//propertiesConfig.getEnterpriseSetupServiceUrl();
	}
	
	public boolean validateUser(String name, String password) {
		try {
			String authToken = commonService.generateOAuthToken();

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getEnterpriseSetupServiceApiUrl() + "login")
			        .queryParam("name", name)
			        .queryParam("password", password);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
