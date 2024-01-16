package com.mnrclara.wrapper.core.service;

import com.mnrclara.wrapper.core.config.PropertiesConfig;
import com.mnrclara.wrapper.core.model.auth.AuthToken;
import com.mnrclara.wrapper.core.model.cgsetup.CgEntity;
import com.mnrclara.wrapper.core.model.cgsetup.FindCgEntity;
import com.mnrclara.wrapper.core.model.cgtransaction.*;
import com.mnrclara.wrapper.core.model.crm.UploadFileResponse;
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
import java.util.List;

@Slf4j
@Service
public class CGTransactionService {

    @Autowired
    PropertiesConfig propertiesConfig;

    @Autowired
    AuthTokenService authTokenService;

    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    private String getCgTransactionServiceApiUrl() {
        return propertiesConfig.getCgTransactionServiceUrl();
    }


    //--------------------------------------------OwnerShipRequest ------------------------------------------------------------------------

    //GET ALL
    public OwnerShipRequest[] getAllOwnerShipRequest(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "ownershiprequest");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<OwnerShipRequest[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, OwnerShipRequest[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET
    public OwnerShipRequest getOwnerShipRequest(String languageId, String companyId, Long requestId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "ownershiprequest/" + requestId)
                            .queryParam("languageId", languageId)
                            .queryParam("companyId", companyId);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<OwnerShipRequest> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, OwnerShipRequest.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //CREATE
    public OwnerShipRequest createOwnerShipRequest(OwnerShipRequest newOwnerShipRequest, String loginUserID, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "ownershiprequest")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newOwnerShipRequest, headers);
        ResponseEntity<OwnerShipRequest> result = getRestTemplate().exchange(builder.toUriString(),
                HttpMethod.POST, entity, OwnerShipRequest.class);
        return result.getBody();
    }


