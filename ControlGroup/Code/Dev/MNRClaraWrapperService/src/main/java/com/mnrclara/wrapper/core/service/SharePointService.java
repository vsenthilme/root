package com.mnrclara.wrapper.core.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.CreateFolderResult;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.mnrclara.wrapper.core.config.PropertiesConfig;
import com.mnrclara.wrapper.core.model.auth.AuthToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SharePointService {

	private static final String ACCESS_TOKEN = null;

	@Autowired
	PropertiesConfig propertiesConfig;
	
	@Autowired
	AuthTokenService authTokenService;

	/**
	 * 
	 * @return
	 */
	public RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();

		// Object Convertor
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter
				.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
		restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
		return restTemplate;
	}
	
	/**
	 * 
	 * @param fileLocation
	 * @throws Exception
	 */
	public void doUploadFile(Path fileLocation) throws Exception {
		InputStream inputStream = Files.newInputStream(fileLocation);
		byte[] data = inputStream.readAllBytes();
		
		AuthToken authToken = authTokenService.getSharePointOAuthToken();
		log.info("FileUpload authToken : " + authToken.getAccess_token());
		
		RestTemplate restTemplate = getRestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.add("Authorization", "Bearer " + authToken.getAccess_token());
		
		String buildUrl = propertiesConfig.getSpFileUploadUrl();
		log.info("buildUrl : " + buildUrl);
		
		String folderName = "Test";
		String fileName = fileLocation.getFileName().toString();
		buildUrl = buildUrl.replaceFirst("@folder", folderName);
		buildUrl = buildUrl.replaceFirst("@file", fileName);
		log.info("--------buildUrl--------------->: " + buildUrl);
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(buildUrl);
		HttpEntity<byte[]> requestEntity = new HttpEntity<>(data, headers);

		ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT, requestEntity, String.class);
		log.info("FileUpload Response : " + response.getBody());
	}
	
		/*
	 * URL: http://site url/_api/web/GetFolderByServerRelativeUrl('/Folder Name')/Files('file name')/$value
	 * method: GET
	 * headers: Authorization: "Bearer " + accessToken
	 */
	public byte[] doDownloadFile(String fileName) throws Exception {
		AuthToken authToken = authTokenService.getSharePointOAuthToken();
		log.info("FileDownload authToken : " + authToken.getAccess_token());
		
		RestTemplate restTemplate = getRestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.add("Authorization", "Bearer " + authToken.getAccess_token());
		
		String buildUrl = propertiesConfig.getSpFileUploadUrl();
		log.info("buildUrl : " + buildUrl);
		
		String folderName = "Test";
//		fileName = "a.txt";
		buildUrl = buildUrl.replaceFirst("@folder", folderName);
		buildUrl = buildUrl.replaceFirst("@file", fileName);
		log.info("--------buildUrl--------------->: " + buildUrl);
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(buildUrl);
		HttpEntity<?> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, requestEntity, byte[].class);
		log.info("FileDownload Response : " + response.getBody());
		return response.getBody();
	}
	
	public static void main(String[] args) {
		//String buildUrl = "https://graph.microsoft.com/v1.0/sites/207ace78-0651-4d8e-a273-5422ff52a99d/Drives/b!eM56IFEGjk2ic1Qi_1KpnfTRlMrY2K9JuOIWpyqvaQNAH-ACGzXyT7sM--rezEpv/root:/Test/Request1.txt:/content";
		String buildUrl = "https://graph.microsoft.com/v1.0/sites/207ace78-0651-4d8e-a273-5422ff52a99d/Drives/b!eM56IFEGjk2ic1Qi_1KpnfTRlMrY2K9JuOIWpyqvaQNAH-ACGzXyT7sM--rezEpv/root:/@folder/@file:/content";
		String fileName = "xxww";
		String folderName = "Test";
		
		buildUrl = buildUrl.replaceFirst("@folder", folderName);
		buildUrl = buildUrl.replaceFirst("@file", fileName);
		log.info(buildUrl);
	}
}
