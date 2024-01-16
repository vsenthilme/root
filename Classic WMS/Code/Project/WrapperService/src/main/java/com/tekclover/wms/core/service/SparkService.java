package com.tekclover.wms.core.service;


import com.tekclover.wms.core.config.PropertiesConfig;
import com.tekclover.wms.core.model.spark.FindInventoryMovement;
import com.tekclover.wms.core.model.spark.InventoryMovement;
import com.tekclover.wms.core.model.spark.InventoryMovementV2;
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

    private String getSparkServiceUrl() {
        return propertiesConfig.getSparkServiceUrl();
    }

    //SEARCH


    //SEARCH
    public InventoryMovement[] findInventoryMovement(FindInventoryMovement findInventoryMovement) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            // headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "spark/inventorymovement");
            HttpEntity<?> entity = new HttpEntity<>(findInventoryMovement, headers);
            ResponseEntity<InventoryMovement[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, InventoryMovement[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //SEARCH
    public InventoryMovementV2[] findInventoryMovementV2(FindInventoryMovement findInventoryMovement) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            // headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "spark/inventory/inventorymovement");
            HttpEntity<?> entity = new HttpEntity<>(findInventoryMovement, headers);
            ResponseEntity<InventoryMovementV2[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, InventoryMovementV2[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


}
