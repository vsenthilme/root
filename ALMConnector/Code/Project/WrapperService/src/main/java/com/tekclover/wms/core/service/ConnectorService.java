package com.tekclover.wms.core.service;


import com.tekclover.wms.core.config.PropertiesConfig;
import com.tekclover.wms.core.model.middleware.*;
import com.tekclover.wms.core.model.transaction.IntegrationLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

@Slf4j
@Service
public class ConnectorService {

    @Autowired
    PropertiesConfig propertiesConfig;


    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    private String getConnectorServiceApiUrl(){
        return propertiesConfig.getConnectorServiceUrl();
    }

    // GET ALL
    public SupplierInvoiceHeader[] getAllSupplierInvoiceHeader(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getConnectorServiceApiUrl() + "supplierinvoice");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<SupplierInvoiceHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, SupplierInvoiceHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Get All StockReceipts
    public StockReceiptHeader[] getAllStockReceipts(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getConnectorServiceApiUrl() + "stockreceipt");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<StockReceiptHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StockReceiptHeader[].class);
            log.info("result: " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Get All B2BTransferIn Details
    public TransferInHeader[] getAllB2BTransferInDetails(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getConnectorServiceApiUrl() + "b2btransferin");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<TransferInHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, TransferInHeader[].class);
            log.info("result: " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    //Get All InterWhTransferInV2 Details
    public TransferInHeader[] getAllInterWhTransferInV2Details(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getConnectorServiceApiUrl() + "interwarehousetransferoutv2");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<TransferInHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, TransferInHeader[].class);
            log.info("result: " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    public StockAdjustment[] getAllStockAdjustment(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getConnectorServiceApiUrl() + "stockAdjustment");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<StockAdjustment[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StockAdjustment[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //===============================================OUTBOUND============================================================
    //Get All SalesOrderV2 Details
    public PickListHeader[] getAllSalesOrderV2Details(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getConnectorServiceApiUrl() + "salesorderv2");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<PickListHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PickListHeader[].class);
            log.info("result: " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    //Get All ShipmentOrderV2 Details
    public TransferOutHeader[] getAllSoV2Details(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getConnectorServiceApiUrl() + "shipmentorderv2");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<TransferOutHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, TransferOutHeader[].class);
            log.info("result: " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    //Get All ReturnPOV2 Details
    public PurchaseReturnHeader[] getAllReturnPOV2Details(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getConnectorServiceApiUrl() + "returnpov2");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<PurchaseReturnHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PurchaseReturnHeader[].class);
            log.info("result: " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    //Get All InterWarehouseTransferOutV2 Details
    public TransferOutHeader[] getAllIWhTransferOutV2Details(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getConnectorServiceApiUrl() + "interwarehousetransferoutv2");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<TransferOutHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, TransferOutHeader[].class);
            log.info("result: " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    //Get All SalesInvoice Details
    public SalesInvoice[] getAllSalesInvoiceDetails(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getConnectorServiceApiUrl() + "salesinvoice");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<SalesInvoice[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, SalesInvoice[].class);
            log.info("result: " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    //Get All ItemMaster Details
    public ItemMaster[] getAllItemMasterDetails(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getConnectorServiceApiUrl() + "itemmaster");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<ItemMaster[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ItemMaster[].class);
            log.info("result: " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    //Get All CustomerMaster Details
    public CustomerMaster[] getAllCustomerMasterDetails(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getConnectorServiceApiUrl() + "customermaster");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<CustomerMaster[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, CustomerMaster[].class);
            log.info("result: " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    //===============================================StockCount============================================================
    //Get All Perpetual Details
    public PerpetualHeader[] getAllPerpetualDetails(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getConnectorServiceApiUrl() + "perpetual");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<PerpetualHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PerpetualHeader[].class);
            log.info("result: " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    //Get All Periodic Details
    public PeriodicHeader[] getAllPeriodicDetails(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getConnectorServiceApiUrl() + "periodic");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<PeriodicHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PeriodicHeader[].class);
            log.info("result: " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
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


}
