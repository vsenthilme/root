package com.mnrclara.api.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@PropertySource("classpath:application-messages.properties")
public class PropertiesConfig {
	
	//--------------EMAIL-------------------------------------------------------------
	
	@Value("${email.from.address}")
	private String emailFromAddress;
	
	//--------------FILE UPLOAD-------------------------------------------------------------
	
	@Value("${file.upload-dir}")
	private String fileUploadDir;
	
	@Value("${mailmerge.processed.file.path}")
	private String processedFilePath;
	
	@Value("${mailmerge.download.file.path}")
	private String downloadFilePath;
	
	//--------------DOCUSIGN-------------------------------------------------------------

	@Value("${docusign.hostenv}")
	private String docusignHostenv;
	
	@Value("${docusign.basepath}")
	private String docusignBasepath;
	
	@Value("${docusign.api.version}")
	private String docusignApiVersion;
	
	@Value("${docusign.account_id}")
	private String docusignAccountId;
	
	@Value("${docusign.client_id}")
	private String docusignClientIid;
	
	@Value("${docusign.client_secret_key}")
	private String docusignClientSecretKey;
	
	@Value("${docusign.access_token_url}")
	private String docusignAccessTokenUrl;
	
	@Value("${docusign.grant_type}")
	private String docusignGrantType;
	
	//--------------DOCUSIGN-SERVICE-----------------------------------------------------
	@Value("${api.docusign.service.url}")
	private String docusignServiceUrl;
	
	//--------------DOCKETWISE-----------------------------------------------------------
	
	@Value("${docketwise.baseuri}")
	private String docketwiseBaseUri;
	
	@Value("${docketwise.access_token}")
	private String docketwiseAccessToken;
	
	//--------------MOZEO----------------------------------------------------------------
	@Value("${mozeo.prod.url}")
	private String mozeoUrl;
	
	//-----------------------------------------------------------------------------------
	
	/*
	 * doc.storage.base.path=D:\Murugavel.R\Project\7horses\root\MNR-Clara\Code\Project\template
	 * 
	 * doc.storage.lne.base.path=D:/Murugavel.R/Project/7horses/root/MNR-Clara/Code/Project
	 * doc.storage.immig.base.path=D:/Murugavel.R/Project/7horses/root/MNR-Clara/Code/Project
	 * doc.storage.template.path=/template
	 * doc.storage.agreement.path=/agreement
	 * doc.storage.document.path=/document
	 */
	@Value("${doc.storage.base.path}")
	private String docStorageBasePath;
	
	@Value("${doc.storage.template.path}")
	private String docStorageTemplatePath;
	
	@Value("${doc.storage.agreement.path}")
	private String docStorageAgreementPath;
	
	@Value("${doc.storage.document.path}")
	private String docStorageDocumentPath;
	
	@Value("${doc.storage.lne.base.path}")
	private String docStorageLNEBasePath;
	
	@Value("${doc.storage.immig.base.path}")
	private String docStorageImmigBasePath;
}
