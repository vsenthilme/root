package com.tekclover.wms.core.service;

import com.tekclover.wms.core.config.PropertiesConfig;
import com.tekclover.wms.core.model.spark.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

@Slf4j
@Service
public class SparkService {

    @Autowired
    PropertiesConfig propertiesConfig;

    private String getSparkServiceApiUrl() {
        return propertiesConfig.getSparkServiceUrl();
    }


    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    public StorageBin[] findStorageBin(FindStorageBin findStorageBin){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
         //   headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() +"wms/spark/storagebin/findStorageBin");
            HttpEntity<?> entity = new HttpEntity<>(findStorageBin,headers);
            ResponseEntity<StorageBin[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StorageBin[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    public StagingLine[] findStagingLine(FindStagingLine findStagingLine){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
       //     headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() +"wms/spark/stagingline");
            HttpEntity<?> entity = new HttpEntity<>(findStagingLine,headers);
            ResponseEntity<StagingLine[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StagingLine[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

            public StagingHeader[] findStagingHeader(FindStagingHeaderV2 findStagingHeaderV2){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
      //      headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() +"wms/spark/qualityheader");
            HttpEntity<?> entity = new HttpEntity<>(findStagingHeaderV2,headers);
            ResponseEntity<StagingHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StagingHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

        public QualityHeader[] findQualityHeader(FindQualityHeader findQualityHeader){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
     //       headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() +"wms/spark/qualityheader");
            HttpEntity<?> entity = new HttpEntity<>(findQualityHeader,headers);
            ResponseEntity<QualityHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, QualityHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

            public QualityLine[] findQualityLine(FindQualityLine findQualityLine){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
     //       headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() +"wms/spark/qualityline");
            HttpEntity<?> entity = new HttpEntity<>(findQualityLine,headers);
            ResponseEntity<QualityLine[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, QualityLine[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public PutAwayLine[] findPutAwayLine(FindPutAwayLine findPutAwayLine){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
    //        headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() +"wms/spark/putawayline");
            HttpEntity<?> entity = new HttpEntity<>(findPutAwayLine,headers);
            ResponseEntity<PutAwayLine[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PutAwayLine[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public PutAwayHeader[] findPutAwayHeader(FindPutAwayHeader findPutAwayHeader){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
  //          headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() +"wms/spark/putawayheader");
            HttpEntity<?> entity = new HttpEntity<>(findPutAwayHeader,headers);
            ResponseEntity<PutAwayHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PutAwayHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public ContainerReceipt[] findContainerReceipt(SearchContainerReceipt searchContainerReceipt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", " RestTemplate");
//        headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() + "wms/spark/findContainerReceipt");
            HttpEntity<?> entity = new HttpEntity<>(searchContainerReceipt, headers);
            ResponseEntity<ContainerReceipt[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ContainerReceipt[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public GrHeader[] findGrHeader(SearchGrHeader searchGrHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", " RestTemplate");
//        headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() + "wms/spark/findGrHeader");
            HttpEntity<?> entity = new HttpEntity<>(searchGrHeader, headers);
            ResponseEntity<GrHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, GrHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public GrLine[] findGrLine(SearchGrLine searchGrLine) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", " RestTemplate");
//        headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() + "wms/spark/findGrLine");
            HttpEntity<?> entity = new HttpEntity<>(searchGrLine, headers);
            ResponseEntity<GrLine[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, GrLine[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public InboundHeader[] findInboundHeader(SearchInboundHeader searchInboundHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", " RestTemplate");
//        headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() + "wms/spark/findInboundHeader");
            HttpEntity<?> entity = new HttpEntity<>(searchInboundHeader, headers);
            ResponseEntity<InboundHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, InboundHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public InboundLine[] findInboundLine(SearchInboundLine searchInboundLine) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", " RestTemplate");
//        headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() + "wms/spark/findInboundLine");
            HttpEntity<?> entity = new HttpEntity<>(searchInboundLine, headers);
            ResponseEntity<InboundLine[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, InboundLine[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public InhouseTransferHeader[] findInhouseTransferHeader(SearchInhouseTransferHeader searchInhouseTransferHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", " RestTemplate");
//        headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() + "wms/spark/findInhouseTransferHeader");
            HttpEntity<?> entity = new HttpEntity<>(searchInhouseTransferHeader, headers);
            ResponseEntity<InhouseTransferHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, InhouseTransferHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public InhouseTransferLine[] findInhouseTransferLine(SearchInhouseTransferLine searchInhouseTransferLine) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", " RestTemplate");
//        headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() + "wms/spark/findInhouseTransferLine");
            HttpEntity<?> entity = new HttpEntity<>(searchInhouseTransferLine, headers);
            ResponseEntity<InhouseTransferLine[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, InhouseTransferLine[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public InventoryMovement[] findInventoryMovement(SearchInventoryMovement searchInventoryMovement) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", " RestTemplate");
//        headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() + "wms/spark/findInventoryMovement");
            HttpEntity<?> entity = new HttpEntity<>(searchInventoryMovement, headers);
            ResponseEntity<InventoryMovement[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, InventoryMovement[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Inventory[] findInventory(SearchInventory searchInventory) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", " RestTemplate");
//        headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() + "wms/spark/findInventory");
            HttpEntity<?> entity = new HttpEntity<>(searchInventory, headers);
            ResponseEntity<Inventory[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Inventory[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public OrderManagementLine[] findOrderManagementLine(SearchOrderManagementLine searchOrderManagementLine) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", " RestTemplate");
//        headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() + "wms/spark/findOrderManagementLine");
            HttpEntity<?> entity = new HttpEntity<>(searchOrderManagementLine, headers);
            ResponseEntity<OrderManagementLine[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, OrderManagementLine[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public OutboundHeader[] findOutboundHeader(SearchOutboundHeader searchOutboundHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", " RestTemplate");
//        headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() + "wms/spark/findOutboundHeader");
            HttpEntity<?> entity = new HttpEntity<>(searchOutboundHeader, headers);
            ResponseEntity<OutboundHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, OutboundHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public OutboundReversal[] findOutboundReversal(SearchOutboundReversal searchOutboundReversal) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", " RestTemplate");
//        headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() + "wms/spark/findOutboundReversal");
            HttpEntity<?> entity = new HttpEntity<>(searchOutboundReversal, headers);
            ResponseEntity<OutboundReversal[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, OutboundReversal[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public PeriodicHeader[] findPeriodicHeader(SearchPeriodicHeader searchPeriodicHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", " RestTemplate");
//        headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() + "wms/spark/findPeriodicHeader");
            HttpEntity<?> entity = new HttpEntity<>(searchPeriodicHeader, headers);
            ResponseEntity<PeriodicHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PeriodicHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public PerpetualHeader[] findPerpetualHeader(SearchPerpetualHeader searchPerpetualHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", " RestTemplate");
//        headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() + "wms/spark/findPerpetualHeader");
            HttpEntity<?> entity = new HttpEntity<>(searchPerpetualHeader, headers);
            ResponseEntity<PerpetualHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PerpetualHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public PerpetualLine[] findPerpetualLine(SearchPerpetualLine searchPerpetualLine) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", " RestTemplate");
//        headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() + "wms/spark/findPerpetualLine");
            HttpEntity<?> entity = new HttpEntity<>(searchPerpetualLine, headers);
            ResponseEntity<PerpetualLine[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PerpetualLine[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public PickupHeader[] findPickupHeader(SearchPickupHeader searchPickupHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", " RestTemplate");
//        headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() + "wms/spark/findPickupHeader");
            HttpEntity<?> entity = new HttpEntity<>(searchPickupHeader, headers);
            ResponseEntity<PickupHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PickupHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public PickupLine[] findPickupLine(SearchPickupLine searchPickupLine) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", " RestTemplate");
//        headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() + "wms/spark/findPickupLine");
            HttpEntity<?> entity = new HttpEntity<>(searchPickupLine, headers);
            ResponseEntity<PickupLine[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PickupLine[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public PreInboundHeader[] findPreInboundHeader(SearchPreInboundHeader searchPreInboundHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", " RestTemplate");
//        headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() + "wms/spark/findPreInboundHeader");
            HttpEntity<?> entity = new HttpEntity<>(searchPreInboundHeader, headers);
            ResponseEntity<PreInboundHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PreInboundHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public PreOutboundHeader[] findPreOutboundHeader(SearchPreOutboundHeader searchPreOutboundHeader){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", " RestTemplate");
//        headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getSparkServiceApiUrl() + "wms/spark/findPreOutboundHeader");
            HttpEntity<?> entity = new HttpEntity<>(searchPreOutboundHeader, headers);
            ResponseEntity<PreOutboundHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PreOutboundHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }
}
