package com.iweb2b.core.service;

import java.util.Collections;

import com.iweb2b.core.exception.BadRequestException;
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
import com.iweb2b.core.model.auth.AuthToken;
import com.iweb2b.core.model.integration.*;
import com.iweb2b.core.model.integration.asyad.Consignment;
import com.iweb2b.core.model.integration.asyad.ConsignmentResponse;
import com.iweb2b.core.model.integration.asyad.ConsignmentWebhook;
import com.iweb2b.core.model.integration.asyad.JNTPrintLabelResponse;
import com.iweb2b.core.model.integration.asyad.JNTResponse;
import com.iweb2b.core.model.integration.asyad.JNTWebhookEntity;
import com.iweb2b.core.model.integration.asyad.JNTWebhookRequest;
import com.iweb2b.core.model.integration.asyad.OrderStatusUpdate;
import com.iweb2b.core.model.integration.asyad.OrderStatusUpdateResponse;
import com.iweb2b.core.model.integration.asyad.QPOrderCreateResponse;
import com.iweb2b.core.model.integration.asyad.QPTrackingRequest;
import com.iweb2b.core.model.integration.asyad.QPTrackingResponse;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

@Slf4j
@Service
public class IntegrationService {

	@Autowired
	PropertiesConfig propertiesConfig;
	
	@Autowired
	AuthTokenService authTokenService;
	
	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	private String getB2bintegrationServiceUrl() {
		return propertiesConfig.getB2bintegrationServiceUrl();
	}

