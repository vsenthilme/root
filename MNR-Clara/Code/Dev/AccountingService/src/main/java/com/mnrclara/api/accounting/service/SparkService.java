package com.mnrclara.api.accounting.service;

import java.util.Collections;
import java.util.Date;

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
import com.mnrclara.api.accounting.model.invoice.AddInvoiceHeader;
import com.mnrclara.api.accounting.model.invoice.SearchInvoiceHeader;
import com.mnrclara.api.accounting.model.invoice.spark.InvoiceHeader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SparkService {

	@Autowired
	PropertiesConfig propertiesConfig;
	
	/**
	 * 
	 * @return
	 */
	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	/**
	 * 
	 * @return
	 */
	private String getSparkServiceUrl() {
		return propertiesConfig.getSparkServiceUrl();
	}
	
	// GET
	public InvoiceHeader[] getInvoiceHeaders (SearchInvoiceHeader searchInvoiceHeader) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getSparkServiceUrl() + "/spark/invoiceHeader");
					
			HttpEntity<?> entity = new HttpEntity<>(searchInvoiceHeader, headers);
			ResponseEntity<InvoiceHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, InvoiceHeader[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
