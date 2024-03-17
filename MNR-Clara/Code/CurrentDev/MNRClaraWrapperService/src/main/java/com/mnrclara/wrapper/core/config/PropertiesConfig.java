package com.mnrclara.wrapper.core.config;

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
	
	@Value("${accounting.oauth.access_token_url}")
	private String accountingAccessTokenUrl;
	
	@Value("${management.oauth.access_token_url}")
	private String managementAccessTokenUrl;
	
	@Value("${crm.oauth.access_token_url}")
	private String crmAccessTokenUrl;
	
	@Value("${setup.oauth.access_token_url}")
	private String setupAccessTokenUrl;

	@Value("${common.oauth.access_token_url}")
	private String commonAccessTokenUrl;
	
	//-----------------------------------------------------------------------------------
	@Value("${api.accounting.service.url}")
	private String accountingServiceUrl;
	
	@Value("${api.management.service.url}")
	private String managementServiceUrl;
	
	@Value("${api.crm.service.url}")
	private String crmServiceUrl;
	
	@Value("${api.setup.service.url}")
	private String setupServiceUrl;

	@Value("${api.spark.service.url}")
	private String sparkServiceUrl;
	/*
	 * doc.storage.base.path=D:\Murugavel.R\Project\7horses\root\MNR-Clara\Code\Project
	 * doc.storage.template.path=/template
	 * doc.storage.agreement.path=\agreement
	 * doc.storage.document.path=\document
	 */
	@Value("${doc.storage.base.path}")
	private String docStorageBasePath;
	
	@Value("${doc.storage.lne.base.path}")
	private String docStorageLNEBasePath;
	
	@Value("${doc.storage.immig.base.path}")
	private String docStorageImmigBasePath;
	
	@Value("${doc.storage.template.path}")
	private String docStorageTemplatePath;
	
	@Value("${doc.storage.agreement.path}")
	private String docStorageAgreementPath;
	
	@Value("${doc.storage.document.path}")
	private String docStorageDocumentPath;
	
	@Value("${doc.storage.receipt.path}")
	private String docStorageReceiptPath;

	@Value("${doc.storage.check.path}")
	private String docStorageCheckPath;

	//-----------------------------------------------------------------------------------
	@Value("${login.email.from.address}")
	private String loginEmailFromAddress;

	@Value("${login.email.check.address}")
	private String loginEmailCheckAddress;

	//-----------------------------------------------------------------------------------
	@Value("${login.email.to.address}")
	private String loginEmailToAddress;

	@Value("${login.email.cc.address}")
	private String loginEmailCcAddress;

	//-----------------------------------------------------------------------------------
	@Value("${api.common.service.url}")
	private String commonServiceUrl;
	
	//---------------Batch Upload---------------------------------------------------------
	@Value("${batchfile.upload-dir}")
	private String batchFileUploadDir;
	
	@Value("${file.upload-dir}")
	private String fileUploadDir;
	
	@Value("${file.moveto-dir}")
	private String fileMoveToDir;
	
//	file.invoiceHeader=/invoiceHeader.csv
//	file.invoiceLine=/invoiceLine.csv
//	file.paymentPlanHeader=/paymentPlanHeader.csv
//	file.paymentPlanLine=/paymentPlanLine.csv
//	file.paymentUpdate=/paymentUpdate.csv
//	file.clientGeneral=/clientGeneral.csv
//	file.clientNote=/clientNote.csv
//	file.matterAssignment=/matterAssignment.csv
//	file.matterExpense=/matterExpense.csv
//	file.matterGenAcc=/matterGenAcc.csv
//	file.matterNote=/matterNote.csv
//	file.matterRate=/matterRate.csv
//	file.matterTimeTicket=/matterTimeTicket.csv
	
	@Value("${file.clientGeneral}")
	private String clientGeneralFile;
	
	@Value("${file.clientNote}")
	private String clientNoteFile;
	
	@Value("${file.matterAssignment}")
	private String matterAssignmentFile;
	
	@Value("${file.matterExpense}")
	private String matterExpenseFile;
	
	@Value("${file.matterGenAcc}")
	private String matterGenAccFile;
	
	@Value("${file.matterNote}")
	private String matterNoteFile;
	
	@Value("${file.matterRate}")
	private String matterRateFile;
	
	@Value("${file.matterTimeTicket}")
	private String matterTimeTicketFile;
	
	@Value("${file.invoiceHeader}")
	private String invoiceHeaderFile;
	
	@Value("${file.invoiceLine}")
	private String invoiceLineFile;
	
	@Value("${file.paymentPlanHeader}")
	private String paymentPlanHeaderFile;
	
	@Value("${file.paymentPlanLine}")
	private String paymentPlanLineFile;
	
	@Value("${file.paymentUpdate}")
	private String paymentUpdateFile;
}
