package com.mnrclara.wrapper.core.service;

import java.util.Collections;

import javax.validation.Valid;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.mnrclara.wrapper.core.config.PropertiesConfig;
import com.mnrclara.wrapper.core.model.crm.Dashboard;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm001;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm002;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm003;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm004;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm005;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm006;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DashboardService {

	@Autowired
	PropertiesConfig propertiesConfig;

	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	private String getCRMServiceUrl() {
		return propertiesConfig.getCrmServiceUrl();
	}

	// GET - /inquiry
	public Dashboard getInquiryCount(String loginUserId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/dashboard/inquiry")
					.queryParam("loginUserId", loginUserId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Dashboard> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					Dashboard.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /pcIntakeForm
	public Dashboard getPCIntakeFormCount(String loginUserId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCRMServiceUrl() + "/dashboard/pcIntakeForm").queryParam("loginUserId", loginUserId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Dashboard> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					Dashboard.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /potentialClient
	public Dashboard getPotentialClientCount (String loginUserId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCRMServiceUrl() + "/dashboard/potentialClient").queryParam("loginUserId", loginUserId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Dashboard> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					Dashboard.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /agreement
	public Dashboard getAgreementCount(String loginUserId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCRMServiceUrl() + "/dashboard/agreement").queryParam("loginUserId", loginUserId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Dashboard> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					Dashboard.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /agreementTotal
	public Dashboard getAgreementTotalCount(String loginUserId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCRMServiceUrl() + "/dashboard/agreementTotal").queryParam("loginUserId", loginUserId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Dashboard> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					Dashboard.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /agreementSent
	public Dashboard getAgreementSentCount(String loginUserId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCRMServiceUrl() + "/dashboard/agreementSent").queryParam("loginUserId", loginUserId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Dashboard> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					Dashboard.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /agreementReceived
	public Dashboard getAgreementReceivedCount(String loginUserId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCRMServiceUrl() + "/dashboard/agreementReceived").queryParam("loginUserId", loginUserId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Dashboard> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					Dashboard.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /agreementResent
	public Dashboard getAgreementResentCount(String loginUserId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCRMServiceUrl() + "/dashboard/agreementResent").queryParam("loginUserId", loginUserId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Dashboard> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					Dashboard.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
