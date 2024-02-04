package com.almailem.ams.api.connector.service;

import com.almailem.ams.api.connector.config.PropertiesConfig;
import com.almailem.ams.api.connector.model.auth.AuthToken;
import com.almailem.ams.api.connector.model.transferin.SearchTransferInHeader;
import com.almailem.ams.api.connector.model.transferin.SearchTransferInLine;
import com.almailem.ams.api.connector.model.transferin.TransferInHeader;
import com.almailem.ams.api.connector.model.transferin.TransferInLine;
import com.almailem.ams.api.connector.model.wms.InterWarehouseTransferIn;
import com.almailem.ams.api.connector.model.wms.WarehouseApiResponse;
import com.almailem.ams.api.connector.repository.TransferInHeaderRepository;
import com.almailem.ams.api.connector.repository.TransferInLineRepository;
import com.almailem.ams.api.connector.repository.specification.TransferInHeaderSpecification;
import com.almailem.ams.api.connector.repository.specification.TransferInLineSpecification;
import com.almailem.ams.api.connector.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

//import java.sql.Date;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class InterWarehouseTransferInService {

    @Autowired
    TransferInHeaderRepository transferInHeaderRepository;

    @Autowired
    TransferInLineRepository transferInLineRepository;

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
     * Get All InterWhTransferInV2 Details
     *
     * @return
     */
    public List<TransferInHeader> getAllInterWhTransferInV2Details() {
        List<TransferInHeader> transferIns = transferInHeaderRepository.findAll();
        return transferIns;
    }

//    public void updateProcessedInboundOrder(String asnNumber) {
//        TransferInHeader dbInboundOrder = transferInHeaderRepository.findTopByTransferOrderNoOrderByOrderReceivedOnDesc(asnNumber);
//        log.info("orderId : " + asnNumber);
//        log.info("dbInboundOrder : " + dbInboundOrder);
//        if (dbInboundOrder != null) {
//            dbInboundOrder.setProcessedStatusId(10L);
//            dbInboundOrder.setOrderProcessedOn(new Date());
////            TransferInHeader inboundOrder = transferInHeaderRepository.save(dbInboundOrder);
//            transferInHeaderRepository.updateProcessStatusId(asnNumber, new Date());
////            return null;
//        }
////        return dbInboundOrder;
//    }

    public TransferInHeader updateProcessedInboundOrder(Long transferInHeaderId, String companyCode, String branchCode, String transferOrderNo, Long processedStatusId){
//        TransferInHeader dbInboundOrder =
//                transferInHeaderRepository.findTopByTransferInHeaderIdAndSourceCompanyCodeAndSourceBranchCodeAndTransferOrderNoAndProcessedStatusIdOrderByOrderReceivedOn(
//                        transferInHeaderId, companyCode, branchCode, transferOrderNo, 0L);
        TransferInHeader dbInboundOrder = transferInHeaderRepository.getTransferInHeader(transferInHeaderId);
        log.info("orderId : " + transferOrderNo);
        log.info("dbInboundOrder : " + dbInboundOrder);
        if (dbInboundOrder != null) {
//            dbInboundOrder.setProcessedStatusId(10L);
//            dbInboundOrder.setOrderProcessedOn(new Date());
//            SupplierInvoiceHeader inboundOrder = supplierInvoiceHeaderRepository.save(dbInboundOrder);
//            return inboundOrder;
            transferInHeaderRepository.updateProcessStatusId(dbInboundOrder.getTransferInHeaderId(), processedStatusId);
        }
        return dbInboundOrder;
    }


    /**
     *
     * @param interWarehouseTransferIn
     * @return
     */
    public WarehouseApiResponse postIWTTransferIn(InterWarehouseTransferIn interWarehouseTransferIn) {
        AuthToken authToken = authTokenService.getTransactionServiceAuthToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken.getAccess_token());
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "warehouse/inbound/interWarehouseTransferIn/v2");
        HttpEntity<?> entity = new HttpEntity<>(interWarehouseTransferIn, headers);
        ResponseEntity<WarehouseApiResponse> result =
                getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, WarehouseApiResponse.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    public List<TransferInHeader> findInterWareHouseTransferInHeader(SearchTransferInHeader searchTransferInHeader) throws ParseException {

        if (searchTransferInHeader.getFromTransferOrderDate() != null && searchTransferInHeader.getFromTransferOrderDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchTransferInHeader.getFromTransferOrderDate(), searchTransferInHeader.getToTransferOrderDate());
            searchTransferInHeader.setFromTransferOrderDate(dates[0]);
            searchTransferInHeader.setToTransferOrderDate(dates[1]);
        }
        if (searchTransferInHeader.getFromOrderProcessedOn() != null && searchTransferInHeader.getToOrderProcessedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchTransferInHeader.getFromOrderProcessedOn(), searchTransferInHeader.getToOrderProcessedOn());
            searchTransferInHeader.setFromOrderProcessedOn(dates[0]);
            searchTransferInHeader.setToOrderProcessedOn(dates[1]);
        }
        if (searchTransferInHeader.getFromOrderReceivedOn() != null && searchTransferInHeader.getToOrderReceivedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchTransferInHeader.getFromOrderReceivedOn(), searchTransferInHeader.getToOrderReceivedOn());
            searchTransferInHeader.setFromOrderReceivedOn(dates[0]);
            searchTransferInHeader.setToOrderReceivedOn(dates[1]);
        }


        TransferInHeaderSpecification spec = new TransferInHeaderSpecification(searchTransferInHeader);
        List<TransferInHeader> results = transferInHeaderRepository.findAll(spec);
        return results;

    }


    public List<TransferInLine> findInterWareHouseTransferInLine(SearchTransferInLine searchTransferInLine) throws ParseException {


        TransferInLineSpecification spec = new TransferInLineSpecification(searchTransferInLine);
        List<TransferInLine> results = transferInLineRepository.findAll(spec);
        return results;

    }


}
