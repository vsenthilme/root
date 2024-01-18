package com.tekclover.wms.core.service;


import com.tekclover.wms.core.config.PropertiesConfig;
import com.tekclover.wms.core.model.Connector.*;
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

    // Find SalesReturnHeader
    public SalesReturnHeader[] findSalesReturnHeader(FindSalesReturnHeader findSalesReturnHeader, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getConnectorServiceApiUrl() + "/salesReturn/findsalesreturnheader");
            HttpEntity<?> entity = new HttpEntity<>(findSalesReturnHeader, headers);
            ResponseEntity<SalesReturnHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, SalesReturnHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    // Find StockAdjustment
    public StockAdjustment[] findStockAdjustment(FindStockAdjustment findStockAdjustment, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getConnectorServiceApiUrl() + "/stockAdjustment/findStockAdjustment");
            HttpEntity<?> entity = new HttpEntity<>(findStockAdjustment, headers);
            ResponseEntity<StockAdjustment[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StockAdjustment[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Find StockAdjustment
    public PickListHeader[] findPickListHeader(FindPickListHeader findPickListHeader, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getConnectorServiceApiUrl() + "/salesorderv2/findPickListHeader");
            HttpEntity<?> entity = new HttpEntity<>(findPickListHeader, headers);
            ResponseEntity<PickListHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PickListHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Find StockAdjustment
    public PurchaseReturnHeader[] findPurchaseReturnHeader(FindPurchaseReturnHeader findPurchaseReturnHeader, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getConnectorServiceApiUrl() + "/returnpov2/findPurchaseReturnHeader");
            HttpEntity<?> entity = new HttpEntity<>(findPurchaseReturnHeader, headers);
            ResponseEntity<PurchaseReturnHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PurchaseReturnHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Find StockAdjustment
    public SalesInvoice[] findSalesInvoice(FindSalesInvoice findSalesInvoice, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getConnectorServiceApiUrl() + "/salesinvoice/findSalesInvoice");
            HttpEntity<?> entity = new HttpEntity<>(findSalesInvoice, headers);
            ResponseEntity<SalesInvoice[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, SalesInvoice[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
