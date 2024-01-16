package com.almailem.ams.api.connector.service;

import com.almailem.ams.api.connector.config.PropertiesConfig;
import com.almailem.ams.api.connector.model.auth.AuthToken;
import com.almailem.ams.api.connector.model.picklist.PickListHeader;
import com.almailem.ams.api.connector.model.wms.SalesOrder;
import com.almailem.ams.api.connector.model.wms.WarehouseApiResponse;
import com.almailem.ams.api.connector.repository.PickListHeaderRepository;
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
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SalesOrderService {

    @Autowired
    PickListHeaderRepository pickListHeaderRepository;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    PropertiesConfig propertiesConfig;

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
     * Get All PickList Details
     *
     * @return
     */
    public List<PickListHeader> getAllSalesOrderV2Details() {
        List<PickListHeader> pickLists = pickListHeaderRepository.findAll();
        return pickLists;
    }

    /**
     *
     * @param pickListHeaderId
     * @param companyCode
     * @param branchCode
     * @param pickListNo
     * @param processedStatusId
     * @return
     */
    public PickListHeader updateProcessedInboundOrder(Long pickListHeaderId, String companyCode,
                                                      String branchCode, String pickListNo, Long processedStatusId) {
        PickListHeader dbInboundOrder =
                pickListHeaderRepository.getPickListHeader(pickListHeaderId);
//                        findTopByPickListHeaderIdAndCompanyCodeAndBranchCodeAndPickListNoOrderByOrderReceivedOnDesc(
//                pickListHeaderId, companyCode, branchCode, pickListNo);
        log.info("orderId : " + pickListNo);
        log.info("dbInboundOrder : " + dbInboundOrder);
        if (dbInboundOrder != null) {
//            dbInboundOrder.setProcessedStatusId(10L);
//            dbInboundOrder.setOrderProcessedOn(new Date());
//            PickListHeader inboundOrder = pickListHeaderRepository.save(dbInboundOrder);
            pickListHeaderRepository.updateProcessStatusId(dbInboundOrder.getPickListHeaderId(), processedStatusId);
        }
        return dbInboundOrder;
    }

    /**
     * @param salesOrder
     * @return
     */
    public WarehouseApiResponse postSalesOrder(SalesOrder salesOrder) {
        AuthToken authToken = authTokenService.getTransactionServiceAuthToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken.getAccess_token());
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "warehouse/outbound/salesorderv2");
        HttpEntity<?> entity = new HttpEntity<>(salesOrder, headers);
        ResponseEntity<WarehouseApiResponse> result =
                getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, WarehouseApiResponse.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }
}
