package com.iwmvp.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@PropertySource("classpath:application-messages.properties")
public class PropertiesConfig {

	@Value("${iwmvp.oauth.credentials.client_id}")
	private String clientId;
	
	@Value("${iwmvp.oauth.credentials.client_secret_key}")
	private String clientSecretKey;
	
	@Value("${iwmvp.oauth.grant_type}")
	private String grantType;
	
	@Value("${iwmvp.oauth.grant_type.username}")
	private String username;
	
	@Value("${iwmvp.oauth.grant_type.password}")
	private String password;
	
	//-----------------------------------------------------------------------------------

	@Value("${iwmvp.oauth.access_token_url}")
	private String masterAccessTokenUrl;

	//-----------------------------------------------------------------------------------

	@Value("${api.iwmvp.service.url}")
	private String masterServiceUrl;

}
