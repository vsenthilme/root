package com.tekclover.wms.core.service;


import com.tekclover.wms.core.config.PropertiesConfig;
import com.tekclover.wms.core.model.spark.*;
import com.tekclover.wms.core.model.transaction.QualityHeader;
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

    public ImBasicData1[] findImBasicData1(FindImBasicData1 findImBasicData1) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
//            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "spark/imbasicData1");
            HttpEntity<?> entity = new HttpEntity<>(findImBasicData1,headers);
            ResponseEntity<ImBasicData1[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ImBasicData1[].class);
			log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public PutAwayHeader[] findPutAwayHeader(FindPutAwayHeader findPutAwayHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            // headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "spark/putaway");
            HttpEntity<?> entity = new HttpEntity<>(findPutAwayHeader, headers);
            ResponseEntity<PutAwayHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PutAwayHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public InboundHeader[] findInboundHeader(FindInboundHeader findInboundHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            // headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "spark/inboundHeader");
            HttpEntity<?> entity = new HttpEntity<>(findInboundHeader, headers);
            ResponseEntity<InboundHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, InboundHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public PickupHeader[] findPickupHeader(FindPickupHeader findPickupHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            // headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "spark/pickupHeader");
            HttpEntity<?> entity = new HttpEntity<>(findPickupHeader, headers);
            ResponseEntity<PickupHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PickupHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public QualityHeader[] findQualityHeader(FindQualityHeader findQualityHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            // headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "spark/qualityHeader");
            HttpEntity<?> entity = new HttpEntity<>(findQualityHeader, headers);
            ResponseEntity<QualityHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, QualityHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Find StagingHeader
    public StagingHeader[] findStagingHeader(FindStagingHeader findStagingHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            // headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "spark/stagingheader/find");
            HttpEntity<?> entity = new HttpEntity<>(findStagingHeader, headers);
            ResponseEntity<StagingHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StagingHeader[].class);
            log.info("result: " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    //Find GrHeader
    public GrHeader[] findGrHeader(FindGrHeader findGrHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            // headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "spark/grheader/find");
            HttpEntity<?> entity = new HttpEntity<>(findGrHeader, headers);
            ResponseEntity<GrHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, GrHeader[].class);
            log.info("result: " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    //Find PreOutboundHeader
    public PreOutboundHeader[] findPreOutboundHeader(FindPreOutboundHeader findPreOutboundHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            // headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "spark/preoutboundheader/find");
            HttpEntity<?> entity = new HttpEntity<>(findPreOutboundHeader, headers);
            ResponseEntity<PreOutboundHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PreOutboundHeader[].class);
            log.info("result: " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    //Find OrderManagementLine
    public OrderManagementLine[] findOrderManagementLine(FindOrderManagementLine findOrderManagementLine) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            // headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "spark/ordermanagementline/find");
            HttpEntity<?> entity = new HttpEntity<>(findOrderManagementLine, headers);
            ResponseEntity<OrderManagementLine[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, OrderManagementLine[].class);
            log.info("result: " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //findpreinboundheader
    public PreInboundHeader[] findPreInboundHeader(FindPreInboundHeader findPreInboundHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            // headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "spark/preinboundheader/find");
            HttpEntity<?> entity = new HttpEntity<>(findPreInboundHeader, headers);
            ResponseEntity<PreInboundHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PreInboundHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public OutBoundHeader[] findOutboundHeader(FindOutBoundHeader findOutBoundHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            // headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "spark/outboundheader/find");
            HttpEntity<?> entity = new HttpEntity<>(findOutBoundHeader, headers);
            ResponseEntity<OutBoundHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, OutBoundHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public OutBoundReversal[] findOutBoundReversal(FindOutBoundReversal findOutBoundReversal) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            // headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "spark/outboundreversal/find");
            HttpEntity<?> entity = new HttpEntity<>(findOutBoundReversal, headers);
            ResponseEntity<OutBoundReversal[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, OutBoundReversal[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //FindContainerReceipt

    public ContainerReceipt[] findContainerReceipt(FindContainerReceipt findContainerReceipt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            // headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "spark/containerReceipt/find");
            HttpEntity<?> entity = new HttpEntity<>(findContainerReceipt, headers);
            ResponseEntity<ContainerReceipt[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ContainerReceipt[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //Find OrderManagementLine
    public OutboundHeaderOutput[] findOutboundHeaderOutput(FindOutBoundHeader findOutBoundHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            // headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSparkServiceUrl() + "spark/outboundorderoutput/find");
            HttpEntity<?> entity = new HttpEntity<>(findOutBoundHeader, headers);
            ResponseEntity<OutboundHeaderOutput[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, OutboundHeaderOutput[].class);
            log.info("result: " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
