package com.almailem.ams.api.connector.service;

import com.almailem.ams.api.connector.config.PropertiesConfig;
import com.almailem.ams.api.connector.model.auth.AuthToken;
import com.almailem.ams.api.connector.model.supplierinvoice.*;
import com.almailem.ams.api.connector.model.wms.*;
import com.almailem.ams.api.connector.repository.SupplierInvoiceHeaderRepository;
import com.almailem.ams.api.connector.repository.specification.SupplierInvoiceHeaderSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.ParseException;
import java.util.*;

@Service
@Slf4j
public class SupplierInvoiceService {

    @Autowired
    private SupplierInvoiceHeaderRepository supplierInvoiceHeaderRepository;

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
    public List<SupplierInvoiceHeader> getAllSupplierInvoiceHeader() {
        List<SupplierInvoiceHeader> supplierInvoiceHeader = supplierInvoiceHeaderRepository.findAll();
        return supplierInvoiceHeader;
    }

    /**
     *
     * @param supplierInvoiceNo
     * @return
     */
    public SupplierInvoiceHeader updateProcessedInboundOrder(Long supplierInvoiceHeaderId, String companyCode, String branchCode, String supplierInvoiceNo,Long processedStatusId) {
        SupplierInvoiceHeader dbInboundOrder =
                supplierInvoiceHeaderRepository.getSupplierInvoiceHeader(supplierInvoiceHeaderId);
//                        findTopBySupplierInvoiceHeaderIdAndCompanyCodeAndBranchCodeAndSupplierInvoiceNoOrderByOrderReceivedOnDesc(
//                        supplierInvoiceHeaderId, companyCode, branchCode, supplierInvoiceNo);
        log.info("orderId : " + supplierInvoiceNo);
        log.info("dbInboundOrder : " + dbInboundOrder);
        if (dbInboundOrder != null) {
//            dbInboundOrder.setProcessedStatusId(10L);
//            dbInboundOrder.setOrderProcessedOn(new Date());
//            SupplierInvoiceHeader inboundOrder = supplierInvoiceHeaderRepository.save(dbInboundOrder);
//            return inboundOrder;
            supplierInvoiceHeaderRepository.updateProcessStatusId(dbInboundOrder.getSupplierInvoiceHeaderId(), processedStatusId);
        }
        return dbInboundOrder;
    }

    /**
     * @param ASN
     * @return
     */
    public WarehouseApiResponse postASNV2(ASN ASN) {
        AuthToken authToken = authTokenService.getTransactionServiceAuthToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken.getAccess_token());
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "warehouse/inbound/asn/v2");
        HttpEntity<?> entity = new HttpEntity<>(ASN, headers);
        ResponseEntity<WarehouseApiResponse> result =
                getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, WarehouseApiResponse.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }


    //Find
    public List<SupplierInvoiceHeader> findSupplierInvoiceHeader(SearchSupplierInvoiceHeader searchSupplierInvoiceHeader) throws ParseException {

        SupplierInvoiceHeaderSpecification spec = new SupplierInvoiceHeaderSpecification(searchSupplierInvoiceHeader);
        List<SupplierInvoiceHeader> results = supplierInvoiceHeaderRepository.findAll(spec);
        return results;

    }
}
