package com.almailem.ams.api.connector.service;

import com.almailem.ams.api.connector.config.PropertiesConfig;
import com.almailem.ams.api.connector.model.auth.AuthToken;
import com.almailem.ams.api.connector.model.transferout.TransferOutHeader;
import com.almailem.ams.api.connector.model.wms.InterWarehouseTransferOut;
import com.almailem.ams.api.connector.model.wms.WarehouseApiResponse;
import com.almailem.ams.api.connector.repository.TransferOutHeaderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class InterWarehouseTransferOutService {

    @Autowired
    TransferOutHeaderRepository transferOutHeaderRepository;

    @Autowired
    PropertiesConfig propertiesConfig;

    @Autowired
    AuthTokenService authTokenService;

    private String getTransactionServiceApiUrl() {
        return propertiesConfig.getTransactionServiceUrl();
    }

    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // Object Convertor
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter
                .setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
        return restTemplate;
    }

    /**
     * Get All InterWarehouseTransferOutV2 Details
     *
     * @return
     */
    public List<TransferOutHeader> getAllInterWhTransferOutV2Details() {
        List<TransferOutHeader> transferOuts = transferOutHeaderRepository.findAll();
        return transferOuts;
    }

//    /**
//     * @param transferOrderNumber
//     * @return
//     */
//    public TransferOutHeader updateProcessedOutboundOrder(String transferOrderNumber) {
//        TransferOutHeader dbObOrder = transferOutHeaderRepository.findTopByTransferOrderNumberOrderByOrderReceivedOnDesc(transferOrderNumber);
//        log.info("orderId: " + transferOrderNumber);
//        log.info("IWhTransfer Out Order: " + dbObOrder);
//        if (dbObOrder != null) {
//            dbObOrder.setProcessedStatusId(10L);
//            dbObOrder.setOrderProcessedOn(new Date());
////            transferOutHeaderRepository.save(dbObOrder);
//            transferOutHeaderRepository.updateProcessStatusId(transferOrderNumber, new Date());
//        }
//        return dbObOrder;
//    }

    public TransferOutHeader updateProcessedOutboundOrder(Long transferOutHeaderId, String sourceCompanyCode,
                                                          String sourceBranchCode, String transferOrderNumber, Long processedStatusId) {
//        TransferOutHeader dbInboundOrder =
//                transferOutHeaderRepository.findTopByTransferOutHeaderIdAndSourceCompanyCodeAndSourceBranchCodeAndTransferOrderNumberOrderByOrderReceivedOnDesc(
//                        transferOutHeaderId, sourceCompanyCode, sourceBranchCode, transferOrderNumber);
        TransferOutHeader dbInboundOrder = transferOutHeaderRepository.getTransferOutHeader(transferOutHeaderId);
        log.info("orderId : " + transferOrderNumber);
        log.info("dbInboundOrder : " + dbInboundOrder);
        if (dbInboundOrder != null) {
            transferOutHeaderRepository.updateProcessStatusId(dbInboundOrder.getTransferOutHeaderId(), processedStatusId);
        }
        return dbInboundOrder;
    }

//    public TransferOutHeader updatefailureProcessedOutboundOrder(String transferOrderNumber) {
//        TransferOutHeader dbObOrder = transferOutHeaderRepository.findTopByTransferOrderNumberOrderByOrderReceivedOnDesc(transferOrderNumber);
//        log.info("orderId: " + transferOrderNumber);
//        log.info("IWhTransfer Out Order: " + dbObOrder);
//        if (dbObOrder != null) {
////            dbObOrder.setProcessedStatusId(10L);
////            dbObOrder.setOrderProcessedOn(new Date());
////            transferOutHeaderRepository.save(dbObOrder);
//            transferOutHeaderRepository.updatefailureProcessStatusId(transferOrderNumber);
//        }
//        return dbObOrder;
//    }

    public WarehouseApiResponse postIWhTransferOutV2(InterWarehouseTransferOut iWhTransOutV2) {
        AuthToken authToken = authTokenService.getTransactionServiceAuthToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken.getAccess_token());
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "warehouse/outbound/interwarehousetransferoutv2");
        HttpEntity<?> entity = new HttpEntity<>(iWhTransOutV2, headers);
        ResponseEntity<WarehouseApiResponse> result =
                getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, WarehouseApiResponse.class);
        log.info("result: " + result.getStatusCode());
        return result.getBody();
    }
}
