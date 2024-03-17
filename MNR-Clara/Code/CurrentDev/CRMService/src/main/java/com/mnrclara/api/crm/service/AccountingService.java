package com.mnrclara.api.crm.service;

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

import com.mnrclara.api.crm.config.PropertiesConfig;
import com.mnrclara.api.crm.model.dto.InvoiceHeader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountingService {
	
	@Autowired
	AuthTokenService authTokenService;
	
	@Autowired
	PropertiesConfig propertiesConfig;

	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	private String getAccountingServiceUrl() {
		return propertiesConfig.getAccountingServiceUrl();
	}
	
	// FIND - InvoiceHeader
	public InvoiceHeader[] findInvoiceHeaderRecords (String fullTextSearch, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			log.info("getAccountingServiceUrl() : " + getAccountingServiceUrl());
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/findRecords")
					.queryParam("fullTextSearch", fullTextSearch);
			ResponseEntity<InvoiceHeader[]> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, InvoiceHeader[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
