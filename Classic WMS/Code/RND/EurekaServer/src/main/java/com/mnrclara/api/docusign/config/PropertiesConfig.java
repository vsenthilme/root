package com.mnrclara.api.docusign.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@PropertySource("classpath:application-messages.properties")
public class PropertiesConfig {
	
	//-------------------------------------------------------------------------------------
	
//	@Value("${mailmerge.processed.file.path}")
//	private String processedFilePath;
//	
	@Value("${mailmerge.download.file.path}")
	private String downloadFilePath;
	
	//-------------------------------------------------------------------------------------

	@Value("${docusign.basepath}")
	private String docusignBasepath;
	
	@Value("${docusign.client_id}")
	private String docusignClientIid;
	
	@Value("${docusign.account_id}")
	private String docusignAccountId;
	
	@Value("${docusign.client_secret_key}")
	private String docusignClientSecretKey;
	
	@Value("${docusign.access_token_url}")
	private String docusignAccessTokenUrl;
	
	@Value("${docusign.grant_type}")
	private String docusignGrantType;
	
	@Value("${docusign.api.version}")
	private String docusignApiVersion;
	
	//---------------DOC-STORAGE------------------------------------------------------------------------
	
	/*
	 * doc.storage.base.path=D:\Murugavel.R\Project\7horses\root\MNR-Clara\Code\Project\template
	 * doc.storage.agreement.path=/agreement
	 * doc.storage.document.path=/document
	 */
	@Value("${doc.storage.base.path}")
	private String docStorageBasePath;
	
	@Value("${doc.storage.agreement.path}")
	private String docStorageAgreementPath;
	
	@Value("${doc.storage.document.path}")
	private String docStorageDocumentPath;
	
}
