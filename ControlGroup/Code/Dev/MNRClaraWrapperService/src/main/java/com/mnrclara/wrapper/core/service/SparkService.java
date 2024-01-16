package com.mnrclara.wrapper.core.service;


import com.mnrclara.wrapper.core.config.PropertiesConfig;
import com.mnrclara.wrapper.core.model.spark.*;
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
    public ControlGroupTypeV1[] findControlGroupType(FindControlGroupTypeV1 findControlGroup) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
           // headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "spark/controlgroup");
            HttpEntity<?> entity = new HttpEntity<>(findControlGroup, headers);
            ResponseEntity<ControlGroupTypeV1[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ControlGroupTypeV1[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

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
    public ControlGroupResponse totalLanguageId(FindResult findResult) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            // headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "spark/calculateTotalLanguageId");
            HttpEntity<?> entity = new HttpEntity<>(findResult, headers);
            ResponseEntity<ControlGroupResponse> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ControlGroupResponse.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //SEARCH
    public ControlGroupResponse totalCompanyId(FindResult findResult) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            // headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "spark/calculateTotalCompanyId");
            HttpEntity<?> entity = new HttpEntity<>(findResult, headers);
            ResponseEntity<ControlGroupResponse> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ControlGroupResponse.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    //SEARCH
    public BilledHoursPaidReport[] getBilledHoursPaid(BilledHoursPaid billedHoursPaid) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            // headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "spark/billedHoursPaid");
            HttpEntity<?> entity = new HttpEntity<>(billedHoursPaid, headers);
            ResponseEntity<BilledHoursPaidReport[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BilledHoursPaidReport[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //SEARCH
    public InvoiceHeader[] findInvoiceHeader(SearchInvoiceHeader searchInvoiceHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            // headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "spark/invoiceHeader");
            HttpEntity<?> entity = new HttpEntity<>(searchInvoiceHeader, headers);
            ResponseEntity<InvoiceHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, InvoiceHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //SEARCH
    public Inventory[] findInventory(FindInventory searchInvoiceHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            // headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "spark/inventory");
            HttpEntity<?> entity = new HttpEntity<>(searchInvoiceHeader, headers);
            ResponseEntity<Inventory[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Inventory[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET ALL
    public ImBasicData1[] getImBasicData1() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
//            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "spark");
            ResponseEntity<ImBasicData1[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ImBasicData1[].class);
//			log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
