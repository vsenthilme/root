package com.almailem.ams.api.connector.service;

import com.almailem.ams.api.connector.config.PropertiesConfig;
import com.almailem.ams.api.connector.model.auth.AuthToken;
import com.almailem.ams.api.connector.model.stockadjustment.StockAdjustment;
import com.almailem.ams.api.connector.model.wms.WarehouseApiResponse;
import com.almailem.ams.api.connector.repository.StockAdjustmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class StockAdjustmentService {

    @Autowired
    StockAdjustmentRepository stockAdjustmentRepo;

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

    public List<StockAdjustment> getAllStockAdjustment() {
        List<StockAdjustment> stockAdjustments = stockAdjustmentRepo.findAll();
        return stockAdjustments;
    }

    /**
     *
     * @param stockAdjustmentId
     * @param companyCode
     * @param branchCode
     * @param itemCode
     * @param processStatusId
     * @return
     */
    public StockAdjustment updateProcessedStockAdjustment(Long stockAdjustmentId, String companyCode, String branchCode,
                                                          String itemCode, Long processStatusId) {
        StockAdjustment dbSA =
                stockAdjustmentRepo.getStockAdjustment(stockAdjustmentId);
//                        findTopByStockAdjustmentIdAndCompanyCodeAndBranchCodeAndItemCodeOrderByDateOfAdjustmentDesc(
//                        stockAdjustmentId, companyCode, branchCode, itemCode);
        log.info("Item Code: " + itemCode);
        log.info("dbStockAdjustment: " + dbSA);
        if (dbSA != null) {
//            dbSA.setProcessedStatusId(10L);
//            dbSA.setOrderProcessedOn(new Date());
            stockAdjustmentRepo.updateProcessStatusId(stockAdjustmentId, processStatusId);
        }
        return dbSA;
    }

    public WarehouseApiResponse postStockAdjustment(com.almailem.ams.api.connector.model.wms.StockAdjustment stockAdjustment) {
        AuthToken authToken = authTokenService.getTransactionServiceAuthToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken.getAccess_token());
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "warehouse/stockAdjustment");
        HttpEntity<?> entity = new HttpEntity<>(stockAdjustment, headers);
        ResponseEntity<WarehouseApiResponse> result =
                getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, WarehouseApiResponse.class);
        log.info("result: " + result.getStatusCode());
        return result.getBody();
    }
}