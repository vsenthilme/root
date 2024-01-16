package com.tekclover.wms.api.billing.service;

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

import com.tekclover.wms.api.billing.config.PropertiesConfig;
import com.tekclover.wms.api.billing.model.dto.Billing;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MastersService {
	
	@Autowired
	PropertiesConfig propertiesConfig;
	
	@Autowired
	AuthTokenService authTokenService;
	
	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}
	
	private String getMastersServiceUrl () {
		return propertiesConfig.getMastersServiceUrl();
	}
	
	/**
	 * 
	 * @param module
	 * @param partnerCode
	 * @param warehouseId
	 * @param authToken
	 */
	public Billing getBilling (Long module, String partnerCode, String warehouseId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getMastersServiceUrl() + "billing/" + partnerCode)
					.queryParam("module", module)
					.queryParam("warehouseId", warehouseId);
			ResponseEntity<Billing> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Billing.class);
			log.info("result : " + result);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}