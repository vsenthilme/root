package com.mnrclara.api.common.service;

import com.mnrclara.api.common.config.PropertiesConfig;
import com.mnrclara.api.common.model.auth.AuthToken;
import com.mnrclara.api.common.model.auth.AuthTokenRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Slf4j
@Service
public class AuthTokenService {

    @Autowired
    PropertiesConfig propertiesConfig;

    /**
     * Returns RestTemplate Object
     *
     * @return
     */
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // Object Convertor
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter
                .setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
        return restTemplate;
    }

    private AuthToken generateOAuthToken(String apiUrl, String clientId, String clientSecretKey, String grantType,
                                         String oauthUserName, String oauthPassword) {
        // Client Id and Client Secret Key to be sent as part of header for
        // authentication
        String credentials = clientId + ":" + clientSecretKey;
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Basic " + encodedCredentials);

        HttpEntity<String> request = new HttpEntity<String>(headers);
        String accessTokenUrl = apiUrl;

        // AuthToken URL dynamically
//        if (apiUrl.equalsIgnoreCase("mnr-setup-service")) {
//            accessTokenUrl = propertiesConfig.getSetupAccessTokenUrl();
//        } else if (apiUrl.equalsIgnoreCase("mnr-crm-service")) {
//            accessTokenUrl = propertiesConfig.getCrmAccessTokenUrl();
//        } else if (apiUrl.equalsIgnoreCase("mnr-common-service")) {
//            accessTokenUrl = propertiesConfig.getCommonAccessTokenUrl();
//        } else if (apiUrl.equalsIgnoreCase("mnr-management-service")) {
//            accessTokenUrl = propertiesConfig.getManagementAccessTokenUrl();
//        } else if (apiUrl.equalsIgnoreCase("mnr-accounting-service")) {
//            accessTokenUrl = propertiesConfig.getAccountingAccessTokenUrl();
//        } else if (apiUrl.equalsIgnoreCase("mnr-cg-setup-service")) {
//            accessTokenUrl = propertiesConfig.getCgSetupAccessTokenUrl();
//        } else if (apiUrl.equalsIgnoreCase("mnr-cg-transaction-service")) {
//            accessTokenUrl = propertiesConfig.getCgTransactionAccessTokenUrl();
//            log.info("accessTokenUrl Acc::---------> : " + accessTokenUrl);
//        } else {
//            log.info("The given URL is not available. Quiting.");
//            throw new BadRequestException("The given URL is not available. Quiting");
//        }

        log.info("Access token url: " + accessTokenUrl);
        accessTokenUrl += "?grant_type=" + grantType + "&username=" + oauthUserName + "&password=" + oauthPassword;
        log.info("accessTokenUrl : " + accessTokenUrl);

        ResponseEntity<AuthToken> response = restTemplate.exchange(accessTokenUrl, HttpMethod.POST, request,
                AuthToken.class);
        log.info("Access Token Response ---------" + response.getBody());
        return response.getBody();
    }

    //--------------------SharePoint-Token----------------------------------------------------------

    /**
     * @return
     */
    public AuthToken getSharePointOAuthToken() {
        // Client Id and Client Secret Key to be sent as part of header for authentication
		/*
		 * 	sp.tenant.id=0b198ef5-3ea1-4bc9-91a6-a6874d0dee1e
			sp.client.id=46650881-943c-4904-a1c6-9150788b49ae
			sp.client.secret=iiz8Q~O_noWHZKa-5fI4DUlvwbZjLlX-4eGwCaj1
			sp.grant_type=client_credentials
			sp.scope=https://graph.microsoft.com/.default
			sp.token.url=https://login.microsoftonline.com/0b198ef5-3ea1-4bc9-91a6-a6874d0dee1e/oauth2/v2.0/token
		 */
        String clientId = propertiesConfig.getSpClientId();
        String clientSecretKey = propertiesConfig.getSpClientSecret();
        String grantType = propertiesConfig.getSpGrantType();
        String scope = propertiesConfig.getSpScope();
        String accessTokenUrl = propertiesConfig.getSpTokenUrl();
        log.info("accessTokenUrl : " + accessTokenUrl);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("grant_type", grantType);
        map.add("client_id", clientId);
        map.add("client_secret", clientSecretKey);
        map.add("scope", scope);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<AuthToken> response = restTemplate.exchange(accessTokenUrl, HttpMethod.POST, request, AuthToken.class);
        log.info("Access Token Response ---------" + response.getBody());
        return response.getBody();
    }

    public AuthToken getAuthToken(AuthTokenRequest authTokenRequest) {
        return generateOAuthToken(authTokenRequest.getApiName(), authTokenRequest.getClientId(),
                authTokenRequest.getClientSecretKey(), authTokenRequest.getGrantType(),
                authTokenRequest.getOauthUserName(), authTokenRequest.getOauthPassword());
    }


}
