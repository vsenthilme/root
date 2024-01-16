package com.almailem.ams.api.connector.service;

import com.almailem.ams.api.connector.config.PropertiesConfig;
import com.almailem.ams.api.connector.model.auth.AuthToken;
import com.almailem.ams.api.connector.model.master.ItemMaster;
import com.almailem.ams.api.connector.model.wms.Item;
import com.almailem.ams.api.connector.model.wms.WarehouseApiResponse;
import com.almailem.ams.api.connector.repository.ItemMasterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
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
public class ItemMasterService {

    @Autowired
    ItemMasterRepository itemMasterRepository;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    PropertiesConfig propertiesConfig;


    private String getMasterServiceApiUrl() {
        return propertiesConfig.getMastersServiceUrl();
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
     * Get All ItemMaster Details
     *
     * @return
     */
    public List<ItemMaster> getAllItemMasterDetails() {
        List<ItemMaster> imList = itemMasterRepository.findAll();
        return imList;
    }

    /**
     * @param itemCode
     */
    public void updateProcessedItemMaster(Long itemMasterId, String companyCode, String branchCode, String manufacturerName, String itemCode, Long processedStatusId) {
        ItemMaster dbItemMaster = itemMasterRepository.getItemMaster(itemMasterId);
//                findTopByItemMasterIdAndCompanyCodeAndBranchCodeAndItemCodeAndManufacturerShortNameOrderByOrderReceivedOn(
//                itemMasterId, companyCode, branchCode, itemCode, manufacturerName);
        log.info("ItemCode : " + itemCode);
        log.info("dbItemMaster : " + dbItemMaster);
        if (dbItemMaster != null) {
//            dbItemMaster.setProcessedStatusId(10L);
//            dbItemMaster.setOrderProcessedOn(new Date());
//            ItemMaster itemMaster = itemMasterRepository.save(dbItemMaster);
//            itemMasterRepository.updateProcessStatusId(itemCode, new Date());
            itemMasterRepository.updateProcessStatusId(dbItemMaster.getItemMasterId(), processedStatusId);
        }
    }

    /**
     * @param itemCode
     */
//    public void updateProcessedItemMaster(String companyCode, String branchCode, String manufacturerName, String itemCode, String remark) {
//        ItemMaster dbItemMaster = itemMasterRepository.findTopByCompanyCodeAndBranchCodeAndItemCodeAndManufacturerShortNameOrderByOrderReceivedOnDesc(
//                companyCode, branchCode, itemCode, manufacturerName);
//        log.info("ItemCode : " + itemCode);
//        log.info("dbItemMaster : " + dbItemMaster);
//        if (dbItemMaster != null) {
//            dbItemMaster.setProcessedStatusId(10L);
//            dbItemMaster.setRemarks(remark);
//            dbItemMaster.setOrderProcessedOn(new Date());
//            ItemMaster itemMaster = itemMasterRepository.save(dbItemMaster);
//            itemMasterRepository.updateProcessStatusId(itemCode, new Date());
//        }
//    }

    /**
     * @param item
     * @return
     */
    public WarehouseApiResponse postItemMaster(Item item) {
        AuthToken authToken = authTokenService.getMastersServiceAuthToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken.getAccess_token());
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(getMasterServiceApiUrl() + "masterorder/master/item");
        HttpEntity<?> entity = new HttpEntity<>(item, headers);
        ResponseEntity<WarehouseApiResponse> result =
                getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, WarehouseApiResponse.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

}
