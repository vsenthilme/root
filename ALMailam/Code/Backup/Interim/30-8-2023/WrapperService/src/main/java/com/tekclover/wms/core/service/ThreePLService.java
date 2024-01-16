package com.tekclover.wms.core.service;

import com.tekclover.wms.core.config.PropertiesConfig;
import com.tekclover.wms.core.model.threepl.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Collections;

@Service
@Slf4j
public class ThreePLService {
    @Autowired
    PropertiesConfig propertiesConfig;
    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
    private String getIDMasterServiceApiUrl () {
        return propertiesConfig.getIdmasterServiceUrl();
    }
    private String getMastersServiceApiUrl(){
        return propertiesConfig.getMastersServiceUrl();
    }
    private String getTransactionServiceApiUrl(){
        return propertiesConfig.getTransactionServiceUrl();
    }


    /*--------------------------------------BillingFormatId---------------------------------------------------------------------------------*/
    // GET ALL
    public BillingFormatId[] getBillingFormatIds (String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingformatid");
            ResponseEntity<BillingFormatId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BillingFormatId[].class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // GET
    public BillingFormatId getBillingFormatId(String warehouseId,Long billFormatId,String companyCodeId,String languageId,String plantId,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingformatid/" + billFormatId)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCodeId",companyCodeId)
                    .queryParam("languageId",languageId)
                    .queryParam("plantId",plantId);
            ResponseEntity<BillingFormatId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BillingFormatId.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // CREATE
    public BillingFormatId addBillingFormatId (AddBillingFormatId newBillingFormatId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(newBillingFormatId, headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingformatid")
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<BillingFormatId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BillingFormatId.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //UPDATE
    public BillingFormatId updateBillingFormatId (String warehouseId, Long billFormatId,String companyCodeId,String languageId,String plantId,String loginUserID,
                                                  UpdateBillingFormatId modifiedBillingFormatId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(modifiedBillingFormatId, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingformatid/" + billFormatId)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("warehouseId",warehouseId)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId);
            ResponseEntity<BillingFormatId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, BillingFormatId.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // DELETE
    public boolean deleteBillingFormatId(String warehouseId,Long billFormatId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingformatid/" + billFormatId)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("warehouseId",warehouseId)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }
    //SEARCH
    public BillingFormatId[] findBillingFormatId(FindBillingFormatId findBillingFormatId, String authToken){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"billingformatid/find");
            HttpEntity<?>entity = new HttpEntity<>(findBillingFormatId,headers);
            ResponseEntity<BillingFormatId[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BillingFormatId[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    /*--------------------------------------BillingFrequencyId---------------------------------------------------------------------------------*/
    // GET ALL
    public BillingFrequencyId[] getBillingFrequencyIds (String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingfrequencyid");
            ResponseEntity<BillingFrequencyId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BillingFrequencyId[].class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // GET
    public BillingFrequencyId getBillingFrequencyId(String warehouseId, Long billFrequencyId,String companyCodeId,String languageId,String plantId,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingfrequencyid/" + billFrequencyId)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCodeId",companyCodeId)
                    .queryParam("languageId",languageId)
                    .queryParam("plantId",plantId);
            ResponseEntity<BillingFrequencyId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BillingFrequencyId.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // CREATE
    public BillingFrequencyId addBillingFrequencyId (AddBillingFrequencyId newBillingFrequencyId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(newBillingFrequencyId, headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingfrequencyid")
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<BillingFrequencyId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BillingFrequencyId.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //UPDATE
    public BillingFrequencyId updateBillingFrequencyId (String warehouseId, Long billFrequencyId,String companyCodeId,String languageId,String plantId,String loginUserID,
                                                        UpdateBillingFrequencyId modifiedBillingFrequencyId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(modifiedBillingFrequencyId, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingfrequencyid/" + billFrequencyId)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("warehouseId",warehouseId)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId);
            ResponseEntity<BillingFrequencyId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, BillingFrequencyId.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // DELETE
    public boolean deleteBillingFrequencyId(String warehouseId,Long billFrequencyId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingfrequencyid/" + billFrequencyId)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("warehouseId",warehouseId)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }
    //SEARCH
    public BillingFrequencyId[] findBillingFrequencyId(FindBillingFrequencyId findBillingFrequencyId, String authToken){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"billingfrequencyid/find");
            HttpEntity<?>entity = new HttpEntity<>(findBillingFrequencyId,headers);
            ResponseEntity<BillingFrequencyId[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BillingFrequencyId[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    /*--------------------------------------BillingModeId---------------------------------------------------------------------------------*/
    // GET ALL
    public BillingModeId[] getBillingModeIds (String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingmodeid");
            ResponseEntity<BillingModeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BillingModeId[].class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // GET
    public BillingModeId getBillingModeId(String warehouseId, Long billModeId,String companyCodeId,String languageId,String plantId,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingmodeid/" + billModeId)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCodeId",companyCodeId)
                    .queryParam("languageId",languageId)
                    .queryParam("plantId",plantId);
            ResponseEntity<BillingModeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BillingModeId.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // CREATE
    public BillingModeId addBillingModeId (AddBillingModeId newBillingModeId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(newBillingModeId, headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingmodeid")
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<BillingModeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BillingModeId.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //UPDATE
    public BillingModeId updateBillingModeId (String warehouseId, Long billModeId,String companyCodeId,String languageId,String plantId,String loginUserID,
                                              UpdateBillingModeId modifiedBillingModeId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(modifiedBillingModeId, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingmodeid/" + billModeId)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("warehouseId",warehouseId)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId);
            ResponseEntity<BillingModeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, BillingModeId.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // DELETE
    public boolean deleteBillingModeId(String warehouseId,Long billModeId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingmodeid/" + billModeId)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("warehouseId",warehouseId)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }
    //SEARCH
    public BillingModeId[] findBillingModeId(FindBillingModeId findBillingModeId, String authToken){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"billingmodeid/find");
            HttpEntity<?>entity = new HttpEntity<>(findBillingModeId,headers);
            ResponseEntity<BillingModeId[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BillingModeId[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    /*--------------------------------------PaymentModeId---------------------------------------------------------------------------------*/
    // GET ALL
    public PaymentModeId[] getPaymentModeIds (String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "paymentmodeid");
            ResponseEntity<PaymentModeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PaymentModeId[].class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // GET
    public PaymentModeId getPaymentModeId(String warehouseId, Long paymentModeId,String companyCodeId,String languageId,String plantId,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "paymentmodeid/" + paymentModeId)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCodeId",companyCodeId)
                    .queryParam("languageId",languageId)
                    .queryParam("plantId",plantId);
            ResponseEntity<PaymentModeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PaymentModeId.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // CREATE
    public PaymentModeId addPaymentModeId(AddPaymentModeId newPaymentModeId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(newPaymentModeId, headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "paymentmodeid")
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<PaymentModeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PaymentModeId.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //UPDATE
    public PaymentModeId updatePaymentModeId (String warehouseId, Long paymentModeId,String companyCodeId,String languageId,String plantId,String loginUserID,
                                              UpdatePaymentModeId modifiedPaymentModeId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(modifiedPaymentModeId, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "paymentmodeid/" + paymentModeId)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("warehouseId",warehouseId)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId);

            ResponseEntity<PaymentModeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, PaymentModeId.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // DELETE
    public boolean deletePaymentModeId(String warehouseId,Long paymentModeId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "paymentmodeid/" + paymentModeId)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("warehouseId",warehouseId)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }
    //SEARCH
    public PaymentModeId[] findPaymentModeId(FindPaymentModeId findPaymentModeId, String authToken){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"paymentmodeid/find");
            HttpEntity<?>entity = new HttpEntity<>(findPaymentModeId,headers);
            ResponseEntity<PaymentModeId[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PaymentModeId[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    /*--------------------------------------PaymentTermId---------------------------------------------------------------------------------*/
    // GET ALL
    public PaymentTermId[] getPaymentTermIds (String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "paymenttermid");
            ResponseEntity<PaymentTermId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PaymentTermId[].class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // GET
    public PaymentTermId getPaymentTermId(String warehouseId, Long paymentTermId,String companyCodeId,String languageId,String plantId,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "paymenttermid/" + paymentTermId)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCodeId",companyCodeId)
                    .queryParam("languageId",languageId)
                    .queryParam("plantId",plantId);
            ResponseEntity<PaymentTermId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PaymentTermId.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // CREATE
    public PaymentTermId addPaymentTermId(AddPaymentTermId newPaymentTermId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(newPaymentTermId, headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "paymenttermid")
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<PaymentTermId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PaymentTermId.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //UPDATE
    public PaymentTermId updatePaymentTermId (String warehouseId, Long paymentTermId,String companyCodeId,String languageId,String plantId,String loginUserID,
                                              UpdatePaymentTermId modifiedPaymentTermId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(modifiedPaymentTermId, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "paymenttermid/" + paymentTermId)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("warehouseId",warehouseId)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId);

            ResponseEntity<PaymentTermId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, PaymentTermId.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // DELETE
    public boolean deletePaymentTermId(String warehouseId,Long paymentTermId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "paymenttermid/" + paymentTermId)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("warehouseId",warehouseId)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }
    //SEARCH
    public PaymentTermId[] findPaymentTermId(FindPaymentTermId findPaymentTermId, String authToken){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"paymenttermid/find");
            HttpEntity<?>entity = new HttpEntity<>(findPaymentTermId,headers);
            ResponseEntity<PaymentTermId[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PaymentTermId[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    /*--------------------------------------ServiceTypeId---------------------------------------------------------------------------------*/
    // GET ALL
    public ServiceTypeId[] getServiceTypeIds (String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "servicetypeid");
            ResponseEntity<ServiceTypeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ServiceTypeId[].class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // GET
    public ServiceTypeId getServiceTypeId(String warehouseId, Long moduleId, Long serviceTypeId,String companyCodeId,String languageId,String plantId,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "servicetypeid/" + serviceTypeId)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("moduleId",moduleId)
                    .queryParam("companyCodeId",companyCodeId)
                    .queryParam("languageId",languageId)
                    .queryParam("plantId",plantId);
            ResponseEntity<ServiceTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ServiceTypeId.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // CREATE
    public ServiceTypeId addServiceTypeId(AddServiceTypeId newServiceTypeId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(newServiceTypeId, headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "servicetypeid")
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<ServiceTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ServiceTypeId.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //UPDATE
    public ServiceTypeId updateServiceTypeId (String warehouseId,Long moduleId, Long serviceTypeId,String companyCodeId,String languageId,String plantId,String loginUserID,
                                              UpdateServiceTypeId modifiedServiceTypeId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(modifiedServiceTypeId, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "servicetypeid/" + serviceTypeId)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("warehouseId",warehouseId)
                            .queryParam("moduleId",moduleId)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId);
            ResponseEntity<ServiceTypeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ServiceTypeId.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // DELETE
    public boolean deleteServiceTypeId(String warehouseId,Long moduleId,Long serviceTypeId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "servicetypeid/" + serviceTypeId)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("warehouseId",warehouseId)
                            .queryParam("moduleId",moduleId)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }
    //SEARCH
    public ServiceTypeId[] findServiceTypeId(FindServiceTypeId findServiceTypeId, String authToken){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"servicetypeid/find");
            HttpEntity<?>entity = new HttpEntity<>(findServiceTypeId,headers);
            ResponseEntity<ServiceTypeId[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ServiceTypeId[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    /*--------------------------------------Billing---------------------------------------------------------------------------------*/
    // GET ALL
    public Billing[] getBillings (String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl( getMastersServiceApiUrl()+ "billing");
            ResponseEntity<Billing[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Billing[].class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // GET
    public Billing getBilling(String warehouseId, Long module, String partnerCode,String companyCodeId,String languageId,String plantId,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "billing/" + partnerCode)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("module",module)
                    .queryParam("companyCodeId",companyCodeId)
                    .queryParam("languageId",languageId)
                    .queryParam("plantId",plantId);
            ResponseEntity<Billing> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Billing.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // CREATE
    public Billing addBilling (AddBilling newBilling, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(newBilling, headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "billing")
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<Billing> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Billing.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //UPDATE
    public Billing updateBilling (String warehouseId, Long module,String partnerCode,String companyCodeId,String languageId,String plantId,String loginUserID,
                                  UpdateBilling modifiedBilling, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(modifiedBilling, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "billing/" + partnerCode)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("module",module)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId)
                            .queryParam("warehouseId",warehouseId);
            ResponseEntity<Billing> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, Billing.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // DELETE
    public boolean deleteBilling(String warehouseId,Long module,String partnerCode,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "billing/" + partnerCode)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("module",module)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId)
                            .queryParam("warehouseId",warehouseId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }
    //SEARCH
    public Billing[] findBilling(FindBilling findBilling, String authToken){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() +"billing/find");
            HttpEntity<?>entity = new HttpEntity<>(findBilling,headers);
            ResponseEntity<Billing[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Billing[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    /*--------------------------------------CbmInbound---------------------------------------------------------------------------------*/
    // GET ALL
    public CbmInbound[] getCbmInbounds (String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "cbminbound");
            ResponseEntity<CbmInbound[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, CbmInbound[].class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // GET
    public CbmInbound getCbmInbound(String warehouseId,String itemCode,String companyCodeId,String languageId,String plantId,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "cbminbound/" + itemCode)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCodeId",companyCodeId)
                    .queryParam("languageId",languageId)
                    .queryParam("plantId",plantId);
            ResponseEntity<CbmInbound> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, CbmInbound.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // CREATE
    public CbmInbound addCbmInbound (AddCbmInbound newCbmInbound, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(newCbmInbound, headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "cbminbound")
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<CbmInbound> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, CbmInbound.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //UPDATE
    public CbmInbound updateCbmInbound (String warehouseId,  String itemCode,String companyCodeId,String languageId,String plantId,String loginUserID,
                                        UpdateCbmInbound modifiedCbmInbound, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(modifiedCbmInbound, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "cbminbound/" + itemCode)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("warehouseId",warehouseId)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId);
            ResponseEntity<CbmInbound> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, CbmInbound.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // DELETE
    public boolean deleteCbmInbound(String warehouseId,String itemCode,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "cbminbound/" + itemCode)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("warehouseId",warehouseId)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }
    //SEARCH
    public CbmInbound[] findCbmInbound(FindCbmInbound findCbmInbound, String authToken){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() +"cbminbound/find");
            HttpEntity<?>entity = new HttpEntity<>(findCbmInbound,headers);
            ResponseEntity<CbmInbound[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, CbmInbound[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    /*--------------------------------------PriceList---------------------------------------------------------------------------------*/
    // GET ALL
    public PriceList[] getPriceLists (String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl()+ "pricelist");
            ResponseEntity<PriceList[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PriceList[].class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // GET
    public PriceList getPriceList(String warehouseId, Long module, Long priceListId, Long serviceTypeId,String companyCodeId,String languageId,String plantId,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "pricelist/" + priceListId)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("module",module)
                    .queryParam("serviceTypeId",serviceTypeId)
                    .queryParam("companyCodeId",companyCodeId)
                    .queryParam("languageId",languageId)
                    .queryParam("plantId",plantId);
            ResponseEntity<PriceList> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PriceList.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // CREATE
    public PriceList addPriceList (AddPriceList newPriceListId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(newPriceListId, headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "pricelist")
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<PriceList> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PriceList.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //UPDATE
    public PriceList updatePriceList (String warehouseId, Long module, Long priceListId, Long serviceTypeId,String companyCodeId,String languageId,String plantId, String loginUserID,
                                      UpdatePriceList modifiedPriceList, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(modifiedPriceList, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "pricelist/" + priceListId)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("module",module)
                            .queryParam("serviceTypeId",serviceTypeId)
                            .queryParam("warehouseId",warehouseId)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId);
            ResponseEntity<PriceList> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, PriceList.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // DELETE
    public boolean deletePriceList(String warehouseId,Long module, Long priceListId,Long serviceTypeId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "pricelist/" + priceListId)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("module",module)
                            .queryParam("serviceTypeId",serviceTypeId)
                            .queryParam("warehouseId",warehouseId)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }
    //SEARCH
    public PriceList[] findPriceList(FindPriceList findPriceList, String authToken){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() +"pricelist/find");
            HttpEntity<?>entity = new HttpEntity<>(findPriceList,headers);
            ResponseEntity<PriceList[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PriceList[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    /*--------------------------------------PriceListAssignment---------------------------------------------------------------------------------*/
    // GET ALL
    public PriceListAssignment[] getPriceListAssignments (String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "pricelistassignment");
            ResponseEntity<PriceListAssignment[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PriceListAssignment[].class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // GET
    public PriceListAssignment getPriceListAssignment(String warehouseId,String partnerCode,Long priceListId,String companyCodeId,String languageId,String plantId,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "pricelistassignment/" + priceListId)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("partnerCode",partnerCode)
                    .queryParam("companyCodeId",companyCodeId)
                    .queryParam("languageId",languageId)
                    .queryParam("plantId",plantId);
            ResponseEntity<PriceListAssignment> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PriceListAssignment.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // CREATE
    public PriceListAssignment addPriceListAssignment (AddPriceListAssignment newPriceListAssignment, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(newPriceListAssignment, headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "pricelistassignment")
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<PriceListAssignment> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PriceListAssignment.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //UPDATE
    public PriceListAssignment updatePriceListAssignment (String warehouseId,String partnerCode, Long priceListId,String companyCodeId,String languageId,String plantId,String loginUserID,
                                                          UpdatePriceListAssignment modifiedPriceListAssignment, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(modifiedPriceListAssignment, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "pricelistassignment/" + priceListId)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("partnerCode",partnerCode)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId)
                            .queryParam("warehouseId",warehouseId);
            ResponseEntity<PriceListAssignment> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, PriceListAssignment.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // DELETE
    public boolean deletePriceListAssignment(String warehouseId,Long priceListId,String partnerCode,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "pricelistassignment/" + priceListId)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("partnerCode",partnerCode)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId)
                            .queryParam("warehouseId",warehouseId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }
    //SEARCH
    public PriceListAssignment[] findPriceListAssignment(FindPriceListAssignment findPriceListAssignment, String authToken){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() +"pricelistassignment/find");
            HttpEntity<?>entity = new HttpEntity<>(findPriceListAssignment,headers);
            ResponseEntity<PriceListAssignment[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PriceListAssignment[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    /*--------------------------------------InvoiceHeader---------------------------------------------------------------------------------*/
    // GET ALL
    public InvoiceHeader[] getInvoiceHeaders (String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "invoiceheader");
            ResponseEntity<InvoiceHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, InvoiceHeader[].class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // GET
    public InvoiceHeader getInvoiceHeader(String warehouseId,String invoiceNumber, String partnerCode,String companyCodeId,String languageId,String plantId,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "invoiceheader/" + invoiceNumber)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("partnerCode",partnerCode)
                    .queryParam("companyCodeId",companyCodeId)
                    .queryParam("languageId",languageId)
                    .queryParam("plantId",plantId);
            ResponseEntity<InvoiceHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, InvoiceHeader.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // CREATE
    public InvoiceHeader addInvoiceHeader (AddInvoiceHeader newInvoiceHeader, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(newInvoiceHeader, headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "invoiceheader")
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<InvoiceHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, InvoiceHeader.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //UPDATE
    public InvoiceHeader updateInvoiceHeader (String warehouseId,String invoiceNumber, String partnerCode,String companyCodeId,String languageId,String plantId,String loginUserID,
                                              UpdateInvoiceHeader modifiedInvoiceHeader, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(modifiedInvoiceHeader, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "invoiceheader/" + invoiceNumber)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("partnerCode",partnerCode)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId)
                            .queryParam("warehouseId",warehouseId);
            ResponseEntity<InvoiceHeader> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, InvoiceHeader.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // DELETE
    public boolean deleteInvoiceHeader(String warehouseId,String invoiceNumber, String partnerCode,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "invoiceheader/" + invoiceNumber)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("partnerCode",partnerCode)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId)
                            .queryParam("warehouseId",warehouseId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }
    //SEARCH
    public InvoiceHeader[] findInvoiceHeader(FindInvoiceHeader findInvoiceHeader, String authToken){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() +"invoiceheader/find");
            HttpEntity<?>entity = new HttpEntity<>(findInvoiceHeader,headers);
            ResponseEntity<InvoiceHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, InvoiceHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    /*--------------------------------------InvoiceLine---------------------------------------------------------------------------------*/
    // GET ALL
    public InvoiceLine[] getInvoiceLines (String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "invoiceline");
            ResponseEntity<InvoiceLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, InvoiceLine[].class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // GET
    public InvoiceLine getInvoiceLine(String warehouseId,String invoiceNumber, String partnerCode,Long lineNumber,String companyCodeId,String languageId,String plantId,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "invoiceline/" + lineNumber)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("invoiceNumber",invoiceNumber)
                    .queryParam("companyCodeId",companyCodeId)
                    .queryParam("languageId",languageId)
                    .queryParam("plantId",plantId)
                    .queryParam("partnerCode",partnerCode);
            ResponseEntity<InvoiceLine> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, InvoiceLine.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // CREATE
    public InvoiceLine addInvoiceLine (AddInvoiceLine newInvoiceLine, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(newInvoiceLine, headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "invoiceline")
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<InvoiceLine> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, InvoiceLine.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //UPDATE
    public InvoiceLine updateInvoiceLine (String warehouseId,String invoiceNumber, String partnerCode,Long lineNumber, String companyCodeId,String languageId,String plantId,String loginUserID,
                                          UpdateInvoiceLine modifiedInvoiceLine, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(modifiedInvoiceLine, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "invoiceline/" + lineNumber)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("invoiceNumber",invoiceNumber)
                            .queryParam("partnerCode",partnerCode)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId)
                            .queryParam("warehouseId",warehouseId);
            ResponseEntity<InvoiceLine> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, InvoiceLine.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // DELETE
    public boolean deleteInvoiceLine(String warehouseId,String invoiceNumber, String partnerCode,Long lineNumber,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "invoiceline/" + lineNumber)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("invoiceNumber",invoiceNumber)
                            .queryParam("partnerCode",partnerCode)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId)
                            .queryParam("warehouseId",warehouseId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }
    //SEARCH
    public InvoiceLine[] findInvoiceLine(FindInvoiceLine findInvoiceLine, String authToken){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() +"invoiceline/find");
            HttpEntity<?>entity = new HttpEntity<>(findInvoiceLine,headers);
            ResponseEntity<InvoiceLine[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, InvoiceLine[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    /*--------------------------------------ProformaInvoiceHeader---------------------------------------------------------------------------------*/
    // GET ALL
    public ProformaInvoiceHeader[] getProformaInvoiceHeaders (String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "proformainvoiceheader");
            ResponseEntity<ProformaInvoiceHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ProformaInvoiceHeader[].class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // GET
    public ProformaInvoiceHeader getProformaInvoiceHeader(String warehouseId, String proformaBillNo, String partnerCode,String companyCodeId,String languageId,String plantId,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "proformainvoiceheader/" + proformaBillNo)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCodeId",companyCodeId)
                    .queryParam("languageId",languageId)
                    .queryParam("plantId",plantId)
                    .queryParam("partnerCode",partnerCode);
            ResponseEntity<ProformaInvoiceHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ProformaInvoiceHeader.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // CREATE
    public ProformaInvoiceHeader addProformaInvoiceHeader(AddProformaInvoiceHeader newProformaInvoiceHeader, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(newProformaInvoiceHeader, headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "proformainvoiceheader")
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<ProformaInvoiceHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ProformaInvoiceHeader.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //UPDATE
    public ProformaInvoiceHeader updateProformaInvoiceHeader(String warehouseId,String proformaBillNo,String partnerCode,String companyCodeId,String languageId,String plantId,String loginUserID,
                                                             UpdateProformaInvoiceHeader modifiedProformaInvoiceHeader, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(modifiedProformaInvoiceHeader, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "proformainvoiceheader/" + proformaBillNo)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("partnerCode",partnerCode)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId)
                            .queryParam("warehouseId",warehouseId);
            ResponseEntity<ProformaInvoiceHeader> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ProformaInvoiceHeader.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // DELETE
    public boolean deleteProformaInvoiceHeader(String warehouseId,String proformaBillNo,String partnerCode,String companyCodeId,String languageId,String plantId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "proformainvoiceheader/" + proformaBillNo)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("partnerCode",partnerCode)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId)
                            .queryParam("warehouseId",warehouseId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }
    //SEARCH
    public ProformaInvoiceHeader[] findProformaInvoiceHeader(FindProformaInvoiceHeader findProformaInvoiceHeader, String authToken){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() +"proformainvoiceheader/find");
            HttpEntity<?>entity = new HttpEntity<>(findProformaInvoiceHeader,headers);
            ResponseEntity<ProformaInvoiceHeader[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ProformaInvoiceHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    /*--------------------------------------ProformaInvoiceLine---------------------------------------------------------------------------------*/
    // GET ALL
    public ProformaInvoiceLine[] getProformaInvoiceLines(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "proformainvoiceline");
            ResponseEntity<ProformaInvoiceLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ProformaInvoiceLine[].class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // GET
    public ProformaInvoiceLine getProformaInvoiceLine(String warehouseId, String proformaBillNo, String partnerCode,Long lineNumber,String companyCodeId,String languageId,String plantId,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "proformainvoiceline/" + lineNumber)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCodeId",companyCodeId)
                    .queryParam("languageId",languageId)
                    .queryParam("plantId",plantId)
                    .queryParam("proformaBillNo",proformaBillNo)
                    .queryParam("partnerCode",partnerCode);
            ResponseEntity<ProformaInvoiceLine> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ProformaInvoiceLine.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // CREATE
    public ProformaInvoiceLine addProformaInvoiceLine(AddProformaInvoiceLine newProformaInvoiceLine, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(newProformaInvoiceLine, headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "proformainvoiceline")
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<ProformaInvoiceLine> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ProformaInvoiceLine.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //UPDATE
    public ProformaInvoiceLine updateProformaInvoiceLine(String warehouseId,String proformaBillNo,String partnerCode, Long lineNumber,String companyCodeId,String languageId,String plantId,String loginUserID,
                                                         UpdateProformaInvoiceLine modifiedProformaInvoiceLine, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(modifiedProformaInvoiceLine, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "proformainvoiceline/" + lineNumber)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("proformaBillNo",proformaBillNo)
                            .queryParam("partnerCode",partnerCode)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId)
                            .queryParam("warehouseId",warehouseId);
            ResponseEntity<ProformaInvoiceLine> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ProformaInvoiceLine.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // DELETE
    public boolean deleteProformaInvoiceLine(String warehouseId,String proformaBillNo,String partnerCode,
                                             Long lineNumber,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Threepl RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "proformainvoiceline/" + lineNumber)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("proformaBillNo",proformaBillNo)
                            .queryParam("partnerCode",partnerCode)
                            .queryParam("companyCodeId",companyCodeId)
                            .queryParam("languageId",languageId)
                            .queryParam("plantId",plantId)
                            .queryParam("warehouseId",warehouseId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }
    //SEARCH
    public ProformaInvoiceLine[] findProformaInvoiceLine(FindProformaInvoiceLine findProformaInvoiceLine, String authToken){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() +"proformainvoiceline/find");
            HttpEntity<?>entity = new HttpEntity<>(findProformaInvoiceLine,headers);
            ResponseEntity<ProformaInvoiceLine[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ProformaInvoiceLine[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
