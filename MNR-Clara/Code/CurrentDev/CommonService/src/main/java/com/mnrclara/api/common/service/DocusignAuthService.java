package com.mnrclara.api.common.service;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import javax.ws.rs.BadRequestException;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.mnrclara.api.common.config.PropertiesConfig;
import com.mnrclara.api.common.model.docusign.AuthToken;
import com.mnrclara.api.common.model.docusign.DocusignToken;
import com.mnrclara.api.common.repository.DocusignTokenRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocusignAuthService {

	@Autowired
	PropertiesConfig propertiesConfig;
	
	@Autowired
	DocusignTokenRepository docusignTokenRepository;
	
	/**
	 * generateOAuthToken
	 * @return
	 */
	public AuthToken generateOAuthToken (String code) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		
		/*
		 * Integration Key (Client ID) - 37ae229e-aeb7-4039-ab97-f6afb4cc7263
		 * Secret Key - 26e6fe90-f1d6-45c5-9f48-e65afd735476
		 */
		String clientId = propertiesConfig.getDocusignClientIid();
		String clientSecretKey = propertiesConfig.getDocusignClientSecretKey();
		
		// Client Id and Client Secret Key to be sent as part of header for authentication
		String credentials = clientId + ":" + clientSecretKey;
		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Basic " + encodedCredentials);
		
//		String accessTokenUrl = "https://account-d.docusign.com/oauth/token";
//				accessTokenUrl += "?grant_type=authorization_code&scope=extended"
//						+ "&code=eyJ0eXAiOiJNVCIsImFsZyI6IlJTMjU2Iiwia2lkIjoiNjgxODVmZjEtNGU1MS00Y2U5LWFmMWMtNjg5ODEyMjAzMzE3In0.AQoAAAABAAYABwAAVgbgtY_ZSAgAAOKMJ7aP2UgCAHasHT6Zf6JFhtw393A8MzMVAAEAAAAYAAEAAAAFAAAADQAkAAAAMzdhZTIyOWUtYWViNy00MDM5LWFiOTctZjZhZmI0Y2M3MjYzIgAkAAAAMzdhZTIyOWUtYWViNy00MDM5LWFiOTctZjZhZmI0Y2M3MjYzNwD0gpXlAEu8SKBFIQ1v_E8LMAAAMoIOso_ZSA.Gt92rgUCX0DacqdGzTZywCWOqCJzOHVBujwwES4NcK4czSqETes-6Q5g9-bzssyCYV2Zh5Zh8gxCpnP3W_6Fwn0khuU8Yw7JGySSzX04nDPt3TBPM3TxvZ9BbFeOdohNH-eWq5Cy-NA4LKSliBGlK5sVGYMcCHqB2FNp2w8vsPzr4Qt6xw2MvPGIVbk9ZFekuu5QJa558w83a-VTYDr-mlRZxbN8b0FarKTnmq15FkVXJi0W219DndCb-J_P4EhFmN-q4E59MRza45EzqzHl2A_yWKjtC1pROVc6UywI9f0CheAWjE08A8sc6HaTkMq2u4xQqVm2DHfhoP_188uHtA";
		
		String accessTokenUrl = propertiesConfig.getDocusignAccessTokenUrl();
		accessTokenUrl += "?grant_type=" + propertiesConfig.getDocusignGrantType() 
							+ "&scope=extended&code=" + code; //propertiesConfig.getDocusignCode();
		log.info("Access token url: " + accessTokenUrl);
		ResponseEntity<AuthToken> responseAuthToken = restTemplate.exchange(accessTokenUrl, HttpMethod.POST, entity, AuthToken.class);
		
		log.info("Access Token Response ---------" + responseAuthToken.getBody());
		AuthToken authToken = responseAuthToken.getBody();
		
		DocusignToken docusign = new DocusignToken();
		docusign.setClientId(clientId);
		docusign.setAuthToken(authToken.getAccess_token());
		docusign.setRefreshToken(authToken.getRefresh_token());
		docusign.setExpiresIn(Long.valueOf(authToken.getExpires_in()));
		docusign.setTokenGeneratedOn(new Date());
		docusignTokenRepository.save(docusign);
		return responseAuthToken.getBody();
	}
	
	/**
	 * generateRefreshToken
	 * @return
	 */
	public AuthToken generateRefreshToken () {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		
		String clientId = propertiesConfig.getDocusignClientIid();
		String clientSecretKey = propertiesConfig.getDocusignClientSecretKey();
		
		String credentials = clientId + ":" + clientSecretKey;
		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
		headers.add("Authorization", "Basic " + encodedCredentials);
		
		Optional<DocusignToken> optDocusign = docusignTokenRepository.findByClientId(clientId);
		if (optDocusign.isEmpty()) {
			throw new BadRequestException("ClientID does not exist.");
		}
		
		DocusignToken docusignToken = optDocusign.get();
		String accessTokenUrl = propertiesConfig.getDocusignAccessTokenUrl();
		accessTokenUrl += "?grant_type=refresh_token"
				+ "&refresh_token=" + docusignToken.getRefreshToken();
		log.info("Access token url: " + accessTokenUrl);
		
		ResponseEntity<AuthToken> responseRefToken = 
				restTemplate.exchange(accessTokenUrl, HttpMethod.POST, entity, AuthToken.class);
		log.info("Access Token Response ---------" + responseRefToken.getBody());
		
		AuthToken authToken = responseRefToken.getBody();
		docusignToken.setAuthToken(authToken.getAccess_token());
		docusignToken.setRefreshToken(authToken.getRefresh_token());
		docusignToken.setExpiresIn(Long.valueOf(authToken.getExpires_in()));
		docusignToken.setUpdatedOn(new Date());
		docusignTokenRepository.save(docusignToken);
		
		return responseRefToken.getBody();
	}
}
