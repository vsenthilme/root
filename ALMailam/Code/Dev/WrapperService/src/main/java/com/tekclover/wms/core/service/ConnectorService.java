package com.tekclover.wms.core.service;


import com.tekclover.wms.core.config.PropertiesConfig;
import com.tekclover.wms.core.model.Connector.SearchSupplierInvoiceHeader;
import com.tekclover.wms.core.model.Connector.SupplierInvoiceHeader;
import com.tekclover.wms.core.model.transaction.IntegrationLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

@Service
@Slf4j
public class ConnectorService {


    @Autowired
    PropertiesConfig propertiesConfig;


    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    private String getConnectorServiceApiUrl() {
        return propertiesConfig.getConnectorServiceUrl();
    }

    /*=================================================Integration Log=========================================================*/

    //Integration Log
    public IntegrationLog[] getAllIntegrationLog(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getConnectorServiceApiUrl() + "integrationlog");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<IntegrationLog[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, IntegrationLog[].class);
            log.info("result: " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Find SupplierInvoiceHeader
    public SupplierInvoiceHeader[] findSupplierInvoiceHeader(SearchSupplierInvoiceHeader searchSupplierInvoiceHeader, String authToken){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getConnectorServiceApiUrl() +"/supplierinvoice/findSupplierInvoiceHeader");
            HttpEntity<?>entity = new HttpEntity<>(searchSupplierInvoiceHeader,headers);
            ResponseEntity<SupplierInvoiceHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, SupplierInvoiceHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
