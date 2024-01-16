package com.iwmvp.api.master.service;

import com.iwmvp.api.master.config.PropertiesConfig;


import com.iwmvp.api.master.model.customer.*;
import com.iwmvp.api.master.model.asyad.*;
import com.iwmvp.api.master.model.auth.AuthToken;
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

	private String getB2bintegrationServiceApiUrl() {
		return propertiesConfig.getB2bintegrationServiceUrl();
	}


	//CREATE
	public Customer createCustomer(Customer newCustomer, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceApiUrl() + "customer")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newCustomer, headers);
		ResponseEntity<Customer> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Customer.class);
		return result.getBody();
	}

	// POST - MVP Consignment
	public ConsignmentResponse createConsignment (AddMvpConsignment newConsignment, String loginUserID,String authToken) {
//		AuthToken integAuthToken = authTokenService.getSetupServiceAuthToken();
//		String authToken = integAuthToken.getAccess_token();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceApiUrl() + "softDataUpload/mvp")
				.queryParam("loginUserID", loginUserID);

		HttpEntity<?> entity = new HttpEntity<>(newConsignment, headers);
		ResponseEntity<ConsignmentResponse> result =
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ConsignmentResponse.class);
		return result.getBody();
	}

	}
