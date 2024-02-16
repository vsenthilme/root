package com.mnrclara.api.crm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@PropertySource(value = "application-messages.properties", ignoreResourceNotFound = true)
public class PropertiesConfig {

	/*
	 * setup.oauth.credentials.client_id=pixeltrice
	 * setup.oauth.credentials.client_secret_key=pixeltrice-secret-key
	 * setup.oauth.access_token_url=http://localhost:8082/mnrclara/api-dev/wms-setup-service/oauth/token
	 * setup.oauth.grant_type=password
	 * setup.oauth.grant_type.username=muru
	 * setup.oauth.grant_type.password=welcome
	 * 
	 * #NR Clara RestService URL
	 * api.setup.service.url=http://localhost:8084/mnrclara/api-dev/wms-setup-service/
	 * api.common.service.url=http://localhost:8085/mnrclara/api-dev/wms-common-service/

	 */
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
	
	//-----------------------------------------------------------------------------------
	
	@Value("${setup.oauth.access_token_url}")
	private String setupAccessTokenUrl;
	
	@Value("${common.oauth.access_token_url}")
	private String commonAccessTokenUrl;
	
	@Value("${management.oauth.access_token_url}")
	private String managementAccessTokenUrl;
	
	@Value("${accounting.oauth.access_token_url}")
	private String accountingAccessTokenUrl;
	
	//-----------------------------------------------------------------------------------
	
	@Value("${api.setup.service.url}")
	private String setupServiceUrl;
	
	@Value("${api.common.service.url}")
	private String commonServiceUrl;
	
	@Value("${api.management.service.url}")
	private String managementServiceUrl;
	
	@Value("${api.accounting.service.url}")
	private String accountingServiceUrl;
	
	//--------------------------------------------------------------------------------------------
	
	/*
	 * doc.storage.base.path=D:\Murugavel.R\Project\7horses\root\MNR-Clara\Code\Project\template
	 * doc.storage.lne.base.path=/home/admini/mrclara/desktop/ydrive/clara
	 * doc.storage.immig.base.path=/home/admini/mrclara/desktop/winfiles/clara
	 * doc.storage.template.path=/template
	 * doc.storage.agreement.path=/agreement
	 * doc.storage.document.path=/document
	 */
	@Value("${doc.storage.base.path}")
	private String docStorageBasePath;
	
	@Value("${doc.storage.lne.base.path}")
	private String docStorageLNEPath;
	
	@Value("${doc.storage.immig.base.path}")
	private String docStorageImmigrationPath;
	
	@Value("${doc.storage.template.path}")
	private String docStorageTemplatePath;
	
	@Value("${doc.storage.agreement.path}")
	private String docStorageAgreementPath;
	
	@Value("${doc.storage.document.path}")
	private String docStorageDocumentPath;
}
