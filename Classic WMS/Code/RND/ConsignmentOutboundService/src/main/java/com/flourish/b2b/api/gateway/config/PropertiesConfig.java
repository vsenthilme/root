package com.flourish.b2b.api.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@PropertySource("classpath:application-messages.properties")
public class PropertiesConfig {

	/*
	 * oauth.credentials.client_id=pixeltrice
	 * oauth.credentials.client_secret_key=pixeltrice-secret-key
	 * oauth.access_token_url=http://localhost:8085/oauth/token
	 * oauth.grant_type=password
	 * oauth.grant_type.username=muru
	 * oauth.grant_type.password=welcome
	 */
			
	@Value("${oauth.credentials.client_id}")
	private String clientId;
	
	@Value("${oauth.credentials.client_secret_key}")
	private String clientSecretKey;
	
	@Value("${oauth.access_token_url}")
	private String accessTokenUrl;
	
	@Value("${oauth.grant_type}")
	private String grantType;
	
	@Value("${oauth.grant_type.username}")
	private String username;
	
	@Value("${oauth.grant_type.password}")
	private String password;
	
	@Value("${serviceProvider.simulator.url}")
	private String spSimulatorUrl;
}
