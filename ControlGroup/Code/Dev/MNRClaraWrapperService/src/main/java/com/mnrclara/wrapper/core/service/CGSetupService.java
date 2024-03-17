package com.mnrclara.wrapper.core.service;

import com.mnrclara.wrapper.core.config.PropertiesConfig;
import com.mnrclara.wrapper.core.model.auth.AuthToken;
import com.mnrclara.wrapper.core.model.cgsetup.*;
import com.mnrclara.wrapper.core.model.cgtransaction.StoreDropDown;
import com.mnrclara.wrapper.core.model.crm.EMail;
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


@Slf4j
@Service
public class CGSetupService {
    @Autowired
    PropertiesConfig propertiesConfig;

    @Autowired
    private CommonService commonService;

    @Autowired
    AuthTokenService authTokenService;

    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    private String getCgSetupServiceApiUrl() {
        return propertiesConfig.getCgSetupServiceUrl();
    }

    private String getCommonServiceApiUrl(){
        return propertiesConfig.getCommonServiceUrl();
    }

    private String getMnrSetupServiceApiUrl() {
        return propertiesConfig.getMnrSetupServiceUrl();
    }

    //--------------------------------------------LanguageId ------------------------------------------------------------------------
    //GET ALL
    public LanguageId[] getLanguageIds(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "languageid");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<LanguageId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, LanguageId[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET
    public LanguageId getLanguageId(String languageId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "languageid/" + languageId);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<LanguageId> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, LanguageId.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //CREATE
    public LanguageId createLanguageId(LanguageId newLanguageId, String loginUserID, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "languageid")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newLanguageId, headers);
        ResponseEntity<LanguageId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, LanguageId.class);
        //		log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    //PATCH
    public LanguageId updateLanguageId(String languageId, String loginUserID,
                                       LanguageId updateLanguageId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MNRClara's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(updateLanguageId, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "languageid/" + languageId)
                            .queryParam("loginUserID", loginUserID);
            ResponseEntity<LanguageId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, LanguageId.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //DELETE
    public boolean deleteLanguageId(String languageId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MNRClara's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "languageid/" + languageId)
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
    public LanguageId[] findLanguageId(FindLanguageId findLanguageId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "languageid/find");
            HttpEntity<?> entity = new HttpEntity<>(findLanguageId, headers);
            ResponseEntity<LanguageId[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, LanguageId[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    //--------------------------------------------CompanyId------------------------------------------------------------------------
    // GET ALL
    public CompanyId[] getCompanyIds(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "companyid");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<CompanyId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, CompanyId[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET
    public CompanyId getCompanyId(String companyId, String languageId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "companyid/" + companyId)
                            .queryParam("languageId", languageId);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<CompanyId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, CompanyId.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST
    public CompanyId createCompanyId(CompanyId newCompanyId, String loginUserID, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "companyid")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newCompanyId, headers);
        ResponseEntity<CompanyId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, CompanyId.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    // PATCH
    public CompanyId updateCompanyId(String companyId, String languageId, String loginUserID, CompanyId modifiedCompanyId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MNRClara's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(modifiedCompanyId, headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "companyid/" + companyId)
                    .queryParam("languageId", languageId)
                    .queryParam("loginUserID", loginUserID);

            ResponseEntity<CompanyId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, CompanyId.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteCompanyId(String companyId, String languageId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MNRClara's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "companyid/" + companyId)
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
    public CompanyId[] findCompanyId(FindCompanyId findCompanyId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "companyid/find");
            HttpEntity<?> entity = new HttpEntity<>(findCompanyId, headers);
            ResponseEntity<CompanyId[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, CompanyId[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /* ------------------------CITY-----------------------------------------------------------------------------------------*/

    // GET
    public City getCity(String cityId, String companyId, String stateId, String countryId, String languageId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "city/" + cityId)
                    .queryParam("stateId", stateId)
                    .queryParam("companyId", companyId)
                    .queryParam("countryId", countryId)
                    .queryParam("languageId", languageId);
            ResponseEntity<City> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, City.class);
//			log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET ALL
    public City[] getCities(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "city");
            ResponseEntity<City[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, City[].class);
//			log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // CREATE
    public City addCity(City newCity, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(newCity, headers);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "city")
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<City> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, City.class);
//			log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // UPDATE
    public City updateCity(String cityId, String stateId, String countryId, String languageId, String companyId,
                           String loginUserID, City modifiedCity, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(modifiedCity, headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "city/" + cityId)
                    .queryParam("stateId", stateId)
                    .queryParam("countryId", countryId)
                    .queryParam("companyId", companyId)
                    .queryParam("languageId", languageId)
                    .queryParam("loginUserID", loginUserID);

            ResponseEntity<City> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, City.class);
//			log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteCity(String cityId, String stateId, String countryId, String companyId,
                              String languageId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "city/" + cityId)
                    .queryParam("stateId", stateId)
                    .queryParam("countryId", countryId)
                    .queryParam("companyId", companyId)
                    .queryParam("loginUserID", loginUserID)
                    .queryParam("languageId", languageId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
//			log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //SEARCH
    public City[] findCity(FindCity findCity, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "city/find");
            HttpEntity<?> entity = new HttpEntity<>(findCity, headers);
            ResponseEntity<City[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, City[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /* ------------------------Country-----------------------------------------------------------------------------------------*/

    // GET ALL
    public Country[] getCountries(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "country");
            ResponseEntity<Country[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Country[].class);
//			log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET
    public Country getCountry(String countryId, String companyId, String languageId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "country/" + countryId)
                    .queryParam("languageId", languageId)
                    .queryParam("companyId", companyId);
            ResponseEntity<Country> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Country.class);
//			log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // CREATE
    public Country addCountry(Country newCountry, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(newCountry, headers);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "country")
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<Country> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Country.class);
//			log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // UPDATE
    public Country updateCountry(String countryId, String languageId, String loginUserID, String companyId,
                                 Country modifiedCountry, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(modifiedCountry, headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "country/" + countryId)
                    .queryParam("languageId", languageId)
                    .queryParam("companyId", companyId)
                    .queryParam("loginUserID", loginUserID);

            ResponseEntity<Country> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, Country.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteCountry(String countryId, String languageId, String companyId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "country/" + countryId)
                    .queryParam("languageId", languageId)
                    .queryParam("loginUserID", loginUserID)
                    .queryParam("companyId", companyId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //SEARCH
    public Country[] findCountry(FindCountry findCountry, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "country/find");
            HttpEntity<?> entity = new HttpEntity<>(findCountry, headers);
            ResponseEntity<Country[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Country[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    /* ------------------------State-----------------------------------------------------------------------------------------*/

    // GET
    public State getState(String stateId, String companyId, String languageId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "state/" + stateId)
                    .queryParam("companyId", companyId)
                    .queryParam("languageId", languageId);
            ResponseEntity<State> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, State.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET ALL
    public State[] getStates(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "state");
            ResponseEntity<State[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, State[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // CREATE
    public State addState(State newState, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(newState, headers);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "state")
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<State> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, State.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // UPDATE
    public State updateState(String stateId, String languageId, String loginUserID, String companyId,
                             State modifiedState, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(modifiedState, headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "state/" + stateId)
                    .queryParam("languageId", languageId)
                    .queryParam("companyId", companyId)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<State> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, State.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteState(String stateId, String companyId, String languageId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "state/" + stateId)
                    .queryParam("companyId", companyId)
                    .queryParam("loginUserID", loginUserID)
                    .queryParam("languageId", languageId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //SEARCH
    public State[] findState(FindState findState, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "state/find");
            HttpEntity<?> entity = new HttpEntity<>(findState, headers);
            ResponseEntity<State[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, State[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    /* ------------------------StoreId-----------------------------------------------------------------------------------------*/

    // GET
    public StoreId getStoreId(Long storeId, String companyId, String languageId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "store/" + storeId)
                    .queryParam("companyId", companyId)
                    .queryParam("languageId", languageId);
            ResponseEntity<StoreId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StoreId.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET ALL
    public StoreId[] getAllStoreId(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "store");
            ResponseEntity<StoreId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StoreId[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // CREATE
    public StoreId addStoreId(StoreId newStore, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(newStore, headers);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "store")
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<StoreId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StoreId.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // UPDATE
    public StoreId updateStoreId(Long storeId, String languageId, String loginUserID, String companyId,
                                 StoreId modifiedStoreId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(modifiedStoreId, headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "store/" + storeId)
                    .queryParam("languageId", languageId)
                    .queryParam("companyId", companyId)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<StoreId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, StoreId.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteStoreId(Long storeId, String companyId, String languageId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "store/" + storeId)
                    .queryParam("companyId", companyId)
                    .queryParam("loginUserID", loginUserID)
                    .queryParam("languageId", languageId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //SEARCH
    public StoreId[] findStoreId(FindStoreId findStoreId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "store/find");
            HttpEntity<?> entity = new HttpEntity<>(findStoreId, headers);
            ResponseEntity<StoreId[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StoreId[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public StoreDropDown[] getStoreDropDown(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "store/storeDropDown");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<StoreDropDown[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StoreDropDown[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    /* ------------------------ControlGroupType-----------------------------------------------------------------------------------------*/

    // GET
    public ControlGroupType getControlGroupType(Long groupTypeId, String companyId, String languageId,
                                                Long versionNumber, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "controlgrouptype/" + groupTypeId)
                    .queryParam("companyId", companyId)
                    .queryParam("languageId", languageId)
                    .queryParam("versionNumber", versionNumber);
            ResponseEntity<ControlGroupType> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ControlGroupType.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET ALL
    public ControlGroupType[] getAllControlGroupType(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "controlgrouptype");
            ResponseEntity<ControlGroupType[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ControlGroupType[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // CREATE
    public ControlGroupType addControlGroupType(ControlGroupType newControlGroupType, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(newControlGroupType, headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "controlgrouptype")
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<ControlGroupType> result
                    = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ControlGroupType.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // UPDATE
    public ControlGroupType updateControlGroupType(Long groupTypeId, String languageId, String loginUserID, String companyId,
                                                   ControlGroupType modifiedControlGroupType, Long versionNumber, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(modifiedControlGroupType, headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "controlgrouptype/" + groupTypeId)
                    .queryParam("languageId", languageId)
                    .queryParam("companyId", companyId)
                    .queryParam("versionNumber", versionNumber)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<ControlGroupType> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ControlGroupType.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteControlGroupType(Long groupTypeId, String companyId, String languageId, Long versionNumber,
                                          String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "controlgrouptype/" + groupTypeId)
                    .queryParam("companyId", companyId)
                    .queryParam("loginUserID", loginUserID)
                    .queryParam("languageId", languageId)
                    .queryParam("versionNumber", versionNumber);
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
    public ControlGroupType[] findControlGroup(FindControlGroupType findControlGroupType, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "controlgrouptype/find");
            HttpEntity<?> entity = new HttpEntity<>(findControlGroupType, headers);
            ResponseEntity<ControlGroupType[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ControlGroupType[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /* ------------------------SubGroupType-----------------------------------------------------------------------------------------*/

    // GET
    public SubGroupType getSubGroupType(Long subGroupTypeId, Long groupTypeId, String companyId, Long versionNumber,
                                        String languageId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "subgrouptype/" + subGroupTypeId)
                    .queryParam("companyId", companyId)
                    .queryParam("groupTypeId", groupTypeId)
                    .queryParam("languageId", languageId)
                    .queryParam("versionNumber", versionNumber);
            ResponseEntity<SubGroupType> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, SubGroupType.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET ALL
    public SubGroupType[] getAllSubGroupType(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "subgrouptype");
            ResponseEntity<SubGroupType[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, SubGroupType[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // CREATE
    public SubGroupType addSubGroupType(SubGroupType newSubGroupType, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(newSubGroupType, headers);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "subgrouptype")
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<SubGroupType> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, SubGroupType.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // UPDATE
    public SubGroupType updateSubGroup(Long groupTypeId, Long subGroupTypeId, String languageId, String loginUserID,
                                       String companyId, Long versionNumber, SubGroupType modifiedSubGroupType, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(modifiedSubGroupType, headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "subgrouptype/" + subGroupTypeId)
                    .queryParam("languageId", languageId)
                    .queryParam("groupTypeId", groupTypeId)
                    .queryParam("companyId", companyId)
                    .queryParam("versionNumber", versionNumber)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<SubGroupType> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, SubGroupType.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteSubGroup(Long subGroupTypeId, Long groupTypeId, String loginUserID, Long versionNumber,
                                  String companyId, String languageId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "subgrouptype/" + subGroupTypeId)
                    .queryParam("companyId", companyId)
                    .queryParam("groupTypeId", groupTypeId)
                    .queryParam("versionNumber", versionNumber)
                    .queryParam("loginUserID", loginUserID)
                    .queryParam("languageId", languageId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    //SEARCH
    public SubGroupType[] findSubGroupType(FindSubGroupType findSubGroupType, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "subgrouptype/find");
            HttpEntity<?> entity = new HttpEntity<>(findSubGroupType, headers);
            ResponseEntity<SubGroupType[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, SubGroupType[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // MASTER

    /* ------------------------Client-----------------------------------------------------------------------------------------*/

    // GET
    public Client getClientId(Long clientId, String companyId, String languageId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "client/" + clientId)
                    .queryParam("companyId", companyId)
                    .queryParam("languageId", languageId);
            ResponseEntity<Client> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Client.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET ALL
    public Client[] getAllClientId(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "client");
            ResponseEntity<Client[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Client[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // CREATE
    public Client addClient(Client newClient, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(newClient, headers);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "client")
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<Client> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Client.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // UPDATE
    public Client updateClient(Long clientId, String languageId, String loginUserID, String companyId,
                               Client modifiedClient, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(modifiedClient, headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "client/" + clientId)
                    .queryParam("languageId", languageId)
                    .queryParam("companyId", companyId)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<Client> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, Client.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteClient(Long clientId, String loginUserID, String companyId, String languageId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "client/" + clientId)
                    .queryParam("companyId", companyId)
                    .queryParam("loginUserID", loginUserID)
                    .queryParam("languageId", languageId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    //SEARCH
    public Client[] findClient(FindClient findClient, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "client/find");
            HttpEntity<?> entity = new HttpEntity<>(findClient, headers);
            ResponseEntity<Client[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Client[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /* ------------------------ControlGroup-----------------------------------------------------------------------------------------*/

    // GET
    public ControlGroup getControlGroup(Long groupId, Long groupTypeId, String companyId, String languageId,
                                        Long versionNumber, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "controlgroup/" + groupId)
                    .queryParam("companyId", companyId)
                    .queryParam("groupTypeId", groupTypeId)
                    .queryParam("languageId", languageId)
                    .queryParam("versionNumber", versionNumber);
            ResponseEntity<ControlGroup> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ControlGroup.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET ALL
    public ControlGroup[] getAllControlGroup(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "controlgroup");
            ResponseEntity<ControlGroup[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ControlGroup[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // CREATE
    public ControlGroup addControlGroup(ControlGroup newControlGroup, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(newControlGroup, headers);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "controlgroup")
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<ControlGroup> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ControlGroup.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // UPDATE
    public ControlGroup updateControlgroup(Long groupId, Long groupTypeId, String languageId, String loginUserID, String companyId,
                                           Long versionNumber, ControlGroup modifiedControlGroup, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(modifiedControlGroup, headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "controlgroup/" + groupId)
                    .queryParam("languageId", languageId)
                    .queryParam("companyId", companyId)
                    .queryParam("groupTypeId", groupTypeId)
                    .queryParam("versionNumber", versionNumber)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<ControlGroup> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ControlGroup.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteControlGroup(Long groupId, Long groupTypeId, String loginUserID, Long versionNumber,
                                      String companyId, String languageId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "controlgroup/" + groupId)
                    .queryParam("companyId", companyId)
                    .queryParam("loginUserID", loginUserID)
                    .queryParam("groupTypeId", groupTypeId)
                    .queryParam("versionNumber", versionNumber)
                    .queryParam("languageId", languageId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    //SEARCH
    public ControlGroup[] findControlGroup(FindControlGroup findControlGroup, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "controlgroup/find");
            HttpEntity<?> entity = new HttpEntity<>(findControlGroup, headers);
            ResponseEntity<ControlGroup[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ControlGroup[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /* ------------------------ClientControlGroup-----------------------------------------------------------------------------------------*/

    // GET
    public ClientControlGroup getClientControlGroup(Long groupTypeId, Long clientId, String companyId,
                                                    String languageId, Long versionNumber, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "clientcontrolgroup/" + clientId)
                    .queryParam("companyId", companyId)
                    .queryParam("groupTypeId", groupTypeId)
                    .queryParam("versionNumber", versionNumber)
                    .queryParam("languageId", languageId);
            ResponseEntity<ClientControlGroup> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ClientControlGroup.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET ALL
    public ClientControlGroup[] getAllClientControlGroup(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "clientcontrolgroup");
            ResponseEntity<ClientControlGroup[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.GET, entity, ClientControlGroup[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // CREATE
    public ClientControlGroup addClientControlGroup(ClientControlGroup newClientControlGroup, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(newClientControlGroup, headers);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "clientcontrolgroup")
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<ClientControlGroup> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ClientControlGroup.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // UPDATE
    public ClientControlGroup updateClientControlGroup(Long groupTypeId, Long clientId, String languageId,
                                                       String loginUserID, String companyId, ClientControlGroup modifiedClientControlGroup,
                                                       Long versionNumber, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(modifiedClientControlGroup, headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "clientcontrolgroup/" + clientId)
                    .queryParam("languageId", languageId)
                    .queryParam("companyId", companyId)
                    .queryParam("groupTypeId", groupTypeId)
                    .queryParam("versionNumber", versionNumber)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<ClientControlGroup> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ClientControlGroup.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteClientControlGroup(Long groupTypeId, Long clientId, String loginUserID,
                                            String companyId, String languageId, Long versionNumber, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "clientcontrolgroup/" + clientId)
                    .queryParam("companyId", companyId)
                    .queryParam("loginUserID", loginUserID)
                    .queryParam("groupTypeId", groupTypeId)
                    .queryParam("versionNumber", versionNumber)
                    .queryParam("languageId", languageId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    //SEARCH
    public ClientControlGroup[] findClientControlGroup(FindClientControlGroup findControlGroup, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "clientcontrolgroup/find");
            HttpEntity<?> entity = new HttpEntity<>(findControlGroup, headers);
            ResponseEntity<ClientControlGroup[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ClientControlGroup[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }



    /* ------------------------RelationShipId-----------------------------------------------------------------------------------------*/

    // GET
    public RelationShipId getRelationShipId(String companyId, String languageId, Long relationShipId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "relationshipid/" + relationShipId)
                    .queryParam("companyId", companyId)
                    .queryParam("languageId", languageId);
            ResponseEntity<RelationShipId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, RelationShipId.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET ALL
    public RelationShipId[] getAllRelationShipId(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "relationshipid");
            ResponseEntity<RelationShipId[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.GET, entity, RelationShipId[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // CREATE
    public RelationShipId addRelationShipId(RelationShipId newRelationShipId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(newRelationShipId, headers);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "relationshipid")
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<RelationShipId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, RelationShipId.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // UPDATE
    public RelationShipId updateRelationShipId(Long relationShipId, String languageId, String loginUserID, String companyId,
                                               RelationShipId modifiedRelationShipId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(modifiedRelationShipId, headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "relationshipid/" + relationShipId)
                    .queryParam("languageId", languageId)
                    .queryParam("companyId", companyId)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<RelationShipId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, RelationShipId.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteRelationShipId(Long relationShipId, String companyId, String languageId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "relationshipid/" + relationShipId)
                    .queryParam("companyId", companyId)
                    .queryParam("loginUserID", loginUserID)
                    .queryParam("languageId", languageId);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    //SEARCH
    public RelationShipId[] findRelationShipId(FindRelationShipId findRelationShipId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "relationshipid/find");
            HttpEntity<?> entity = new HttpEntity<>(findRelationShipId, headers);
            ResponseEntity<RelationShipId[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, RelationShipId[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //----------------------------------------------------ClientStore----------------------------------------------------------------
    //GET ALL ClientStore
    public ClientStore[] getAllClientStore(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "clientstore");
            ResponseEntity<ClientStore[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ClientStore[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Get ClientStore
    public ClientStore getClientStore(Long clientId, Long storeId, String companyId, String languageId,
                                      Long versionNumber, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "clientstore/" + clientId)
                    .queryParam("storeId", storeId)
                    .queryParam("companyId", companyId)
                    .queryParam("languageId", languageId)
                    .queryParam("versionNumber", versionNumber);
            ResponseEntity<ClientStore> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ClientStore.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //CREATE ClientStore
    public ClientStore addClientStore(ClientStore newClientStore, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(newClientStore, headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "clientstore")
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<ClientStore> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ClientStore.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //UPDATE ClientStore
    public ClientStore updateClientStore(Long clientId, Long storeId, String companyId, String languageId, Long versionNumber,
                                         String loginUserID, ClientStore modifiedClientStore, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(modifiedClientStore, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "clientstore/" + clientId)
                    .queryParam("storeId", storeId)
                    .queryParam("companyId", companyId)
                    .queryParam("languageId", languageId)
                    .queryParam("loginUserID", loginUserID)
                    .queryParam("versionNumber", versionNumber);
            ResponseEntity<ClientStore> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ClientStore.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //DELETE ClientStore
    public boolean deleteClientStore(Long clientId, Long storeId, String companyId, String languageId,
                                     Long versionNumber, String authToken, String loginUserID) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "clientstore/" + clientId)
                    .queryParam("storeId", storeId)
                    .queryParam("companyId", companyId)
                    .queryParam("languageId", languageId)
                    .queryParam("versionNumber", versionNumber)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //FIND ClientStore
    public ClientStore[] findClientStore(FindClientStore findClientStore, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "clientstore/find");
            HttpEntity<?> entity = new HttpEntity<>(findClientStore, headers);
            ResponseEntity<ClientStore[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ClientStore[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    // GET
    public String getNextNumberRange (Long numberRangeCode,String numberRangeObject,String languageId,String companyId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MNRClara RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "/numberRange/nextNumberRange")
                    .queryParam("numberRangeCode", numberRangeCode)
                    .queryParam("numberRangeObject", numberRangeObject)
                    .queryParam("languageId", languageId)
                    .queryParam("companyId",companyId);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //----------------------------------------------------Status----------------------------------------------------------------

    //Get StatusId
    public StatusId getStatusId(Long statusId, String languageId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "status/" + statusId)
                    .queryParam("languageId", languageId);
            ResponseEntity<StatusId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StatusId.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    //GET ALL StatusId
    public StatusId[] getAllStatusId(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "status");
            ResponseEntity<StatusId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StatusId[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //CREATE StatusId
    public StatusId addStatusId(StatusId newStatusId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(newStatusId, headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "status")
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<StatusId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StatusId.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //UPDATE StatusId
    public StatusId updateStatusId(String languageId, Long statusId, String loginUserID,
                                   StatusId modifiedStatusId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(modifiedStatusId, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "status/" + statusId)
                    .queryParam("languageId", languageId)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<StatusId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, StatusId.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //DELETE StatusId
    public boolean deleteStatusId(String languageId, Long statusId, String authToken, String loginUserID) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "status/" + statusId)
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

    //FIND StatusId
    public StatusId[] findStatusId(FindStatus findStatus, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "status/findStatus");
            HttpEntity<?> entity = new HttpEntity<>(findStatus, headers);
            ResponseEntity<StatusId[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StatusId[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    //----------------------------------------------------NumberRange----------------------------------------------------------------

    //Get NumberRange
    public NumberRangeId getNumberRange(String companyId, String languageId, Long numberRangeCode, String numberRangeObject, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "numberRange/" + numberRangeCode)
                    .queryParam("languageId", languageId)
                    .queryParam("companyId", companyId)
                    .queryParam("numberRangeObject", numberRangeObject);
            ResponseEntity<NumberRangeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, NumberRangeId.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    //GET ALL StatusId
    public NumberRangeId[] getAllNumberRange(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "numberRange");
            ResponseEntity<NumberRangeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, NumberRangeId[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //CREATE NumberRange
    public NumberRangeId addNumberRange(NumberRangeId newNumberRangeId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(newNumberRangeId, headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "numberRange")
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<NumberRangeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, NumberRangeId.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //UPDATE NumberRange
    public NumberRangeId updateNumberRange(String languageId, String companyId, Long numberRangeCode, String numberRangeObject,
                                           String loginUserID, NumberRangeId numberRangeId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(numberRangeId, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "numberRange/" + numberRangeCode)
                    .queryParam("languageId", languageId)
                    .queryParam("companyId", companyId)
                    .queryParam("numberRangeObject", numberRangeObject)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<NumberRangeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, NumberRangeId.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //DELETE NumberRange
    public boolean deleteNumberRange(String languageId, String companyId, Long numberRangeCode, String numberRangeObject,
                                     String authToken, String loginUserID) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "numberRange/" + numberRangeCode)
                    .queryParam("languageId", languageId)
                    .queryParam("companyId", companyId)
                    .queryParam("numberRangeObject", numberRangeObject)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //FIND NumberRange
    public NumberRangeId[] findNumberRange(FindNumberRange findNumberRange, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "numberRange/findNumberRange");
            HttpEntity<?> entity = new HttpEntity<>(findNumberRange, headers);
            ResponseEntity<NumberRangeId[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, NumberRangeId[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    //----------------------------------------------CgEntity-----------------------------------------------------------------
    //GET All ENTITIES
    public CgEntity[] getAllCgEntities(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "cgentity");
            ResponseEntity<CgEntity[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, CgEntity[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //GET ENTITY
    public CgEntity getCgEntity(Long entityId, Long clientId, String companyId, String languageId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "cgentity/" + entityId)
                    .queryParam("clientId", clientId)
                    .queryParam("companyId", companyId)
                    .queryParam("languageId", languageId);
            ResponseEntity<CgEntity> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, CgEntity.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //CREATE ENTITY
    public CgEntity createCgEntity(CgEntity addCgEntity, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(addCgEntity, headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "cgentity")
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<CgEntity> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, CgEntity.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //UPDATE ENTITY
    public CgEntity patchCgEntity(Long entityId, Long clientId, String companyId, String languageId,
                                  String loginUserID, CgEntity updateCgEntity, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(updateCgEntity, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "cgentity/" + entityId)
                    .queryParam("clientId", clientId)
                    .queryParam("companyId", companyId)
                    .queryParam("languageId", languageId)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<CgEntity> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, CgEntity.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //DELETE ENTITY
    public boolean deleteCgEntity(Long entityId, Long clientId, String companyId, String languageId,
                                  String authToken, String loginUserID) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Classic WMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "cgentity/" + entityId)
                    .queryParam("clientId", clientId)
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

    //FIND ENTITY
    public CgEntity[] findCgEntity(FindCgEntity findCgEntity, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCgSetupServiceApiUrl() + "cgentity/find");
            HttpEntity<?> entity = new HttpEntity<>(findCgEntity, headers);
            ResponseEntity<CgEntity[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, CgEntity[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     *
     * @param email
     */
    public void sendFormThroEmail(EMail email) {
        // Send Email
        // Get AuthToken for SetupService
        AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
        commonService.sendEmail(email, authTokenForCommonService.getAccess_token());
    }

    public void sendEmailWithAttachment(EMail email,String file) {
        // Send Email
        // Get AuthToken for SetupService
        AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
        commonService.sendEmailWithAttachment(email, file, authTokenForCommonService.getAccess_token());
    }
}