    //PATCH
    public OwnerShipRequest updateOwnerShipRequest(String languageId, Long requestId, String companyId, String loginUserID,
                                                   OwnerShipRequest updateOwnerShipRequest, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MNRClara's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(updateOwnerShipRequest, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "ownershiprequest/" + requestId)
                            .queryParam("languageId", languageId)
                            .queryParam("companyId", companyId)
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<OwnerShipRequest> result = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.PATCH, entity, OwnerShipRequest.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //DELETE
    public boolean deleteOwnerShipRequest(String languageId, String companyId, Long requestId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MNRClara's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "ownershiprequest/" + requestId)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("languageId", languageId)
                            .queryParam("companyId", companyId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.DELETE, entity, String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //SEARCH
    public OwnerShipRequest[] findOwnerShipRequest(FindOwnerShipRequest findOwnerShipRequest, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "ownershiprequest/find");
            HttpEntity<?> entity = new HttpEntity<>(findOwnerShipRequest, headers);
            ResponseEntity<OwnerShipRequest[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, OwnerShipRequest[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

//    /**
//     *
//     * @param languageId
//     * @param requestId
//     * @param companyId
//     * @param loginUserID
//     * @param updateOwnerShipRequest
//     * @param authToken
//     * @return
//     */
//    public OwnerShipRequest updateRequestId(String languageId, Long requestId, String companyId, String loginUserID,
//                                                   OwnerShipRequest updateOwnerShipRequest, String authToken) {
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//            headers.add("User-Agent", "MNRClara's RestTemplate");
//            headers.add("Authorization", "Bearer " + authToken);
//            HttpEntity<?> entity = new HttpEntity<>(updateOwnerShipRequest, headers);
//            HttpClient client = HttpClients.createDefault();
//            RestTemplate restTemplate = getRestTemplate();
//            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
//
//            UriComponentsBuilder builder =
//                    UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "ownershiprequest/updateRequestId")
//                            .queryParam("languageId", languageId)
//                            .queryParam("requestId",requestId)
//                            .queryParam("companyId", companyId)
//                            .queryParam("loginUserID", loginUserID);
//            ResponseEntity<OwnerShipRequest> result = restTemplate.exchange(builder.toUriString(),
//                    HttpMethod.PATCH, entity, OwnerShipRequest.class);
//            log.info("result : " + result.getStatusCode());
//            return result.getBody();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }

    //--------------------------------------------StorePartnerListing ------------------------------------------------------------------------

    //GET ALL
    public StorePartnerListing[] getAllStorePartnerListing(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "storepartnerlisting");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<StorePartnerListing[]> result = getRestTemplate()
                    .exchange(builder.toUriString(), HttpMethod.GET, entity, StorePartnerListing[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET
    public StorePartnerListing getStorePartnerListing(String languageId, Long versionNumber, String storeId,
                                                      String companyId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "storepartnerlisting/" + storeId)
                            .queryParam("languageId", languageId)
                            .queryParam("versionNumber", versionNumber)
                            .queryParam("companyId", companyId);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<StorePartnerListing> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StorePartnerListing.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //CREATE
    public StorePartnerListing createStorePartnerListing(StorePartnerListing newStorePartnerListing,
                                                         String loginUserID, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "storepartnerlisting")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newStorePartnerListing, headers);

        ResponseEntity<StorePartnerListing> result = getRestTemplate()
                .exchange(builder.toUriString(), HttpMethod.POST, entity, StorePartnerListing.class);
        return result.getBody();
    }


    //PATCH
    public StorePartnerListing updateStorePartnerListing(String languageId, Long versionNumber, String storeId, String companyId,
                                                         String loginUserID, StorePartnerListing storePartnerListing,
                                                         String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MNRClara's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(storePartnerListing, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "storepartnerlisting/" + storeId)
                            .queryParam("languageId", languageId)
                            .queryParam("companyId", companyId)
                            .queryParam("versionNumber", versionNumber)
                            .queryParam("loginUserID", loginUserID);

            ResponseEntity<StorePartnerListing> result = restTemplate.
                    exchange(builder.toUriString(), HttpMethod.PATCH, entity, StorePartnerListing.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //DELETE
    public boolean deleteStorePartnerListing(String languageId, String companyId, String storeId,
                                             Long versionNumber, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MNRClara's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "storepartnerlisting/" + storeId)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("languageId", languageId)
                            .queryParam("versionNumber", versionNumber)
                            .queryParam("companyId", companyId);

            ResponseEntity<String> result = getRestTemplate().
                    exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //SEARCH
    public StorePartnerListing[] findStorePartnerListing(FindStorePartnerListing findStorePartnerListing, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "storepartnerlisting/find");
            HttpEntity<?> entity = new HttpEntity<>(findStorePartnerListing, headers);
            ResponseEntity<StorePartnerListing[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StorePartnerListing[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //SEARCH - By version
    public StorePartnerListing[] findStorePartnerListingByVersion(FindStorePartnerListing findStorePartnerListing, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "storepartnerlisting/find/version");
            HttpEntity<?> entity = new HttpEntity<>(findStorePartnerListing, headers);
            ResponseEntity<StorePartnerListing[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StorePartnerListing[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Scheduler
    public UploadFileResponse uploadStorePartnerListing () {
        try {
            AuthToken authToken = authTokenService.getCGTransactionServiceAuthToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "storepartnerlisting/batchupload");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<UploadFileResponse> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, UploadFileResponse.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //SEARCH
    public MatchResult[] findMatchResult(FindMatchResult findMatchResult, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "storepartnerlisting/findMatchResult");
            HttpEntity<?> entity = new HttpEntity<>(findMatchResult, headers);
            ResponseEntity<MatchResult[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, MatchResult[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //SEARCH
    public Group findGroup(FindMatchResult findMatchResult, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "storepartnerlisting/findGroup");
            HttpEntity<?> entity = new HttpEntity<>(findMatchResult, headers);
            ResponseEntity<Group> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Group.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //SEARCH
    public Group findGroupCount(FindMatchResult findMatchResult, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "storepartnerlisting/findGroup/count");
            HttpEntity<?> entity = new HttpEntity<>(findMatchResult, headers);
            ResponseEntity<Group> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Group.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //SEARCH
    public Group findStoreEntity(FindMatchResult findMatchResult, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "storepartnerlisting/findStore/entity");
            HttpEntity<?> entity = new HttpEntity<>(findMatchResult, headers);
            ResponseEntity<Group> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Group.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //GET ALL
    public GroupStoreList getAllGroupStore(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "storepartnerlisting/groupStore");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<GroupStoreList> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, GroupStoreList.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //SEARCH
    public BrotherSisterResult findBrotherSisterGroup(FindMatchResults findMatchResult, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "storepartnerlisting/findBrotherSister");
            HttpEntity<?> entity = new HttpEntity<>(findMatchResult, headers);
            ResponseEntity<BrotherSisterResult> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BrotherSisterResult.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

//    /**
//     *
//     * @param versionNumber
//     * @param languageId
//     * @param storeId
//     * @param companyId
//     * @param loginUserID
//     * @param storePartnerListing
//     * @param authToken
//     * @return
//     */
//    public StorePartnerListing updateStorePartner(Long versionNumber,String languageId, String storeId, String companyId, String loginUserID,
//                                            StorePartnerListing storePartnerListing, String authToken) {
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//            headers.add("User-Agent", "MNRClara's RestTemplate");
//            headers.add("Authorization", "Bearer " + authToken);
//            HttpEntity<?> entity = new HttpEntity<>(storePartnerListing, headers);
//            HttpClient client = HttpClients.createDefault();
//            RestTemplate restTemplate = getRestTemplate();
//            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
//
//            UriComponentsBuilder builder =
//                    UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "storepartnerlisting/updateStorePartner")
//                            .queryParam("languageId", languageId)
//                            .queryParam("versionNumber",versionNumber)
//                            .queryParam("storeId",storeId)
//                            .queryParam("companyId", companyId)
//                            .queryParam("loginUserID", loginUserID);
//            ResponseEntity<StorePartnerListing> result = restTemplate.exchange(builder.toUriString(),
//                    HttpMethod.PATCH, entity, StorePartnerListing.class);
//            log.info("result : " + result.getStatusCode());
//            return result.getBody();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }

    //SEARCH
    public ResponceObject findResponse(FindMatchResult findMatchResult, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "storepartnerlisting/findResponse");
            HttpEntity<?> entity = new HttpEntity<>(findMatchResult, headers);
            ResponseEntity<ResponceObject> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ResponceObject.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //SEARCH
    public MatchResult[] findMatchResponse(MatchResultResponse[] findMatchResult, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "storepartnerlisting/findMatchResultResponse");
            HttpEntity<?> entity = new HttpEntity<>(findMatchResult, headers);
            ResponseEntity<MatchResult[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, MatchResult[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //--------------------------------------------BSEffectiveControl ------------------------------------------------------------------------

    //GET ALL
    public BSEffectiveControl[] getAllBSEffectiveControl(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "bseffectivecontrol");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<BSEffectiveControl[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BSEffectiveControl[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET
    public BSEffectiveControl getBSEffectiveControl(String languageId, Long validationId, String companyId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "bseffectivecontrol/" + validationId)
                            .queryParam("languageId", languageId)
                            .queryParam("companyId", companyId);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<BSEffectiveControl> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BSEffectiveControl.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //CREATE
    public BSEffectiveControl createBSEffectiveControl(BSEffectiveControl newBSEffectiveControl, String loginUserID, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "bseffectivecontrol")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newBSEffectiveControl, headers);

        ResponseEntity<BSEffectiveControl> result =
                getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BSEffectiveControl.class);
        return result.getBody();
    }


    //PATCH
    public BSEffectiveControl updateBsEffectiveControl(String languageId, Long validationId, String companyId,
                                                       String loginUserID, BSEffectiveControl bsEffectiveControl,
                                                       String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MNRClara's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(bsEffectiveControl, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "bseffectivecontrol/" + validationId)
                            .queryParam("languageId", languageId)
                            .queryParam("companyId", companyId)
                            .queryParam("loginUserID", loginUserID);

            ResponseEntity<BSEffectiveControl> result =
                    restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, BSEffectiveControl.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //DELETE
    public boolean deleteBSEffectiveControl(String languageId, String companyId, Long validationId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MNRClara's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "bseffectivecontrol/" + validationId)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("languageId", languageId)
                            .queryParam("companyId", companyId);

            ResponseEntity<String> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //SEARCH
    public BSEffectiveControl[] findBSEffectiveControl(FindBSEffectiveControl findBSEffectiveControl, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "bseffectivecontrol/find");
            HttpEntity<?> entity = new HttpEntity<>(findBSEffectiveControl, headers);
            ResponseEntity<BSEffectiveControl[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BSEffectiveControl[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    //--------------------------------------------BSControllingInterest ------------------------------------------------------------------------

    //GET ALL
    public BSControllingInterest[] getAllBSControllingInterest(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "bscontrollinginterest");
            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<BSControllingInterest[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BSControllingInterest[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET
    public BSControllingInterest getBSControllingInterest(String languageId, Long validationId, String companyId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "bscontrollinginterest/" + validationId)
                            .queryParam("languageId", languageId)
                            .queryParam("companyId", companyId);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<BSControllingInterest> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BSControllingInterest.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //CREATE
    public BSControllingInterest createBSControllingInterest(BSControllingInterest newBSControllingInterest,
                                                             String loginUserID, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "bscontrollinginterest")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newBSControllingInterest, headers);

        ResponseEntity<BSControllingInterest> result =
                getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BSControllingInterest.class);
        //		log.info("result : " + result.getStatusCode());
        return result.getBody();
    }


    //PATCH
    public BSControllingInterest updateBSControllingInterest(String languageId, Long validationId, String companyId,
                                                             String loginUserID, BSControllingInterest bsControllingInterest,
                                                             String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MNRClara's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(bsControllingInterest, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "bscontrollinginterest/" + validationId)
                            .queryParam("languageId", languageId)
                            .queryParam("companyId", companyId)
                            .queryParam("loginUserID", loginUserID);

            ResponseEntity<BSControllingInterest> result =
                    restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, BSControllingInterest.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //DELETE
    public boolean deleteBSControllingInterest(String languageId, String companyId, Long validationId,
                                               String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MNRClara's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "bscontrollinginterest/" + validationId)
                            .queryParam("loginUserID", loginUserID)
                            .queryParam("languageId", languageId)
                            .queryParam("companyId", companyId);
            ResponseEntity<String> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //SEARCH
    public BSControllingInterest[] findBSControllingInterest(FindBSControllingInterest findBSControllingInterest, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "bscontrollinginterest/find");
            HttpEntity<?> entity = new HttpEntity<>(findBSControllingInterest, headers);

            ResponseEntity<BSControllingInterest[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BSControllingInterest[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //----------------------------------------------RequestId-----------------------------------------------------------------
    //GET All REQUEST ID
    public RequestId[] getAllRequestIds(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "requestId");
            ResponseEntity<RequestId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, RequestId[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //GET REQUEST_ID
    public RequestId[] getRequestId(Long requestId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "requestId/" + requestId);
            ResponseEntity<RequestId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, RequestId[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //CREATE REQUEST ID
    public RequestId[] createRequestId(RequestId[] addRequestId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(addRequestId, headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "requestId")
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<RequestId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, RequestId[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //UPDATE REQUEST_ID
    public RequestId[] patchRequestId(Long requestId,String loginUserID, List<RequestId> updateRequestId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(updateRequestId, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "requestId/" + requestId)
                    .queryParam("loginUserID",loginUserID);
            ResponseEntity<RequestId[]> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, RequestId[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //DELETE REQUEST_ID
    public boolean deleteRequestId(Long id,Long requestId, String loginUserID,String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "requestId/" + requestId)
                    .queryParam("id",id)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //FIND REQUEST_ID
    public RequestId[] findRequestId(FindRequestId findRequestId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgTransactionServiceApiUrl() + "requestId/find");
            HttpEntity<?> entity = new HttpEntity<>(findRequestId, headers);
            ResponseEntity<RequestId[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, RequestId[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


}
