package com.almailem.ams.api.connector.service;

import com.almailem.ams.api.connector.config.PropertiesConfig;
import com.almailem.ams.api.connector.model.auth.AuthToken;
import com.almailem.ams.api.connector.model.stockreceipt.SearchStockReceiptHeader;
import com.almailem.ams.api.connector.model.stockreceipt.StockReceiptHeader;
import com.almailem.ams.api.connector.model.wms.WarehouseApiResponse;
import com.almailem.ams.api.connector.repository.StockReceiptHeaderRepository;
import com.almailem.ams.api.connector.repository.specification.StockReceiptHeaderSpecification;
import com.almailem.ams.api.connector.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
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
public class StockReceiptService {

    @Autowired
    StockReceiptHeaderRepository stockReceiptHeaderRepo;

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
     * Get All StockReceipt Details
     *
     * @return
     */
    public List<StockReceiptHeader> getAllStockReceiptDetails() {
        List<StockReceiptHeader> stockReceipts = stockReceiptHeaderRepo.findAll();
        return stockReceipts;
    }

    public StockReceiptHeader updateProcessedInboundOrder(Long stockReceiptHeaderId, String companyCode, String branchCode, String receiptNumber, Long processedStatusId) {
        StockReceiptHeader dbInboundOrder =
                stockReceiptHeaderRepo.getStockReceiptHeader(stockReceiptHeaderId);
//                        findTopByStockReceiptHeaderIdAndCompanyCodeAndBranchCodeAndReceiptNoOrderByOrderReceivedOnDesc(
//                        stockReceiptHeaderId, companyCode, branchCode, receiptNumber);

        log.info("orderId : " + receiptNumber);
        log.info("dbInboundOrder : " + dbInboundOrder);
        if (dbInboundOrder != null) {
//            dbInboundOrder.setProcessedStatusId(10L);
//            dbInboundOrder.setOrderProcessedOn(new Date());
//            StockReceiptHeader inboundOrder = stockReceiptHeaderRepo.save(dbInboundOrder);
//            return inboundOrder;
            stockReceiptHeaderRepo.updateProcessStatusId(dbInboundOrder.getStockReceiptHeaderId(), processedStatusId);
        }
        return dbInboundOrder;
    }

    /**
     * @param stockReceipt
     * @return
     */
    public WarehouseApiResponse postStockReceipt(com.almailem.ams.api.connector.model.wms.StockReceiptHeader stockReceipt) {
        AuthToken authToken = authTokenService.getTransactionServiceAuthToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken.getAccess_token());
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "warehouse/inbound/stockReceipt");
        HttpEntity<?> entity = new HttpEntity<>(stockReceipt, headers);
        ResponseEntity<WarehouseApiResponse> result =
                getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, WarehouseApiResponse.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    public List<StockReceiptHeader> findStockReceiptHeader(SearchStockReceiptHeader searchStockReceiptHeader) throws ParseException {


        if (searchStockReceiptHeader.getFromOrderProcessedOn() != null && searchStockReceiptHeader.getToOrderProcessedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchStockReceiptHeader.getFromOrderProcessedOn(), searchStockReceiptHeader.getToOrderProcessedOn());
            searchStockReceiptHeader.setFromOrderProcessedOn(dates[0]);
            searchStockReceiptHeader.setToOrderProcessedOn(dates[1]);
        }
        if (searchStockReceiptHeader.getFromOrderReceivedOn() != null && searchStockReceiptHeader.getToOrderReceivedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchStockReceiptHeader.getFromOrderReceivedOn(), searchStockReceiptHeader.getToOrderReceivedOn());
            searchStockReceiptHeader.setFromOrderReceivedOn(dates[0]);
            searchStockReceiptHeader.setToOrderReceivedOn(dates[1]);
        }


        StockReceiptHeaderSpecification spec = new StockReceiptHeaderSpecification(searchStockReceiptHeader);
        List<StockReceiptHeader> results = stockReceiptHeaderRepo.findAll(spec);
        return results;
    }

}
