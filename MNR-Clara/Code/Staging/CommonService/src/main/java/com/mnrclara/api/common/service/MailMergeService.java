package com.mnrclara.api.common.service;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.License;
import com.mnrclara.api.common.config.PropertiesConfig;
import com.mnrclara.api.common.model.mailmerge.MailMerge;
import com.mnrclara.api.common.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailMergeService {
	
	@Autowired
	DropboxService dropboxService;
	
	@Autowired
	PropertiesConfig propertiesConfig;
	
	/**
	 * 
	 * @param mailMerge
	 * @return
	 * @throws java.lang.Exception
	 */
	public MailMerge doMailMerge (MailMerge mailMerge) throws java.lang.Exception {
		String licFileClasspathResource = "classpath:Aspose.Words.Java.lic";
		File lic_file = ResourceUtils.getFile(licFileClasspathResource);
		License lic = new License();
		lic.setLicense(new FileInputStream(lic_file));

		String templateFileName = mailMerge.getDocumentUrl();
		log.info("templateFileName : " + templateFileName);
		
		if (templateFileName.startsWith("/")) {
			templateFileName = templateFileName.substring(1); // Removing Front slash from URL
		} 
		
		Long dbDocumentUrlVer = mailMerge.getDocumentUrlVersion();
		log.info("mailMerge : " + mailMerge.getFieldValues());
		
		if (dbDocumentUrlVer != null) {
			dbDocumentUrlVer++;	 	// Incrementing version to higher number
		} else {
			dbDocumentUrlVer = 1L;	// By Default, the initial version starts from Zero.
		}
		
		log.info("mailMerge.getClassId() : " + mailMerge.getClassId() );
		
		/*
		 * Download Template from agreement/document 
		 */
		String filePath = "";
		if (mailMerge.getDocumentStorageFolder().equalsIgnoreCase("agreement")) {
			filePath = propertiesConfig.getDocStorageBasePath() + propertiesConfig.getDocStorageTemplatePath() + 
					propertiesConfig.getDocStorageAgreementPath() + "/" + templateFileName;
		} else if (mailMerge.getDocumentStorageFolder().equalsIgnoreCase("document")) {
			filePath = propertiesConfig.getDocStorageBasePath() + propertiesConfig.getDocStorageTemplatePath() + 
					propertiesConfig.getDocStorageDocumentPath() + "/" + templateFileName;
		} else if (mailMerge.getDocumentStorageFolder().equalsIgnoreCase("clientportal")) {
			filePath = propertiesConfig.getDocStorageBasePath() + propertiesConfig.getDocStorageTemplatePath() + 
					propertiesConfig.getDocStorageDocumentPath() + "/" + templateFileName;
//			filePath = propertiesConfig.getDocStorageBasePath() + propertiesConfig.getDocStorageTemplatePath() + 
//					"/" + templateFileName;
		} 
		
		log.info("filePath: " + filePath);
		
		/*
		 * Mail merge process
		 */
		File doc_file = ResourceUtils.getFile(filePath);
		log.info("doc_file: " + doc_file.getAbsolutePath());
		
		// Include the code for our template
		Document doc = new Document(doc_file.getAbsolutePath());
		
		// Pass the document to document builder
		DocumentBuilder builder = new DocumentBuilder(doc);
		
		// Fill the fields in the document with user data
		doc.getMailMerge().execute(mailMerge.getFieldNames(), mailMerge.getFieldValues());
		
		// Save the document
		// Call from isFromClientORMatterDocument flow
		if (mailMerge.isFromClientORMatterDocument()) {
			String fileExtn = templateFileName.substring(templateFileName.lastIndexOf('.'));
			
			// Removing extension of file
			templateFileName = templateFileName.substring(0, templateFileName.lastIndexOf('.'));
			templateFileName = mailMerge.getDocumentCode() + "_" + templateFileName + "_processed_" + 
						DateUtils.getCurrentTimestamp() + fileExtn;	// Creating new filename
		} else {
			String fileExtn = templateFileName.substring(templateFileName.lastIndexOf('.'));
			
			// Removing extension of file
			templateFileName = templateFileName.substring(0, templateFileName.lastIndexOf('.'));	
			templateFileName = mailMerge.getAgreementCode() + "_" + templateFileName + "_processed_v" + 
						dbDocumentUrlVer + fileExtn;		// Creating new filename
		}
		
		String newFolder = null;
		if (mailMerge.getDocumentStorageFolder().equalsIgnoreCase("agreement")) {
			/*
			 * Creating new folder in name of the Potential Client Id
			 */
			if (mailMerge.getClassId() == 1) {										// - LNE
				// Choose Y:\Client\2 Employment-Labor Clients\Clara
				newFolder = propertiesConfig.getDocStorageLNEBasePath() + 
						propertiesConfig.getDocStorageAgreementPath() + "/" + mailMerge.getClientId();
				log.info("LNE path : " + filePath);
			} else { 																// Immigration
				// Choose X:\Firm\Immigration Section\1LawOfficeDoc\Clara
				newFolder = propertiesConfig.getDocStorageImmigBasePath() + 
						propertiesConfig.getDocStorageAgreementPath() + "/" + mailMerge.getClientId();
				log.info("Immigration path : " + filePath);
			}
		} else if (mailMerge.getDocumentStorageFolder().equalsIgnoreCase("document")) {
			if (mailMerge.getClassId() == 1) {										// - LNE
				// Choose Y:\Client\2 Employment-Labor Clients\Clara
				newFolder = propertiesConfig.getDocStorageLNEBasePath() + 
						propertiesConfig.getDocStorageDocumentPath() + "/" + mailMerge.getClientId();
				log.info("LNE path : " + filePath);
			} else { 																// Immigration
				// Choose X:\Firm\Immigration Section\1LawOfficeDoc\Clara
				newFolder = propertiesConfig.getDocStorageImmigBasePath() + 
						propertiesConfig.getDocStorageDocumentPath() + "/" + mailMerge.getClientId();
				log.info("Immigration path : " + filePath);
			}
		}  else { // Client Portal
			if (mailMerge.getClassId() == 1) {										// - LNE
				// Choose Y:\Client\2 Employment-Labor Clients\Clara
				newFolder = propertiesConfig.getDocStorageLNEBasePath() + 
						propertiesConfig.getDocStorageDocumentPath() + "/clientportal/" + mailMerge.getClientId();
				log.info("LNE path : " + filePath);
			} else { 																// Immigration
				// Choose X:\Firm\Immigration Section\1LawOfficeDoc\Clara
				newFolder = propertiesConfig.getDocStorageImmigBasePath() + 
						"/clientportal/" + mailMerge.getClientId();
				log.info("Immigration path : " + filePath);
			}
		}
		log.info("Mailmerge-newFolder----> : " + newFolder);
		
		/*
		 * check for class ID whether 1 0r 2 to decide the X or Y drive
		 */
		Path newDirCreated = Paths.get(newFolder);
		if (!Files.exists(newDirCreated)) {
			newDirCreated = Files.createDirectories(newDirCreated);
		}
		
		String newFileName = newDirCreated + File.separator + templateFileName;
		builder.getDocument().save(newFileName);
		
		// Locating created File for uploading to DropBox
		File processedFilePath = new File(newFileName);
		log.info("proc_doc_file location: " + processedFilePath.getAbsolutePath());
		
		mailMerge.setDocumentUrlVersion(dbDocumentUrlVer);
		mailMerge.setProcessedFilePath(processedFilePath.getName());
		mailMerge.setDocumentUrl(processedFilePath.getAbsolutePath());
		return mailMerge;
	}
}
