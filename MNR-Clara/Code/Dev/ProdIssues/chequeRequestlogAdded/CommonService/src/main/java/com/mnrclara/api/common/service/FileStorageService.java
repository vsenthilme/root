package com.mnrclara.api.common.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import com.mnrclara.api.common.model.auth.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.mnrclara.api.common.config.PropertiesConfig;
import com.mnrclara.api.common.controller.exception.BadRequestException;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class FileStorageService {

	@Autowired
	PropertiesConfig propertiesConfig;

    @Autowired
    AuthTokenService authTokenService;

    private Path fileStorageLocation = null;

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
     * @param file
     * @return
     */
    public String storeFile(MultipartFile file) {
    	this.fileStorageLocation = Paths.get(propertiesConfig.getFileUploadDir()).toAbsolutePath().normalize();
    	if (!Files.exists(fileStorageLocation)) {
    		 try {
	            Files.createDirectories(this.fileStorageLocation);
	        } catch (Exception ex) {
	        	ex.printStackTrace();
	            throw new BadRequestException("Could not create the directory where the uploaded files will be stored.");
	        }
    	}
        
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new BadRequestException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
        	ex.printStackTrace();
            throw new BadRequestException("Could not store file " + fileName + ". Please try again!");
        }
    }

    /**
     * 
     * @param fileName
     * @return
     */
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new BadRequestException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new BadRequestException("File not found " + fileName);
        }
    }

    /**
     *
     * @param file
     * @return
     * @throws Exception
     */
    public byte[] downloadFile(String file) throws Exception {
        byte[] fileContent = doDownloadFile(file);
        log.info("File Content: " + fileContent);
        return fileContent;
    }

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
}
