package com.mnrclara.api.common.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mnrclara.api.common.config.PropertiesConfig;
import com.mnrclara.api.common.controller.exception.BadRequestException;

@Service
public class FileStorageService {

	@Autowired
	PropertiesConfig propertiesConfig;
	
    private Path fileStorageLocation = null;

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
}
