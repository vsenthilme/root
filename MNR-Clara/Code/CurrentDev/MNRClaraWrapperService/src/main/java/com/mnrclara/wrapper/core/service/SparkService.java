package com.mnrclara.wrapper.core.service;


import com.mnrclara.wrapper.core.config.PropertiesConfig;
import com.mnrclara.wrapper.core.model.spark.ClientGeneral;
import com.mnrclara.wrapper.core.model.spark.FindClientGeneral;
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

    private String getSparkServiceUrl () {
        return propertiesConfig.getSparkServiceUrl();
    }


    // Find ClientGeneral
    public ClientGeneral[] findClientGeneral(FindClientGeneral findClientGeneral) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", " RestTemplate");
//        headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "/wms/spark/clientGeneral");
            HttpEntity<?> entity = new HttpEntity<>(findClientGeneral, headers);
            ResponseEntity<ClientGeneral[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ClientGeneral[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
