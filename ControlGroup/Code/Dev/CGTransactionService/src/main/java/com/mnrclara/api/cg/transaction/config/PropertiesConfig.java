package com.mnrclara.api.cg.transaction.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@PropertySource("classpath:application-messages.properties")
public class PropertiesConfig {

	@Value("${common.oauth.credentials.client_id}")
	private String clientId;
	
	@Value("${common.oauth.credentials.client_secret_key}")
	private String clientSecretKey;
	
	@Value("${common.oauth.grant_type}")
	private String grantType;
	
	@Value("${common.oauth.grant_type.username}")
	private String username;
	
	@Value("${common.oauth.grant_type.password}")
	private String password;


	//--------------------------------------------------------------------------------------
	@Value("${file.upload-dir}")
	private String fileUploadDir;

	@Value("${file.moveto-dir}")
	private String fileMoveToDir;

	@Value("${file.storePartnerListing}")
	private String storePartnerListingFile;
	//--------------------------------------------------------------------------------------
	@Value("${api.cg.setup.service.url}")
	private String cgSetupServiceUrl;

	@Value("${cg.setup.oauth.access_token_url}")
	private String cgSetupAccessTokenUrl;

}
