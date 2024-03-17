package com.tekclover.wms.api.transaction.service;

import com.tekclover.wms.api.transaction.config.PropertiesConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

@Slf4j
@Service
public class WrapperService {

    @Autowired
    PropertiesConfig propertiesConfig;

    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    private String getWrapperServiceApiUrl() {
        return propertiesConfig.getWrapperServiceUrl();
    }

    public void createErrorLog() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
//            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getWrapperServiceApiUrl() + "/errorLog/toDb");
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity,
                    String.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


}