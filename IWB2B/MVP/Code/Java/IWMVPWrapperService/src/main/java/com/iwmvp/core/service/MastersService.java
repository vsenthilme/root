package com.iwmvp.core.service;

import com.iwmvp.core.config.PropertiesConfig;
import com.iwmvp.core.model.master.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Collections;

@Service
@Slf4j
public class MastersService {

    @Autowired
    PropertiesConfig propertiesConfig;

    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
    @Autowired
    AuthTokenService authTokenService;
    private String getMastersServiceApiUrl () {
        return propertiesConfig.getMasterServiceUrl();
    }

    //--------------------------------------------Customer ------------------------------------------------------------------------
    public Customer[] getCustomers(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "customer");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<Customer[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Customer[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // GET
    public Customer getCustomer(Long customerId,String companyId,String languageId,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "customer/" + customerId)
                            .queryParam("companyId", companyId)
                            .queryParam("languageId", languageId);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<Customer> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Customer.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //CREATE
    public Customer createCustomer(AddCustomer newCustomer, String loginUserID, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "customer")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newCustomer, headers);
        ResponseEntity<Customer> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Customer.class);
        //		log.info("result : " + result.getStatusCode());
        return result.getBody();
    }
    //PATCH
    public Customer updateCustomer(Long customerId,String companyId,String languageId, String loginUserID,
                                   UpdateCustomer updateCustomer, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MVP RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(updateCustomer, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "customer/" + customerId)
                            .queryParam("companyId", companyId)
                            .queryParam("languageId", languageId)
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<Customer> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, Customer.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //APPROVE
    public Customer approveCustomer(Long customerId, String loginUserID,
                                    String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MVP RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "customer/" + customerId)
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<Customer> result = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, Customer.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //DELETE
    public boolean deleteCustomer(Long customerId,String companyId,String languageId,String loginUserID,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MVP RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "customer/" + customerId)
                            .queryParam("companyId", companyId)
                            .queryParam("languageId", languageId)
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //SEARCH
    public Customer[] findCustomer(FindCustomer findCustomer, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "customer/findCustomer");
            HttpEntity<?> entity = new HttpEntity<>(findCustomer, headers);
            ResponseEntity<Customer[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Customer[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //--------------------------------------------LoyaltyCategory ------------------------------------------------------------------------
    public LoyaltyCategory[] getAllLoyaltyCategory(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "loyaltycategory");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<LoyaltyCategory[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, LoyaltyCategory[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // GET
    public LoyaltyCategory getLoyaltyCategory(Long rangeId,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "loyaltycategory/" + rangeId);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<LoyaltyCategory> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, LoyaltyCategory.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //CREATE
    public LoyaltyCategory createLoyaltyCategory(AddLoyaltyCategory newLoyaltyCategory, String loginUserID, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "loyaltycategory")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newLoyaltyCategory, headers);
        ResponseEntity<LoyaltyCategory> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, LoyaltyCategory.class);
        return result.getBody();
    }
    //PATCH
    public LoyaltyCategory updateLoyaltyCategory(Long rangeId, String loginUserID,
                                                 UpdateLoyaltyCategory updateLoyaltyCategory, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MVP RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(updateLoyaltyCategory, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "loyaltycategory/" + rangeId)
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<LoyaltyCategory> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, LoyaltyCategory.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //DELETE
    public boolean deleteLoyaltyCategory(Long rangeId,String loginUserID,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MVP RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "loyaltycategory/" + rangeId)
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //SEARCH
    public LoyaltyCategory[] findLoyaltyCategory(FindLoyaltyCategory findLoyaltyCategory,String authToken){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "loyaltycategory/findLoyaltyCategory");
            HttpEntity<?> entity = new HttpEntity<>(findLoyaltyCategory,headers);
            ResponseEntity<LoyaltyCategory[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, LoyaltyCategory[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //--------------------------------------------LoyaltySetup ------------------------------------------------------------------------
    public LoyaltySetup[] getAllLoyaltySetup(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "loyaltysetup");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<LoyaltySetup[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, LoyaltySetup[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // GET
    public LoyaltySetup getLoyaltySetup(Long  loyaltyId,String  categoryId,String companyId,String languageId,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "loyaltysetup/" + loyaltyId)
                            .queryParam("categoryId",categoryId)
                            .queryParam("companyId", companyId)
                            .queryParam("languageId", languageId);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<LoyaltySetup> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, LoyaltySetup.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //CREATE
    public LoyaltySetup createLoyaltySetup(AddLoyaltySetup newLoyaltySetup, String loginUserID, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "loyaltysetup")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newLoyaltySetup, headers);
        ResponseEntity<LoyaltySetup> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, LoyaltySetup.class);
        return result.getBody();
    }
    //PATCH
    public LoyaltySetup updateLoyaltySetup(Long loyaltyId, String categoryId,String companyId,String languageId,String loginUserID,
                                           UpdateLoyaltySetup updateLoyaltySetup, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MVP RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(updateLoyaltySetup, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "loyaltysetup/" + loyaltyId)
                            .queryParam("categoryId",categoryId)
                            .queryParam("companyId", companyId)
                            .queryParam("languageId", languageId)
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<LoyaltySetup> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, LoyaltySetup.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //DELETE
    public boolean deleteLoyaltySetup(Long loyaltyId,String categoryId,String companyId,String languageId,String loginUserID,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MVP RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "loyaltysetup/" + loyaltyId)
                            .queryParam("categoryId", categoryId)
                            .queryParam("companyId", companyId)
                            .queryParam("languageId", languageId)
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //SEARCH
    public LoyaltySetup[] findLoyaltySetup(FindLoyaltySetup findLoyaltySetup,String authToken){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() +"loyaltysetup/findLoyaltySetup");
            HttpEntity<?>entity = new HttpEntity<>(findLoyaltySetup,headers);
            ResponseEntity<LoyaltySetup[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, LoyaltySetup[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //--------------------------------------------OrderDetailsHeader ------------------------------------------------------------------------
    public OrderDetailsHeaderOutput[] getAllOrderDetailsHeader(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "orderdetailsheader");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<OrderDetailsHeaderOutput[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, OrderDetailsHeaderOutput[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // GET
    public OrderDetailsHeaderOutput getOrderDetailsHeader(Long orderId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "orderdetailsheader/" + orderId);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<OrderDetailsHeaderOutput> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, OrderDetailsHeaderOutput.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //CREATE
    public OrderDetailsHeaderOutput createOrderDetailsHeader(AddOrderDetailsHeader newOrderDetailsHeader, String loginUserID, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "orderdetailsheader")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newOrderDetailsHeader, headers);
        ResponseEntity<OrderDetailsHeaderOutput> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, OrderDetailsHeaderOutput.class);
        //		log.info("result : " + result.getStatusCode());
        return result.getBody();
    }
    //PATCH
    public OrderDetailsHeaderOutput updateOrderDetailsHeader(Long orderId, String loginUserID,
                                                 AddOrderDetailsHeader updateOrderDetailsHeader, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MVP RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(updateOrderDetailsHeader, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "orderdetailsheader/" + orderId)
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<OrderDetailsHeaderOutput> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, OrderDetailsHeaderOutput.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Approve
    public OrderDetailsHeaderOutput approveOrderDetailsHeader(Long orderId, String loginUserID,
                                                             String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MVP RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "orderdetailsheader/" + orderId)
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<OrderDetailsHeaderOutput> result = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, OrderDetailsHeaderOutput.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //DELETE
    public boolean deleteOrderDetailsHeader(Long orderId,String loginUserID,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MVP RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "orderdetailsheader/" + orderId)
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //SEARCH
    public OrderDetailsHeaderOutput[] findOrderDetailsHeader(FindOrderDetailsHeader findOrderDetailsHeader,String authToken){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() +"orderdetailsheader/findOrderDetailsHeader");
            HttpEntity<?>entity = new HttpEntity<>(findOrderDetailsHeader,headers);
            ResponseEntity<OrderDetailsHeaderOutput[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, OrderDetailsHeaderOutput[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //--------------------------------------------NumberRange ------------------------------------------------------------------------
    public NumberRange[] getAllNumberRange(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "numberrange");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<NumberRange[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, NumberRange[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    // GET
    public NumberRange getNumberRange(Long numberRangeCode, String numberRangeObject,String companyId,String languageId,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "numberrange/" + numberRangeCode)
                            .queryParam("numberRangeObject",numberRangeObject)
                            .queryParam("companyId", companyId)
                            .queryParam("languageId", languageId);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<NumberRange> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, NumberRange.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //CREATE
    public NumberRange createNumberRange(AddNumberRange newNumberRange, String loginUserID, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "numberrange")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newNumberRange, headers);
        ResponseEntity<NumberRange> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, NumberRange.class);
        return result.getBody();
    }
    //PATCH
    public NumberRange updateNumberRange(Long numberRangeCode, String numberRangeObject,String companyId,String languageId, String loginUserID,
                                           UpdateNumberRange updateNumberRange, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MVP RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(updateNumberRange, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "numberrange/" + numberRangeCode)
                            .queryParam("numberRangeObject",numberRangeObject)
                            .queryParam("companyId", companyId)
                            .queryParam("languageId", languageId)
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<NumberRange> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, NumberRange.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //DELETE
    public boolean deleteNumberRange(Long numberRangeCode, String numberRangeObject,String companyId,String languageId,String loginUserID,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MVP RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "numberrange/" + numberRangeCode)
                            .queryParam("numberRangeObject",numberRangeObject)
                            .queryParam("companyId", companyId)
                            .queryParam("languageId", languageId)
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //SEARCH
    public NumberRange[] findNumberRange(FindNumberRange findNumberRange,String authToken){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "numberrange/findNumberRange");
            HttpEntity<?> entity = new HttpEntity<>(findNumberRange, headers);
            ResponseEntity<NumberRange[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, NumberRange[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //--------------------------------------------User------------------------------------------------------------------------
    //Validate user
    /**
     *
     * @param userID
     * @param password
     * @param authToken
     * @return
     */
    public User validateUser (String userID, String password, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "/login/validate")
                    .queryParam("userID", userID)
                    .queryParam("password", password);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<User> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, User.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    // GET ALL
    public User[] getAllUser (String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "/login/users");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<User[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, User[].class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET
    public User getUser (String id, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "/login/user/" + id);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<User> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, User.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST
    public User createUser (AddUser newUser, String loginUserID, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "/login/user")
                .queryParam("loginUserID", loginUserID);

        HttpEntity<?> entity = new HttpEntity<>(newUser, headers);
        ResponseEntity<User> result =
                getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, User.class);
        return result.getBody();
    }

    // PATCH
    public User updateUser(String id, String loginUserID,
                           @Valid UpdateUser updateUser, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(updateUser, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "/login/user/" + id)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<User> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, User.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteUser (String id, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getMastersServiceApiUrl() + "/login/user/" + id)
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
