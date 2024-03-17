package com.iweb2b.core.service;

import com.iweb2b.core.config.PropertiesConfig;
import com.iweb2b.core.model.spark.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

@Service
@Slf4j
public class SparkService {

    @Autowired
    PropertiesConfig propertiesConfig;

    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    private String getSparkServiceUrl() { return propertiesConfig.getSparkServiceUrl();}


    public ConsignmentV2[] findConsignmentV2(FindConsignmentV2 findConsignmentV2) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
//            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(findConsignmentV2,headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "b2b/consignmentV2");
            ResponseEntity<ConsignmentV2[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ConsignmentV2[].class);
//			log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Jntwebhook[] findB2BJntwebhook(FindJntwebhook findJntwebhook) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
//            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(findJntwebhook,headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "b2b/jntwebhook");
            ResponseEntity<Jntwebhook[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Jntwebhook[].class);
//			log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public JntHubCode[] findB2BJntHubCode(FindJntHubCode findJntHubCode) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
//            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(findJntHubCode,headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "b2b/jnthubcode");
            ResponseEntity<JntHubCode[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, JntHubCode[].class);
//			log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public QPWebhook[] findB2BQPWebhook(FindQPWebhook findQPWebhook) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
//            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(findQPWebhook,headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "b2b/qpwebhook");
            ResponseEntity<QPWebhook[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, QPWebhook[].class);
//			log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}



