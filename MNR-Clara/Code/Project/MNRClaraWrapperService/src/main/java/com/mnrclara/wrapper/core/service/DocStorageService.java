package com.mnrclara.wrapper.core.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.CreateFolderResult;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.mnrclara.wrapper.core.config.PropertiesConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DocStorageService {
	
	private static final String ACCESS_TOKEN = null;
	
	@Autowired
	PropertiesConfig propertiesConfig;
	
	/**
	 * doUploadFile
	 * @param file
	 * @return
	 * @throws UploadErrorException
	 * @throws DbxException
	 * @throws IOException
	 */
//	public Map<String, String> doUploadFile (File file) throws UploadErrorException, DbxException, IOException {
//		// Create Dropbox client
//		DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/mnrclara").build();
//		DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
//		
//		Map<String, String> mapUploadedFileProps = new HashMap<>();
//		
//		// Upload File to Dropbox
//		try (InputStream in = new FileInputStream(file)) {
//			FileMetadata metadata = client.files().uploadBuilder("/" + file.getName()).uploadAndFinish(in);
//			mapUploadedFileProps.put("fileName", metadata.getName());
//			mapUploadedFileProps.put("pathDisplay", metadata.getPathDisplay());
//			mapUploadedFileProps.put("pathLower", metadata.getPathLower());
//			return mapUploadedFileProps;
//		} catch (Exception e) {
//			throw e;
//		}
//	}
	
	/**
	 * doUploadFile - Plain File Upload
	 * @param in
	 * @param fileName
	 * @param location 
	 * @return
	 * @throws UploadErrorException
	 * @throws DbxException
	 * @throws IOException
	 */
//	public Map<String, String> doUploadFile (InputStream in, String fileName, String location) 
//			throws UploadErrorException, DbxException, IOException {
//		// Create Dropbox client
//		DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/mnrclara/").build();
//		DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
//		Map<String, String> mapUploadedFileProps = new HashMap<>();
//		String locationPath = "";
//		
//		// Upload File to Dropbox
//		try {
//			if (location != null && location.toLowerCase().startsWith("agreement")) {
//				// "/Agreement template/"
////				locationPath = agreementFolder;
//				if (location.indexOf("/") > 0) {
//					// /Agreement template/A001_200003
//					locationPath = locationPath + location.substring(location.indexOf("/") + 1 ) + "/";
//				}
//			} else if (location != null && location.toLowerCase().startsWith("document")) {
//				// "/Document template/"
////				locationPath = documentFolder;
//				if (location.indexOf("/") > 0) {
//					// /Document template/D001_200003
//					locationPath = locationPath + location.substring(location.indexOf("/") + 1 ) + "/";
//				}
//			} else if (location != null && location.toLowerCase().startsWith("receipt")) {
//				// "/ReceiptNumber/"
////				locationPath = receiptNumberFolder;
//				if (location.indexOf("/") > 0) {
//					locationPath = locationPath + location.substring(location.indexOf("/") + 1 ) + "/";
//					log.info("locationPath : " + locationPath);
//				}
//			}
//			FileMetadata metadata = client.files().uploadBuilder(locationPath + fileName).uploadAndFinish(in);
//			mapUploadedFileProps.put("fileName", metadata.getName());
//			mapUploadedFileProps.put("pathDisplay", metadata.getPathDisplay());
//			mapUploadedFileProps.put("pathLower", metadata.getPathLower());
//			return mapUploadedFileProps;
//		} catch (Exception e) {
//			throw e;
//		}
//	}
	
	public Map<String, String> doUploadFile(String dropboxFolder, String newFolder, String fileName, InputStream in)
			throws UploadErrorException, DbxException, IOException {
		// Create Dropbox client
		DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/mnrclara").build();
		DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
		
		if (dropboxFolder.equalsIgnoreCase("agreement")) {
//			dropboxFolder = agreementFolder;
		} else {
//			dropboxFolder = documentFolder;
		}
		
		// Getting the folder details
		try {
			Metadata metaData = client.files().getMetadata(dropboxFolder + newFolder);
			log.info("newFolder------>" + newFolder);
		} catch (Exception e1) {
			// Checking whether ClientId based Folder exists in Dropbox nr not
			// Creating new folder only if it is not exists in Dropbox
			CreateFolderResult md = client.files().createFolderV2(dropboxFolder + newFolder);
		} 
		
		// Upload File to Dropbox
		Map<String, String> mapUploadedFileProps = new HashMap<>();
		FileMetadata metadata = client.files().uploadBuilder(dropboxFolder + newFolder + "/" + fileName).uploadAndFinish(in);
		mapUploadedFileProps.put("fileName", metadata.getName());
		mapUploadedFileProps.put("pathDisplay", metadata.getPathDisplay());
		mapUploadedFileProps.put("pathLower", metadata.getPathLower());
		return mapUploadedFileProps;
	}
	
	/**
	 * 
	 * @param location
	 * @param fileUrl
	 * @return
	 * @throws DbxException
	 * @throws IOException
	 */
//	public String doDownloadFile (String location, String file) throws DbxException, IOException {
//		// Create Dropbox client
//		DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/mnrclara").build();
//		DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
//		String filePath = propertiesConfig.getDownloadDir() + File.separator + file;
//		log.info("filePath: " + filePath);
//		
//		String locationPath = "";
//		if (location != null && location.startsWith("agreement")) {
////			locationPath = agreementFolder;
//			if (location.indexOf("/") > 0) {
//				// /Agreement template/A001_200003
//				locationPath = locationPath + location.substring(location.indexOf("/")+1) + "/";
//			}
//		} else if (location != null && location.startsWith("document")) {
////			locationPath = documentFolder;
//			if (location.indexOf("/") > 0) {
//				// /Document template/D001_200003
//				locationPath = locationPath + location.substring(location.indexOf("/")+1) + "/";
//			}
//		} else if (location != null && location.toLowerCase().startsWith("receipt")) {
//			// "/ReceiptNumber/"
////			locationPath = receiptNumberFolder;
//			if (location.indexOf("/") > 0) {
//				locationPath = locationPath + location.substring(location.indexOf("/") + 1 ) + "/";
//			}
//		}
//		
//		OutputStream outputStream = new FileOutputStream(filePath);
//		FileMetadata metadata1 = client.files()
//				.downloadBuilder(locationPath + file)
//		        .download(outputStream);
//		log.info("filePath: " + filePath);
//		return filePath;
//	}
	
	/**
	 * 
	 * @param location
	 * @param file
	 * @param classId 
	 * @return
	 * @throws DbxException
	 * @throws IOException
	 */
	public String getQualifiedFilePath (String location, String file, Long classId) throws DbxException, IOException {
		String filePath = propertiesConfig.getDocStorageBasePath();
		
		log.info("getQualifiedFilePath---location------>: " + location);
		log.info("getQualifiedFilePath---file------>: " + file);
		log.info("getQualifiedFilePath---class------>: " + classId);
		
		try {
			/*
			 * mail merge - download
			 * location: agreement/Pot_client_Id
			 * proc_doc_file : /home/mnr/root/MNR-Clara/Code/Project/MailMerge/Processed/2000264/Agreement_Document_processed_v1.docx
			 */
			if (location != null && location.startsWith("agreement")) {
				// template download alone
				filePath = filePath + propertiesConfig.getDocStorageTemplatePath() + 
						propertiesConfig.getDocStorageAgreementPath();	
				
				// This flow for mail merge to download processed file
				if (location.indexOf("/") > 0) { 			
					if (classId != null && classId == 1) {		// - LNE
						/*
						 * Choose Y:\Client\2 Employment-Labor Clients\Clara
						 */
						filePath = propertiesConfig.getDocStorageLNEBasePath() + "/" + location;
						log.info("classId--1--LNE path : " + filePath);
					} else { 								// Immigration
						/*
						 * Choose X:\Firm\Immigration Section\1LawOfficeDoc\Clara
						 */
						filePath = propertiesConfig.getDocStorageImmigBasePath() + "/" + location;
						log.info("classId--1--Immigration path : " + filePath);
					}
				}
			} else if (location != null && location.startsWith("document")) {
				filePath = filePath + propertiesConfig.getDocStorageTemplatePath() + 
						propertiesConfig.getDocStorageDocumentPath();
				
				// This flow for mail merge to download processed file
				if (location.indexOf("/") > 0) { 			
					// This flow for mail merge to download processed file
					if (classId != null && classId == 1) {											// - LNE
						// Choose Y:\Client\2 Employment-Labor Clients\Clara
						filePath = propertiesConfig.getDocStorageLNEBasePath() + "/" + location;
						log.info("classId--2--LNE path : " + filePath);
					} else { 																		// Immigration
						// Choose X:\Firm\Immigration Section\1LawOfficeDoc\Clara
						filePath = propertiesConfig.getDocStorageImmigBasePath() + "/" + location;
						log.info("classId--2--Immigration path : " + filePath);
					}
				}
				log.info("filePath------in document------>: " + filePath);
			} else if (location != null && location.toLowerCase().startsWith("receipt")) {
				 // This flow for mail merge to download processed file
                if (location.indexOf("/") > 0) {
                        filePath = filePath + "/" + location;
                }
                log.info("filePath------in document------>: " + filePath);
			} else if (location != null && location.toLowerCase().startsWith("clientportal")) {
				filePath = filePath + propertiesConfig.getDocStorageTemplatePath() + "/clientportal/";
				
				// This flow for mail merge to download processed file
				if (location.indexOf("/") > 0) { 			
					filePath = propertiesConfig.getDocStorageBasePath() + "/" + location;
				}
			} else if (location != null && location.toLowerCase().startsWith("temp")) {
				filePath = filePath + propertiesConfig.getDocStorageTemplatePath() + "/clientportal/temp/";
				
				// This flow for mail merge to download processed file
				if (location.indexOf("/") > 0) { 			
					filePath = filePath + propertiesConfig.getDocStorageReceiptPath() + location;
				}
			}
		} catch (Exception e) {
			log.info("getQualifiedFilePath------Error------>: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		filePath = filePath + "/" + file;
		log.info("filePath: " + filePath);
		return filePath;
	}
}
