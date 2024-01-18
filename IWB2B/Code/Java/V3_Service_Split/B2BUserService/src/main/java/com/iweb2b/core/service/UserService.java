package com.iweb2b.core.service;

import java.util.Collections;

import javax.validation.Valid;

import com.iweb2b.core.model.integration.*;
import com.iweb2b.core.model.integration.asyad.Consignment;
import com.iweb2b.core.model.integration.asyad.JNTPrintLabelResponse;
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

import com.iweb2b.core.config.PropertiesConfig;
import com.iweb2b.core.exception.BadRequestException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

	@Autowired
	PropertiesConfig propertiesConfig;
	
	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	private String getB2bintegrationServiceUrl() {
		return propertiesConfig.getB2bintegrationServiceUrl();
	}
	
	private String getB2BPortalServiceUrl() {
		return propertiesConfig.getB2bportalServiceUrl();
	}

	/*--------------------------------------------UserManagement--------------------------------------*/

	// GET - /login/validate
//	public UserAccess validateUserID(String userID, String password, String authToken) {
//		try {
//			HttpHeaders headers = new HttpHeaders();
//			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//			headers.add("User-Agent", "b2b RestTemplate");
//			headers.add("Authorization", "Bearer " + authToken);
//
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() + "useraccess/login")
//					.queryParam("userID", userID)
//					.queryParam("password", password);
//
//			HttpEntity<?> entity = new HttpEntity<>(headers);
//			ResponseEntity<UserAccess> result =
//					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UserAccess.class);
//			return result.getBody();
//		} catch (Exception e) {
//			throw new BadRequestException(e.getLocalizedMessage());
//		}
//	}
	
	// NEW PORTAL URL
	public UserAccess validateUserID(String userID, String password, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "b2b RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getB2BPortalServiceUrl() + "useraccess/login")
					.queryParam("userID", userID)
					.queryParam("password", password);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<UserAccess> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UserAccess.class);
			return result.getBody();
		} catch (Exception e) {
			throw new BadRequestException(e.getLocalizedMessage());
		}
	}

	// GET ALL
//	public UserAccess[] getUserAccesss(String authToken) {
//		try {
//			HttpHeaders headers = new HttpHeaders();
//			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//			headers.add("User-Agent", "b2b RestTemplate");
//			headers.add("Authorization", "Bearer " + authToken);
//
//			HttpEntity<?> entity = new HttpEntity<>(headers);
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() + "useraccess");
//			ResponseEntity<UserAccess[]> result =
//					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UserAccess[].class);
//			return result.getBody();
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//	}
	
	public UserAccess[] getUserAccesss(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "b2b RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getB2BPortalServiceUrl() + "useraccess");
			ResponseEntity<UserAccess[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UserAccess[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public UserAccess getUserAccess (String userId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getB2BPortalServiceUrl() + "useraccess/" + userId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<UserAccess> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UserAccess.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public UserAccess createUserAccess (@Valid AddUserAccess newUserAccess, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getB2BPortalServiceUrl() + "useraccess")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newUserAccess, headers);
			ResponseEntity<UserAccess> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, UserAccess.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			throw e;
		}
	}

	// PATCH
	public UserAccess updateUserAccess (String userId, String loginUserID,
										@Valid UpdateUserAccess updateUserAccess,
										String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "b2b RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(updateUserAccess, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getB2BPortalServiceUrl() + "useraccess/" + userId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<UserAccess> result =
					restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, UserAccess.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			throw e;
		}
	}

	// DELETE
	public boolean deleteUserAccess (String userId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "b2b RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getB2BPortalServiceUrl() + "useraccess/" + userId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result.getStatusCode());
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	public UserAccess[] findUserAccess(FindUserAccess findUserAccess, String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getB2BPortalServiceUrl() +"useraccess/findUserAccess");
			HttpEntity<?>entity = new HttpEntity<>(findUserAccess,headers);
			ResponseEntity<UserAccess[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, UserAccess[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------NEW-PORTAL-CODE-----------------------------------------------------------------------------
	public Consignment[] getHubCodeOrders(String hubCode, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder =
				UriComponentsBuilder.fromHttpUrl(getB2BPortalServiceUrl() + "consignment/" + hubCode);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<Consignment[]> result =
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Consignment[].class);
		return result.getBody();
	}

	// Find Consignment
	public Consignment[] findConsignment(FindConsignment findConsignment, String authToken) {

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder =
				UriComponentsBuilder.fromHttpUrl(getB2BPortalServiceUrl() + "consignment/findConsignment");
		HttpEntity<?> entity = new HttpEntity<>(findConsignment, headers);
		ResponseEntity<Consignment[]> result =
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Consignment[].class);
		return result.getBody();
	}

	// NEW PORTAL URL
	public DashboardCountOutput getDashboardCount(CountInput countInput, String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getB2BPortalServiceUrl() + "consignment/dashboard/getDashboardCount");
			HttpEntity<?>entity = new HttpEntity<>(countInput,headers);
			ResponseEntity<DashboardCountOutput> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, DashboardCountOutput.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// Print Label
	public JNTPrintLabelResponse printLabel(String billCode, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder =
				UriComponentsBuilder.fromHttpUrl(getB2BPortalServiceUrl() + "consignment/jnt/" + billCode + "/printLabel");
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<JNTPrintLabelResponse> result =
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, JNTPrintLabelResponse.class);
		return result.getBody();
	}

	// Print Label
	public byte[] pdfPrintLabel(String billCode, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder =
				UriComponentsBuilder.fromHttpUrl(getB2BPortalServiceUrl() + "consignment/jnt/" + billCode + "/pdf/printLabel");
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<byte[]> result =
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, byte[].class);
		return result.getBody();
	}

}