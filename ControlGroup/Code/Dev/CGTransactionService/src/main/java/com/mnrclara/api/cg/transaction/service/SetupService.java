package com.mnrclara.api.cg.transaction.service;

import com.mnrclara.api.cg.transaction.config.PropertiesConfig;
import com.mnrclara.api.cg.transaction.model.auth.AuthToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

@Slf4j
@Service
public class SetupService {

	@Autowired
	PropertiesConfig propertiesConfig;

	@Autowired
	AuthTokenService authTokenService;

	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	private String getCgSetupServiceApiUrl() {
		return propertiesConfig.getCgSetupServiceUrl();
	}

	// GET
	public String getNextNumberRange (Long numberRangeCode,String numberRangeObject,String languageId, String companyId) {
		try {
			HttpHeaders headers = new HttpHeaders();
			AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
			String authToken = authTokenForSetupService.getAccess_token();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "/numberRange/nextNumberRange")
					.queryParam("numberRangeCode", numberRangeCode)
					.queryParam("numberRangeObject",numberRangeObject)
					.queryParam("languageId",languageId)
					.queryParam("companyId",companyId)
					.queryParam("authToken", authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
