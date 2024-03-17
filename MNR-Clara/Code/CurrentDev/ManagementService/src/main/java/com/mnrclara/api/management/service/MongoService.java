package com.mnrclara.api.management.service;

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

import com.mnrclara.api.management.config.PropertiesConfig;
import com.mnrclara.api.management.model.matteritform.ITForm007;
import com.mnrclara.api.management.model.matteritform.ITForm008;
import com.mnrclara.api.management.model.matteritform.ITForm009;
import com.mnrclara.api.management.model.matteritform.ITForm010;
import com.mnrclara.api.management.model.matteritform.ITForm011;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MongoService {
	
	@Autowired
	PropertiesConfig propertiesConfig;

	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	private String getCrmServiceApiUrl() {
		return propertiesConfig.getCrmServiceUrl();
	}

	// POST
	public ITForm007 createITForm007(ITForm007 itform007, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCrmServiceApiUrl() + "/itform007")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(itform007, headers);
			ResponseEntity<ITForm007> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity,
					ITForm007.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public ITForm008 createITForm008(ITForm008 itform008, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCrmServiceApiUrl() + "/itform008")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(itform008, headers);
			ResponseEntity<ITForm008> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity,
					ITForm008.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public ITForm009 createITForm009(ITForm009 itform009, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCrmServiceApiUrl() + "/itform009")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(itform009, headers);
			ResponseEntity<ITForm009> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity,
					ITForm009.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public ITForm010 createITForm010(ITForm010 itform010, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCrmServiceApiUrl() + "/itform010")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(itform010, headers);
			ResponseEntity<ITForm010> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity,
					ITForm010.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public ITForm011 createITForm011(ITForm011 itform011, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCrmServiceApiUrl() + "/itform011")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(itform011, headers);
			ResponseEntity<ITForm011> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity,
					ITForm011.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
