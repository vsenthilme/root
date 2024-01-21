package com.almailem.ams.api.connector.service;

import com.almailem.ams.api.connector.config.PropertiesConfig;
import com.almailem.ams.api.connector.model.auth.AuthToken;
import com.almailem.ams.api.connector.model.purchasereturn.FindPurchaseReturnHeader;
import com.almailem.ams.api.connector.model.purchasereturn.PurchaseReturnHeader;
import com.almailem.ams.api.connector.model.salesreturn.SalesReturnHeader;
import com.almailem.ams.api.connector.model.wms.ReturnPO;
import com.almailem.ams.api.connector.model.wms.WarehouseApiResponse;
import com.almailem.ams.api.connector.repository.PurchaseReturnHeaderRepository;
import com.almailem.ams.api.connector.repository.specification.PurchaseReturnHeaderSpecification;
import com.almailem.ams.api.connector.util.DateUtils;
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

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ReturnPOService {

    @Autowired
    PurchaseReturnHeaderRepository purchaseReturnHeaderRepository;

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
     * Get All ReturnPOV2 Details
     *
     * @return
     */
    public List<PurchaseReturnHeader> getAllReturnPoV2Details() {
        List<PurchaseReturnHeader> purchaseReturns = purchaseReturnHeaderRepository.findAll();
        return purchaseReturns;
    }

//    /**
//     * @param returnOrderNo
//     * @return
//     */
//    public void updateProcessedOutboundOrder(String returnOrderNo) {
//        PurchaseReturnHeader dbObOrder = purchaseReturnHeaderRepository.findTopByReturnOrderNoOrderByOrderReceivedOnDesc(returnOrderNo);
//        log.info("orderId: " + returnOrderNo);
//        log.info("dbOutboundOrder: " + dbObOrder);
//        if (dbObOrder != null) {
//            dbObOrder.setProcessedStatusId(10L);
//            dbObOrder.setOrderProcessedOn(new Date());
////            PurchaseReturnHeader obOrder = purchaseReturnHeaderRepository.save(dbObOrder);
//            purchaseReturnHeaderRepository.updateProcessStatusId(returnOrderNo,new Date());
////            return null;
//        }
////        return dbObOrder;
//    }

    public PurchaseReturnHeader updateProcessedOutboundOrder(Long purchaseReturnHeaderId, String companyCode,
                                                             String branchCode, String returnOrderNo, Long processedStatusId) {
        PurchaseReturnHeader dbInboundOrder =
                purchaseReturnHeaderRepository.getPurchaseReturnHeader(purchaseReturnHeaderId);
//                        findTopByPurchaseReturnHeaderIdAndCompanyCodeAndBranchCodeAndReturnOrderNoOrderByOrderReceivedOnDesc(
//                        purchaseReturnHeaderId, companyCode, branchCode, returnOrderNo);
        log.info("orderId : " + returnOrderNo);
        log.info("dbInboundOrder : " + dbInboundOrder);
        if (dbInboundOrder != null) {
            purchaseReturnHeaderRepository.updateProcessStatusId(dbInboundOrder.getPurchaseReturnHeaderId(), processedStatusId);
        }
        return dbInboundOrder;
    }

    public WarehouseApiResponse postReturnPOV2(ReturnPO returnPO) {
        AuthToken authToken = authTokenService.getTransactionServiceAuthToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken.getAccess_token());
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "warehouse/outbound/returnpov2");
        HttpEntity<?> entity = new HttpEntity<>(returnPO, headers);
        ResponseEntity<WarehouseApiResponse> result =
                getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, WarehouseApiResponse.class);
        log.info("result: " + result.getStatusCode());
        return result.getBody();
    }

    // Find PurchaseReturnHeader
    public List<PurchaseReturnHeader> findPurchaseReturnHeader(FindPurchaseReturnHeader findPurchaseReturnHeader) throws ParseException {

        if (findPurchaseReturnHeader.getFromOrderProcessedOn() != null && findPurchaseReturnHeader.getToOrderProcessedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(findPurchaseReturnHeader.getFromOrderProcessedOn(), findPurchaseReturnHeader.getToOrderProcessedOn());
            findPurchaseReturnHeader.setFromOrderProcessedOn(dates[0]);
            findPurchaseReturnHeader.setToOrderProcessedOn(dates[1]);
        }
        if (findPurchaseReturnHeader.getFromOrderReceivedOn() != null && findPurchaseReturnHeader.getToOrderReceivedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(findPurchaseReturnHeader.getFromOrderReceivedOn(), findPurchaseReturnHeader.getToOrderReceivedOn());
            findPurchaseReturnHeader.setFromOrderReceivedOn(dates[0]);
            findPurchaseReturnHeader.setToOrderReceivedOn(dates[1]);
        }

        PurchaseReturnHeaderSpecification spec = new PurchaseReturnHeaderSpecification(findPurchaseReturnHeader);
        List<PurchaseReturnHeader> results = purchaseReturnHeaderRepository.findAll(spec);
        return results;
    }

}