	//--------------------------------------------ConsignmentTracking------------------------------------------------------------------------
	// GET ALL
	public ConsignmentTracking[] getAllConsignmentTracking (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() + "consignmentTracking");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ConsignmentTracking[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ConsignmentTracking[].class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public ConsignmentTracking getConsignmentTracking (String consignmentTrackingId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() + "consignmentTracking/" + consignmentTrackingId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ConsignmentTracking> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ConsignmentTracking.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	

	public ConsignmentTracking getConsignmentTrackingByRefNumber(String referenceNumber) {
		try {
			AuthToken integAuthToken = authTokenService.getIntegrationServiceAuthToken();
			String authToken = integAuthToken.getAccess_token();
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() + "consignment/" + referenceNumber + "/shipment");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ConsignmentTracking> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ConsignmentTracking.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public JNTResponse postConsignmentWebhookPayload(ConsignmentWebhook consignmentWebhook) {
		AuthToken integAuthToken = authTokenService.getIntegrationServiceAuthToken();
		String authToken = integAuthToken.getAccess_token();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() + "consignment/webhook");

		HttpEntity<?> entity = new HttpEntity<>(consignmentWebhook, headers);
		ResponseEntity<JNTResponse> result =
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, JNTResponse.class);
		return result.getBody();
	}
		
	//--------------------------------------------SoftDataUpload------------------------------------------------------------------------
	// GET
	public byte[] getShippingLabel(String referenceNumber) {
		try {
			AuthToken integAuthToken = authTokenService.getIntegrationServiceAuthToken();
			String authToken = integAuthToken.getAccess_token();
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() + "consignment/" + referenceNumber + "/shippingLabel");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<byte[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, byte[].class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public Consignment[] getConsignments(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() + "softDataUpload");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Consignment[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Consignment[].class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public ConsignmentWebhook[] getConsignmentByType(String type, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() + "softDataUpload/type")
							.queryParam("type",type);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ConsignmentWebhook[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ConsignmentWebhook[].class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST - Asyad
	public ConsignmentResponse createConsignment (Consignment newConsignment, String loginUserID) {
		AuthToken integAuthToken = authTokenService.getIntegrationServiceAuthToken();
		String authToken = integAuthToken.getAccess_token();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() + "softDataUpload/order")
				.queryParam("loginUserID", loginUserID);

		HttpEntity<?> entity = new HttpEntity<>(newConsignment, headers);
		ResponseEntity<ConsignmentResponse> result =
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ConsignmentResponse.class);
		return result.getBody();
	}
	
	// POST
	public JNTResponse postJNTRequest(String referenceNumber) {
		AuthToken integAuthToken = authTokenService.getIntegrationServiceAuthToken();
		String authToken = integAuthToken.getAccess_token();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() + "consignment/jnt")
				.queryParam("referenceNumber", referenceNumber);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<JNTResponse> result =
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, JNTResponse.class);
		return result.getBody();
	}

	// Get All
	public Consignment[] getHubCodeOrders(String hubCode, String authToken) {

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder =
				UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() + "consignment/" + hubCode);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<Consignment[]> result =
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Consignment[].class);
		return result.getBody();
	}
	
	// Print Label
	public JNTPrintLabelResponse printLabel(String billCode, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder =
				UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() + "consignment/jnt/" + billCode + "/printLabel");
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
				UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() + "consignment/jnt/" + billCode + "/pdf/printLabel");
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<byte[]> result =
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, byte[].class);
		return result.getBody();
	}
	
	// PATCH
	public OrderStatusUpdateResponse updateOrderInShipsy(OrderStatusUpdate orderStatusUpdate, String event, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() 
				+ "consignment/jnt/" + orderStatusUpdate.getReference_number() + "/eventUpdate")
				.queryParam("event", event);
		HttpEntity<?> entity = new HttpEntity<>(orderStatusUpdate, headers);
		ResponseEntity<OrderStatusUpdateResponse> result =
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, OrderStatusUpdateResponse.class);
		return result.getBody();
	}
	
	//--------------------------------------------------------------------------------------------------------------

	// POST
	public JNTWebhookEntity postJNTWebhook(JNTWebhookRequest jntWebhookRequest) {
		AuthToken integAuthToken = authTokenService.getIntegrationServiceAuthToken();
		String authToken = integAuthToken.getAccess_token();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() + "consignment/jnt/webhook");

		HttpEntity<?> entity = new HttpEntity<>(jntWebhookRequest, headers);
		ResponseEntity<JNTWebhookEntity> result =
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, JNTWebhookEntity.class);
		log.info("JNTWebhookEntity : " + result.getBody());
		return result.getBody();
	}

	// POST
	public QPOrderCreateResponse postQPRequest (String referenceNumber) {
		AuthToken integAuthToken = authTokenService.getIntegrationServiceAuthToken();
		String authToken = integAuthToken.getAccess_token();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() + "consignment/qp")
				.queryParam("referenceNumber", referenceNumber);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<QPOrderCreateResponse> result =
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, QPOrderCreateResponse.class);
		return result.getBody();
	}

	// POST
	public QPTrackingResponse listenQPWebhook(QPTrackingRequest qpTrackingRequest) {
		AuthToken integAuthToken = authTokenService.getIntegrationServiceAuthToken();
		String authToken = integAuthToken.getAccess_token();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() + "consignment/qp/webhook");
		HttpEntity<?> entity = new HttpEntity<>(qpTrackingRequest, headers);
		ResponseEntity<QPTrackingResponse> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, QPTrackingResponse.class);
		log.info("QPTrackingResponse : " + result.getBody());
		return result.getBody();
	}

	/*--------------------------------------------UserManagement--------------------------------------*/

	// GET - /login/validate
	public UserAccess validateUserID(String userID, String password, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "b2b RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() + "useraccess/login")
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
	public UserAccess[] getUserAccesss(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "b2b RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() + "useraccess");
			ResponseEntity<UserAccess[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UserAccess[].class);
//			log.info("result : " + result.getStatusCode());
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
					UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() + "useraccess/" + userId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<UserAccess> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UserAccess.class);
//			log.info("result : " + result.getStatusCode());
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

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() + "useraccess")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newUserAccess, headers);
			ResponseEntity<UserAccess> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, UserAccess.class);
//			log.info("result : " + result.getStatusCode());
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

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() + "useraccess/" + userId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<UserAccess> result =
					restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, UserAccess.class);
//			log.info("result : " + result.getStatusCode());
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
					UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() + "useraccess/" + userId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
//			log.info("result : " + result);
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
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() +"useraccess/findUserAccess");
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

	public Long getBoutiqaatDashboardCount(CountInput countInput, String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() +"consignment/dashboard/getBoutiqaatCount");
			HttpEntity<?>entity = new HttpEntity<>(countInput,headers);
			ResponseEntity<Long> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Long.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public Long getJNTDashboardCount(CountInput countInput, String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() +"consignment/dashboard/getJNTcount");
			HttpEntity<?>entity = new HttpEntity<>(countInput,headers);
			ResponseEntity<Long> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Long.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public DashboardCountOutput getDashboardCount(CountInput countInput, String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getB2bintegrationServiceUrl() +"consignment/dashboard/getDashboardCount");
			HttpEntity<?>entity = new HttpEntity<>(countInput,headers);
			ResponseEntity<DashboardCountOutput> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, DashboardCountOutput.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
