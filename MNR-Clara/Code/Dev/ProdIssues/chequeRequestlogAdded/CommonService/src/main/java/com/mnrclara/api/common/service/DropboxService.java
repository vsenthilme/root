package com.mnrclara.api.common.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.CreateFolderResult;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.UploadErrorException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DropboxService {

	@Value("${dropbox.access.token}")
	private String ACCESS_TOKEN;

	/**
	 * doUploadFile
	 * 
	 * @param newFolder
	 * @param file
	 * @return
	 * @throws UploadErrorException
	 * @throws DbxException
	 * @throws IOException
	 */
	public Map<String, String> doUploadFile(String dropboxFolder, String newFolder, File file)
			throws UploadErrorException, DbxException, IOException {
		// Create Dropbox client
		DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/mnrclara").build();
		DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
		
		if (dropboxFolder.equalsIgnoreCase("agreement")) {
			dropboxFolder = "/Agreement template/";
		} else {
			dropboxFolder = "/Document template/";
		}
		
		// Getting the folder details
		Metadata metaData = null;
		try {
//			metaData = client.files().getMetadata("/Agreement template/" + newFolder);
			metaData = client.files().getMetadata(dropboxFolder + newFolder);
			log.info("newFolder------>" + newFolder);
			log.info("metaData------>" + metaData.getPathLower());
		} catch (Exception e1) {
			// Checking whether ClientId based Folder exists in Dropbox nr not
			// Creating new folder only if it is not exists in Dropbox
//			CreateFolderResult md = client.files().createFolderV2("/Agreement template/" + newFolder);
			CreateFolderResult md = client.files().createFolderV2(dropboxFolder + newFolder);
		} 
		
		Map<String, String> mapUploadedFileProps = new HashMap<>();

		// Upload File to Dropbox
		try (InputStream in = new FileInputStream(file)) {
//			FileMetadata metadata = client.files().uploadBuilder("/Agreement template/" + newFolder + "/" + file.getName()).uploadAndFinish(in);
			FileMetadata metadata = client.files().uploadBuilder(dropboxFolder + newFolder + "/" + file.getName()).uploadAndFinish(in);
			mapUploadedFileProps.put("fileName", metadata.getName());
			mapUploadedFileProps.put("pathDisplay", metadata.getPathDisplay());
			mapUploadedFileProps.put("pathLower", metadata.getPathLower());
			return mapUploadedFileProps;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * doDownloadFile
	 * 
	 * @param fileUrl
	 * @return
	 * @throws DbxException
	 * @throws IOException
	 */
	public String doDownloadFile(String fileUrl) throws DbxException, IOException {
		log.info("File URL: " + fileUrl);
		// Create Dropbox client
		DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/mnrclara").build();
		DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
		String filedir = System.getProperty("user.dir");
		String dropBoxPath = "/Agreement template";
		String filePath = "";
		
		if (fileUrl.startsWith("/")) {
			filePath = filedir + File.separator + fileUrl.substring(1);			
			dropBoxPath = dropBoxPath + fileUrl;
		} else {
			filePath = filedir + File.separator + fileUrl;
			dropBoxPath = dropBoxPath + "/" + fileUrl;
		}
		
		log.info("Drop box path : " + dropBoxPath);
		log.info("filePath : " + filePath);
		
		OutputStream outputStream = new FileOutputStream(filePath);
		FileMetadata metadata1 = client.files().downloadBuilder(dropBoxPath).download(outputStream);
		return filePath;
	}
	
	/**
	 * 
	 * @param dropBoxPath
	 * @param fileUrl
	 * @return
	 * @throws DbxException
	 * @throws IOException
	 */
	public String doDownloadFile(String dropBoxPath, String fileUrl) throws DbxException, IOException {
		log.info("File URL: " + fileUrl);
		// Create Dropbox client
//		DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/mnrclara").build();
//		DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
		String filedir = System.getProperty("user.dir");
//		String dropBoxPath = "/Agreement template";
		if (dropBoxPath.equalsIgnoreCase("document")) {
			dropBoxPath = "/Document template";
		} else {
			dropBoxPath = "/Agreement template";
		}
		
		String filePath = "";
		if (fileUrl.startsWith("/")) {
			filePath = filedir + File.separator + fileUrl.substring(1);			
			dropBoxPath = dropBoxPath + fileUrl;
		} else {
			filePath = filedir + File.separator + fileUrl;
			dropBoxPath = dropBoxPath + "/" + fileUrl;
		}
		
		log.info("Drop box path : " + dropBoxPath);
		log.info("filePath : " + filePath);
		
		OutputStream outputStream = new FileOutputStream(filePath);
//		FileMetadata metadata1 = client.files().downloadBuilder(dropBoxPath).download(outputStream);
		return filePath;
	}
}
