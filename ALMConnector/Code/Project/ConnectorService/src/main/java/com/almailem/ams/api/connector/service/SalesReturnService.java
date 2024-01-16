package com.almailem.ams.api.connector.service;


import com.almailem.ams.api.connector.config.PropertiesConfig;
import com.almailem.ams.api.connector.model.auth.AuthToken;
import com.almailem.ams.api.connector.model.salesreturn.SalesReturnHeader;
import com.almailem.ams.api.connector.model.wms.SaleOrderReturn;
import com.almailem.ams.api.connector.model.wms.WarehouseApiResponse;
import com.almailem.ams.api.connector.repository.SalesReturnHeaderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
@Slf4j
public class SalesReturnService {

    @Autowired
    private SalesReturnHeaderRepository salesReturnHeaderRepository;

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

    //GET ALL

    public List<SalesReturnHeader>  getAllSalesReturnHeader(){
        List<SalesReturnHeader> salesReturnHeader = salesReturnHeaderRepository.findAll();
        return salesReturnHeader;
    }

    public SalesReturnHeader updateProcessedInboundOrder(Long salesReturnHeaderId, String companyCode,
                                                         String branchCode, String returnOrderNo, Long processedStatusId) {
        SalesReturnHeader dbInboundOrder =
                salesReturnHeaderRepository.getSalesReturnHeader(salesReturnHeaderId);
//                        findTopBySalesReturnHeaderIdAndCompanyCodeAndBranchCodeAndReturnOrderNoOrderByOrderReceivedOnDesc(
//                salesReturnHeaderId, companyCode, branchCode, returnOrderNo);
        log.info("orderId : " + returnOrderNo);
        log.info("dbInboundOrder : " + dbInboundOrder);
        if (dbInboundOrder != null) {
//            dbInboundOrder.setProcessedStatusId(10L);
//            dbInboundOrder.setOrderProcessedOn(new Date());
//            SalesReturnHeader inboundOrder = salesReturnHeaderRepository.save(dbInboundOrder);
//            return inboundOrder;
            salesReturnHeaderRepository.updateProcessStatusId(dbInboundOrder.getSalesReturnHeaderId(), processedStatusId);
        }
        return dbInboundOrder;
    }

    /**
     *
     * @param salesReturn
     * @return
     */
    public WarehouseApiResponse postSaleOrderReturn(SaleOrderReturn salesReturn) {
        AuthToken authToken = authTokenService.getTransactionServiceAuthToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken.getAccess_token());
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "warehouse/inbound/soreturn/v2");
        HttpEntity<?> entity = new HttpEntity<>(salesReturn, headers);
        ResponseEntity<WarehouseApiResponse> result =
                getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, WarehouseApiResponse.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

}
