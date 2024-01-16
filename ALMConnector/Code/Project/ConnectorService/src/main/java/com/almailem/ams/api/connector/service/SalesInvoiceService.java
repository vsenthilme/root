package com.almailem.ams.api.connector.service;

import com.almailem.ams.api.connector.config.PropertiesConfig;
import com.almailem.ams.api.connector.model.auth.AuthToken;
import com.almailem.ams.api.connector.model.salesinvoice.SalesInvoice;
import com.almailem.ams.api.connector.model.wms.WarehouseApiResponse;
import com.almailem.ams.api.connector.repository.SalesInvoiceRepository;
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
public class SalesInvoiceService {

    @Autowired
    private SalesInvoiceRepository salesInvoiceRepository;

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
     * GET ALL
     */
    public List<SalesInvoice> getAllSupplierInvoiceHeader() {
        List<SalesInvoice> salesInvoiceList = salesInvoiceRepository.findAll();
        return salesInvoiceList;
    }

    /**
     *
     * @param salesInvoiceId
     * @param companyCode
     * @param branchCode
     * @param salesInvoiceNumber
     * @param processedStatusId
     * @return
     */
    public SalesInvoice updateProcessedOutboundOrder(Long salesInvoiceId, String companyCode, String branchCode,
                                                     String salesInvoiceNumber, Long processedStatusId) {
        SalesInvoice dbOutboundOrder =
                salesInvoiceRepository.getSalesInvoice(salesInvoiceId);
//                        findTopBySalesInvoiceIdAndCompanyCodeAndBranchCodeAndSalesInvoiceNumberOrderByOrderReceivedOnDesc(
//                salesInvoiceId, companyCode, branchCode, salesInvoiceNumber);
        log.info("orderId : " + salesInvoiceNumber);
        log.info("dbOutboundOrder : " + dbOutboundOrder);
        if (dbOutboundOrder != null) {
//            dbOutboundOrder.setProcessedStatusId(10L);
//            dbOutboundOrder.setOrderProcessedOn(new Date());
//            SalesInvoice OutboundOrder = salesInvoiceRepository.save(dbOutboundOrder);
            salesInvoiceRepository.updateProcessStatusId(dbOutboundOrder.getSalesInvoiceId(), processedStatusId);
        }
        return dbOutboundOrder;
    }

    /**
     * @param salesInvoice
     * @return
     */
    public WarehouseApiResponse postSalesInvoice(com.almailem.ams.api.connector.model.wms.SalesInvoice salesInvoice) {
        AuthToken authToken = authTokenService.getTransactionServiceAuthToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken.getAccess_token());
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "warehouse/outbound/salesinvoice");
        HttpEntity<?> entity = new HttpEntity<>(salesInvoice, headers);
        ResponseEntity<WarehouseApiResponse> result =
                getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, WarehouseApiResponse.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }
}
