package com.mnrclara.api.management.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@PropertySource(value = "application-messages.properties", ignoreResourceNotFound = true)
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

	// -----------------------------------------------------------------------------------

	@Value("${setup.oauth.access_token_url}")
	private String setupAccessTokenUrl;

	@Value("${common.oauth.access_token_url}")
	private String commonAccessTokenUrl;

	@Value("${crm.oauth.access_token_url}")
	private String crmAccessTokenUrl;

	// -----------------------------------------------------------------------------------

	@Value("${api.setup.service.url}")
	private String setupServiceUrl;

	@Value("${api.common.service.url}")
	private String commonServiceUrl;

	@Value("${api.crm.service.url}")
	private String crmServiceUrl;
	
	// -----------------------------------------------------------------------------------
	@Value("${task.email.from.address}")
	private String taskEmailFromAddress;
	
	/*
	 * doc.storage.base.path=D:\Murugavel.R\Project\7horses\root\MNR-Clara\Code\Project
	 * doc.storage.template.path=/template
	 * doc.storage.document.path=\document
	 */
	@Value("${doc.storage.base.path}")
	private String docStorageBasePath;
	
	@Value("${doc.storage.lne.base.path}")
	private String docStorageLNEPath;
	
	@Value("${doc.storage.immig.base.path}")
	private String docStorageImmigrationPath;
	
	@Value("${doc.storage.template.path}")
	private String docStorageTemplatePath;
	
	@Value("${doc.storage.document.path}")
	private String docStorageDocumentPath;
	
	/*--------------------Docketwise-----------------------------------------------------*/
	@Value("${docketwise.store.flag}")
	private String docketwiseStoreFlag;
	
	//-----------------------------------------------------------------------------------
	@Value("${login.email.from.address}")
	private String loginEmailFromAddress;
}
