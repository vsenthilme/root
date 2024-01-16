package com.mnrclara.api.accounting.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@PropertySource("classpath:application-messages.properties")
public class PropertiesConfig {
	
	@Value("${setup.oauth.credentials.client_id}")
	private String clientId;
	
	@Value("${setup.oauth.credentials.client_secret_key}")
	private String clientSecretKey;
	
	@Value("${setup.oauth.grant_type}")
	private String grantType;
	
	@Value("${setup.oauth.grant_type.username}")
	private String username;
	
	@Value("${setup.oauth.grant_type.password}")
	private String password;

	@Value("${api.crm.service.url}")
	private String crmServiceUrl;
	
	//-----------------------------------------------------------------------------------
	
	//--------------EMAIL-------------------------------------------------------------
	
	@Value("${email.from.address}")
	private String emailFromAddress;
	
	@Value("${setup.oauth.access_token_url}")
	private String setupAccessTokenUrl;
	
	@Value("${common.oauth.access_token_url}")
	private String commonAccessTokenUrl;
	
	@Value("${management.oauth.access_token_url}")
	private String managementAccessTokenUrl;

	@Value("${crm.oauth.access_token_url}")
	private String crmAccessTokenUrl;
	
	//-----------------------------------------------------------------------------------
	
	@Value("${api.setup.service.url}")
	private String setupServiceUrl;
	
	@Value("${api.common.service.url}")
	private String commonServiceUrl;
	
	@Value("${api.management.service.url}")
	private String managementServiceUrl;
}
