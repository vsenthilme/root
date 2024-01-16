package com.mnrclara.wrapper.core.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mnrclara.wrapper.core.model.setup.EMail;
import com.mnrclara.wrapper.core.model.setup.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.UploadErrorException;
import com.mnrclara.wrapper.core.config.PropertiesConfig;
import com.mnrclara.wrapper.core.exception.BadRequestException;
import com.mnrclara.wrapper.core.model.auth.AuthToken;
import com.mnrclara.wrapper.core.model.crm.UpdatePotentialClient;
import com.mnrclara.wrapper.core.model.datamigration.ClientGeneral;
import com.mnrclara.wrapper.core.model.datamigration.ClientNote;
import com.mnrclara.wrapper.core.model.datamigration.MatterAssignment;
import com.mnrclara.wrapper.core.model.datamigration.MatterExpense;
import com.mnrclara.wrapper.core.model.datamigration.MatterGenAcc;
import com.mnrclara.wrapper.core.model.datamigration.MatterNote;
import com.mnrclara.wrapper.core.model.datamigration.MatterRate;
import com.mnrclara.wrapper.core.model.datamigration.MatterTimeTicket;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileStorageService {

    @Autowired
    PropertiesConfig propertiesConfig;

    @Autowired
    DocStorageService docStorageService;

    @Autowired
    ManagementService managementService;

    @Autowired
    CRMService crmService;

    @Autowired
    AuthTokenService authTokenService;

    @Autowired
    CommonService commonService;

    @Autowired
    SetupService setupService;
    
    @Autowired
    SharePointService sharePointService;

    private Path fileStorageLocation = null;

    /**
     * @param location
     * @param file
     * @return
     * @throws DbxException
     * @throws UploadErrorException
     */
    public Map<String, String> storeFile(String location, MultipartFile file)
            throws UploadErrorException, DbxException {
    	
    	log.info("location--#2-> : " + location);

        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        log.info("filename before: " + fileName);
        fileName = fileName.replace(" ", "_");
        log.info("filename after: " + fileName);

        String locationPath = null;
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new BadRequestException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            if (location != null && location.toLowerCase().startsWith("agreement")) {
                if (location.indexOf('/') > 0) {
                    locationPath = propertiesConfig.getDocStorageBasePath() + "/" + location;
                } else {
                    // Agreement template
                    locationPath = propertiesConfig.getDocStorageBasePath() + propertiesConfig.getDocStorageTemplatePath() +
                            propertiesConfig.getDocStorageAgreementPath();
                }
            } else if (location != null && location.toLowerCase().startsWith("document")) {
                if (location.indexOf('/') > 0) {
                    locationPath = propertiesConfig.getDocStorageBasePath() + "/" + location;
                } else {
                    // Document template
                    locationPath = propertiesConfig.getDocStorageBasePath() + propertiesConfig.getDocStorageTemplatePath() +
                            propertiesConfig.getDocStorageDocumentPath();
                }
            } else if (location != null && location.toLowerCase().startsWith("receipt")) {
                // ReceiptNumber
                locationPath = propertiesConfig.getDocStorageBasePath() + "/" + location;
            } else if (location != null && location.toLowerCase().startsWith("clientportal")) {
                if (location.indexOf('/') > 0) {
                    locationPath = propertiesConfig.getDocStorageBasePath() + "/" + location;
                }
            } else if (location != null && location.toLowerCase().startsWith("temp")) {
                if (location.indexOf('/') > 0) {
                    locationPath = propertiesConfig.getDocStorageBasePath() + "/clientportal/" + location;
                }
            } else if (location != null && location.toLowerCase().startsWith("sharepoint")) {
                if (location.indexOf('/') > 0) {
                	locationPath = propertiesConfig.getDocStorageBasePath() + "/" + location;
                }
            }

            log.info("locationPath : " + locationPath);

            this.fileStorageLocation = Paths.get(locationPath).toAbsolutePath().normalize();
            log.info("fileStorageLocation--------> " + fileStorageLocation);

            if (!Files.exists(fileStorageLocation)) {
                try {
                    Files.createDirectories(this.fileStorageLocation);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw new BadRequestException("Could not create the directory where the uploaded files will be stored.");
                }
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
        	// Do SharePoint operations
            if (location != null && location.toLowerCase().startsWith("sharepoint")) {
            	try {
					sharePointService.doUploadFile(targetLocation);
					log.info("Sharepoint File uploaded...");
				} catch (Exception e) {
					e.printStackTrace();
					throw new BadRequestException("Could not store file on SharePoint: " + fileName + ". Please try again!");
				}
            }

            Map<String, String> mapFileProps = new HashMap<>();
            mapFileProps.put("file", fileName);
            mapFileProps.put("location", location);
            mapFileProps.put("status", "UPLOADED");
            return mapFileProps;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new BadRequestException("Could not store file " + fileName + ". Please try again!");
        }
    }

    /**
     * @param location
     * @param file
     * @param emailId
     * @return
     * @throws UploadErrorException
     * @throws DbxException
     */
    public Map<String, String> storeFile(String location, MultipartFile file, String emailId, String userName)
            throws UploadErrorException, DbxException {

        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        log.info("filename before: " + fileName);
        fileName = fileName.replace(" ", "_");
        log.info("filename after: " + fileName);

        String locationPath = null;
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new BadRequestException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            if (location != null && location.toLowerCase().startsWith("agreement")) {
                if (location.indexOf('/') > 0) {
                    locationPath = propertiesConfig.getDocStorageBasePath() + "/" + location;
                } else {
                    // Agreement template
                    locationPath = propertiesConfig.getDocStorageBasePath() + propertiesConfig.getDocStorageTemplatePath() +
                            propertiesConfig.getDocStorageAgreementPath();
                }
            } else if (location != null && location.toLowerCase().startsWith("document")) {
                if (location.indexOf('/') > 0) {
                    locationPath = propertiesConfig.getDocStorageBasePath() + "/" + location;
                } else {
                    // Document template
                    locationPath = propertiesConfig.getDocStorageBasePath() + propertiesConfig.getDocStorageTemplatePath() +
                            propertiesConfig.getDocStorageDocumentPath();
                }
            } else if (location != null && location.toLowerCase().startsWith("receipt")) {
                // ReceiptNumber
                locationPath = propertiesConfig.getDocStorageBasePath() + "/" + location;
            } else if (location != null && location.toLowerCase().startsWith("clientportal")) {
                if (location.indexOf('/') > 0) {
                    locationPath = propertiesConfig.getDocStorageBasePath() + "/" + location;
                }
            } else if (location != null && location.toLowerCase().startsWith("temp")) {
                if (location.indexOf('/') > 0) {
                    locationPath = propertiesConfig.getDocStorageBasePath() + "/clientportal/" + location;
                }
            } else if (location != null && location.toLowerCase().startsWith("check")) {
                if (location.indexOf('/') > 0) {
                    // Check
                    locationPath = propertiesConfig.getDocStorageBasePath() + "/" + location;
                    // Append date time with the file
                    String fileExtn = fileName.substring(fileName.lastIndexOf("."));
                    fileName = fileName.substring(0, fileName.lastIndexOf("."));
                    fileName = fileName + "_" + getCurrentTimestamp() + fileExtn;
                }
            }

            log.info("locationPath : " + locationPath);

            this.fileStorageLocation = Paths.get(locationPath).toAbsolutePath().normalize();
            log.info("fileStorageLocation--------> " + fileStorageLocation);

            if (!Files.exists(fileStorageLocation)) {
                try {
                    Files.createDirectories(this.fileStorageLocation);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw new BadRequestException("Could not create the directory where the uploaded files will be stored.");
                }
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            Map<String, String> mapFileProps = new HashMap<>();
            mapFileProps.put("file", fileName);
            mapFileProps.put("location", location);
            mapFileProps.put("status", "UPLOADED");

            if (location != null && location.toLowerCase().startsWith("check")) {
                if (emailId == null) {
                    throw new BadRequestException("Email Id mandatory for Check Upload");
                }
                if (userName == null) {
                    throw new BadRequestException("userName mandatory for Check Upload");
                }
                // Get AuthToken for CommonService
                AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();

                String matterNumber = location.substring(location.indexOf('/', location.indexOf('/') + 1) + 1);
                log.info("matterNumber:" + matterNumber);

                AuthToken authTokenForManagementService = authTokenService.getManagementServiceAuthToken();
                AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
                com.mnrclara.wrapper.core.model.management.MatterGenAcc matterGenAcc = managementService.getMatterGenAcc(matterNumber, authTokenForManagementService.getAccess_token());
                UserProfile userProfile = setupService.getUserProfile(userName, authTokenForSetupService.getAccess_token());

                EMail email = new EMail();
                //subject of the mail
                String subject = null;
                if (emailId.equalsIgnoreCase(propertiesConfig.getLoginEmailCheckAddress())) {
                    subject = userProfile.getFullName() + " has submitted Check Request: " + matterGenAcc.getMatterDescription();
                }

                if (!emailId.equalsIgnoreCase(propertiesConfig.getLoginEmailCheckAddress())) {
                    subject = "Check Request: " + matterNumber + " - " + matterGenAcc.getMatterDescription();
                }
                //body of the mail
                String msg = getString(emailId, matterNumber, matterGenAcc, userProfile.getFullName());

                email.setFromAddress(propertiesConfig.getLoginEmailFromAddress());
                email.setSubject(subject);
                email.setBodyText(msg);
//				email.setToAddress(propertiesConfig.getLoginEmailToAddress());
//				email.setCcAddress(propertiesConfig.getLoginEmailCcAddress());
                email.setToAddress(emailId);
                email.setCcAddress(emailId);
                email.setSenderName("Clara IT");

                boolean isEMailSent = commonService.sendEmail(email, fileName, location, authTokenForCommonService.getAccess_token());

                if (isEMailSent) {
                    mapFileProps.put("EMail", "Sent Successfully");
                }
            }
            
            return mapFileProps;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new BadRequestException("Could not store file " + fileName + ". Please try again!");
        }
    }

    /**
     *
     * @param emailId
     * @param matterNumber
     * @param matterGenAcc
     * @param userName
     * @return
     */
    private String getString(String emailId, String matterNumber,
                             com.mnrclara.wrapper.core.model.management.MatterGenAcc matterGenAcc,
                             String userName) {

        String msg = "Hi";

        if (emailId.equalsIgnoreCase(propertiesConfig.getLoginEmailCheckAddress())) {
            msg = msg + ",<br/>You Have Received a Check Request for matter - "
                    + matterNumber + " : " + matterGenAcc.getMatterDescription() + ". Please approve the request in Clara.<br/><br/> Thank You,<br/>";
        }

        if (!emailId.equalsIgnoreCase(propertiesConfig.getLoginEmailCheckAddress())) {
            msg = msg + " "+ userName + ",<br/> Your request has been processed.<br/><br/> Regards <br/>";
        }

        msg = msg + "Clara IT";
        return msg;
    }

    /**
     * @param location
     * @param file
     * @param loginUserID
     * @param classId
     * @return
     * @throws UploadErrorException
     * @throws DbxException
     */
    public Map<String, String> storeFileForNonMailMerge(String location, MultipartFile file,
                                                        String loginUserID, Long classId)
                                                throws UploadErrorException, DbxException {
        log.info("location----------> : " + location);

        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        log.info("filename before: " + fileName);
        fileName = fileName.replace(" ", "_");
        log.info("filename after: " + fileName);

        String locationPath = null;
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new BadRequestException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            if (location != null && location.toLowerCase().startsWith("agreement")) {
                if (location.indexOf('/') > 0) {
                    // This flow for mail merge to download processed file
                    if (classId != null && classId == 1) {                                            // - LNE
                        locationPath = propertiesConfig.getDocStorageLNEBasePath() + "/" + location;
                        log.info("classId--2--LNE path : " + locationPath);
                    } else {                                                                        // Immigration
                        locationPath = propertiesConfig.getDocStorageImmigBasePath() + "/" + location;
                        log.info("classId--2--Immigration path : " + locationPath);
                    }

                    /*
                     * Update Filename in table
                     */
                    // agreement/2000075
                    String potentialClientId = location.substring(location.indexOf('/') + 1);
                    log.info("potentialClientId : " + potentialClientId);

                    AuthToken authTokenCRM = authTokenService.getCrmServiceAuthToken();
                    UpdatePotentialClient updatePotentialClient = new UpdatePotentialClient();
                    updatePotentialClient.setAgreementUrl(fileName);
                    crmService.updatePotentialClient(potentialClientId, loginUserID, updatePotentialClient, authTokenCRM.getAccess_token());
                } else {
                    // Agreement template
                    locationPath = propertiesConfig.getDocStorageBasePath() + propertiesConfig.getDocStorageTemplatePath() +
                            propertiesConfig.getDocStorageAgreementPath();
                }
            } else if (location != null && location.toLowerCase().startsWith("document")) {
                if (location.indexOf('/') > 0) {// This flow for mail merge to download processed file
                    if (classId != null && classId == 1) {                                            // - LNE
                        locationPath = propertiesConfig.getDocStorageLNEBasePath() + "/" + location;
                        log.info("classId--2--LNE path : " + locationPath);
                    } else {                                                                        // Immigration
                        locationPath = propertiesConfig.getDocStorageImmigBasePath() + "/" + location;
                        log.info("classId--2--Immigration path : " + locationPath);
                    }
                } else {
                    // Document template
                    locationPath = propertiesConfig.getDocStorageBasePath() + propertiesConfig.getDocStorageTemplatePath() +
                            propertiesConfig.getDocStorageDocumentPath();
                }
            } else if (location != null && location.toLowerCase().startsWith("clientportal")) {
                if (location.indexOf('/') > 0) {// This flow for mail merge to download processed file
                    if (classId != null && classId == 1) {                                            // - LNE
                        locationPath = propertiesConfig.getDocStorageLNEBasePath() + "/" + location;
                        log.info("classId--2--LNE path : " + locationPath);
                    } else {                                                                        // Immigration
                        locationPath = propertiesConfig.getDocStorageImmigBasePath() + "/" + location;
                        log.info("classId--2--Immigration path : " + locationPath);
                    }
                } else {
                    // Document template
                    locationPath = propertiesConfig.getDocStorageBasePath() + propertiesConfig.getDocStorageTemplatePath() +
                            propertiesConfig.getDocStorageDocumentPath();
                }
            }

            log.info("locationPath : " + locationPath);

            this.fileStorageLocation = Paths.get(locationPath).toAbsolutePath().normalize();
            log.info("fileStorageLocation--------> " + fileStorageLocation);

            if (!Files.exists(fileStorageLocation)) {
                try {
                    Files.createDirectories(this.fileStorageLocation);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw new BadRequestException("Could not create the directory where the uploaded files will be stored.");
                }
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            log.info("fileName------> : " + fileName);

            Map<String, String> mapFileProps = new HashMap<>();
            mapFileProps.put("file", fileName);
            mapFileProps.put("status", "UPLOADED");
            return mapFileProps;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new BadRequestException("Could not store file " + fileName + ". Please try again!");
        }
    }

    /**
     * @param location
     * @param file
     * @return
     * @throws UploadErrorException
     * @throws DbxException
     */
    public String storeMailmergeManualFile(String location, String newFolder, MultipartFile file)
            throws UploadErrorException, DbxException {
//		this.fileStorageLocation = Paths.get(propertiesConfig.getFileUploadDir()).toAbsolutePath().normalize();
        String path = propertiesConfig.getDocStorageBasePath() + propertiesConfig.getDocStorageDocumentPath();
        this.fileStorageLocation = Paths.get(path).toAbsolutePath().normalize();
        log.info("fileStorageLocation : " + fileStorageLocation);
        if (!Files.exists(fileStorageLocation)) {
            try {
                Files.createDirectories(this.fileStorageLocation);
            } catch (Exception ex) {
                throw new BadRequestException(
                        "Could not create the directory where the uploaded files will be stored.");
            }
        }

        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        fileName = fileName.replace(" ", "_");
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new BadRequestException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Remove extn of file
            String fileExtn = fileName.substring(fileName.lastIndexOf("."));
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
            fileName = fileName + "_processed_" + getCurrentTimestamp() + fileExtn;

            // Placing file in the processed location
            Path processedFolder = Paths.get(path + "/" + newFolder).toAbsolutePath().normalize();
            log.info("processedFolder : " + processedFolder);
            if (!Files.exists(processedFolder)) {
                try {
                    Files.createDirectory(processedFolder);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            processedFolder = processedFolder.resolve(fileName);
            Files.copy(file.getInputStream(), processedFolder, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new BadRequestException("Could not store file " + fileName + ". Please try again!");
        }
    }

    /**
     * @return
     */
    public static String getCurrentTimestamp() {
        DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MMddyyyy_HHmmss");
        LocalDateTime datetime = LocalDateTime.now();
        String currentDatetime = datetime.format(newPattern);
        return currentDatetime;
    }

    /**
     * loadFileAsResource
     *
     * @param fileName
     * @return
     */
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new BadRequestException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new BadRequestException("File not found " + fileName);
        }
    }

    /**
     * @param module
     * @param file
     * @param batchName
     * @return
     * @throws UploadErrorException
     * @throws DbxException
     * @throws IOException
     */
    public void storeBatchFile(String module, MultipartFile file, String batchName)
            throws UploadErrorException, DbxException, IOException {
        this.fileStorageLocation = Paths.get(propertiesConfig.getBatchFileUploadDir() + "/" + module).toAbsolutePath()
                .normalize();
        if (!Files.exists(fileStorageLocation)) {
            try {
                Files.createDirectories(this.fileStorageLocation);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new BadRequestException(
                        "Could not create the directory where the uploaded files will be stored.");
            }
        }

        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        log.info("filename : " + fileName);

        fileName = fileName.replace(" ", "_");
        log.info("filename after: " + fileName);

        // Copy file to the target location (Replacing existing file with the same name)
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        log.info("File path : " + targetLocation);

//		if (batchName.equalsIgnoreCase ("client-general")) {
//			clientGeneral (file);
//		} else if (batchName.equalsIgnoreCase ("matter-general")) {
//			matterGeneral (file);
//		} else if (batchName.equalsIgnoreCase ("matter-assignment")) {
//			matterAssignment (file);
//		} else if (batchName.equalsIgnoreCase ("client-notes")) {
//			clientNotes (file);
//		} else if (batchName.equalsIgnoreCase ("matter-notes")) {
//			matterNotes (file);
//		} else if (batchName.equalsIgnoreCase ("time-tickets")) {
//			timeTickets (file);
//		} else if (batchName.equalsIgnoreCase ("matter-expense")) {
//			matterExpense (file);
//		} else if (batchName.equalsIgnoreCase ("matter-rate")) {
//			matterRate (file);
//		} 
    }

    /**
     * @param file
     * @return
     * @throws Exception
     */
    public Map<String, String> storeFile(MultipartFile file) throws Exception {
        this.fileStorageLocation = Paths.get(propertiesConfig.getFileUploadDir()).toAbsolutePath().normalize();
        if (!Files.exists(fileStorageLocation)) {
            try {
                Files.createDirectories(this.fileStorageLocation);
            } catch (Exception ex) {
                throw new BadRequestException("Could not create the directory where the uploaded files will be stored.");
            }
        }

        log.info("loca : " + fileStorageLocation);

        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        log.info("filename before: " + fileName);
        fileName = fileName.replace(" ", "_");
        log.info("filename after: " + fileName);
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new BadRequestException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            log.info("Copied : " + targetLocation);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new BadRequestException("Could not store file " + fileName + ". Please try again!");
        }
        return null;
    }
    
    /**
     * 
     * @param file
     * @return
     * @throws Exception
     */
	public byte[] downloadFile(String file) throws Exception {
		byte[] fileContent = sharePointService.doDownloadFile(file);
		log.info("" + fileContent);
		return fileContent;
	}

    /**
     * @param file
     */
//    private void matterRate(MultipartFile file) {
//        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
//            HeaderColumnNameMappingStrategy<MatterRate> strategy = new HeaderColumnNameMappingStrategy<>();
//            strategy.setType(MatterRate.class);
//
//            // create csv bean reader
//            CsvToBean<MatterRate> csvToBean = new CsvToBeanBuilder<MatterRate>(reader)
//                    .withType(MatterRate.class)
//                    .withIgnoreLeadingWhiteSpace(true)
//                    .withSkipLines(1)
//                    .build();
//
//            // convert `CsvToBean` object to list of users
//            List<MatterRate> listMatterRates = csvToBean.parse();
//            log.info("listMatterExpenses:" + listMatterRates);
//
//            MatterRate[] arrMatterExpense = listMatterRates.toArray(MatterRate[]::new);
//            AuthToken authTokenForMgmtService = authTokenService.getManagementServiceAuthToken();
//            managementService.createBulkMatterRates(arrMatterExpense, "BATCH_UPLOAD",
//                    authTokenForMgmtService.getAccess_token());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * @param file
//     */
//    private void matterExpense(MultipartFile file) {
//        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
//            HeaderColumnNameMappingStrategy<MatterExpense> strategy = new HeaderColumnNameMappingStrategy<>();
//            strategy.setType(MatterExpense.class);
//
//            // create csv bean reader
//            CsvToBean<MatterExpense> csvToBean = new CsvToBeanBuilder<MatterExpense>(reader)
//                    .withType(MatterExpense.class)
//                    .withIgnoreLeadingWhiteSpace(true)
//                    .withSkipLines(1)
//                    .build();
//
//            // convert `CsvToBean` object to list of users
//            List<MatterExpense> listMatterExpenses = csvToBean.parse();
//            log.info("listMatterExpenses:" + listMatterExpenses);
//
//            MatterExpense[] arrMatterExpense = listMatterExpenses.toArray(MatterExpense[]::new);
//            AuthToken authTokenForMgmtService = authTokenService.getManagementServiceAuthToken();
//            managementService.createBulkMatterExpenses(arrMatterExpense, "BATCH_UPLOAD",
//                    authTokenForMgmtService.getAccess_token());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * @param file
//     */
//    private void timeTickets(MultipartFile file) {
//        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
//            HeaderColumnNameMappingStrategy<MatterTimeTicket> strategy = new HeaderColumnNameMappingStrategy<>();
//            strategy.setType(MatterTimeTicket.class);
//
//            // create csv bean reader
//            CsvToBean<MatterTimeTicket> csvToBean = new CsvToBeanBuilder<MatterTimeTicket>(reader)
//                    .withType(MatterTimeTicket.class)
//                    .withIgnoreLeadingWhiteSpace(true)
//                    .withSkipLines(1)
//                    .build();
//
//            // convert `CsvToBean` object to list of users
//            List<MatterTimeTicket> listMatterTimeTickets = csvToBean.parse();
//            log.info("listMatterTimeTickets:" + listMatterTimeTickets);
//
//            MatterTimeTicket[] arrMatterTimeTicket = listMatterTimeTickets.toArray(MatterTimeTicket[]::new);
//            AuthToken authTokenForMgmtService = authTokenService.getManagementServiceAuthToken();
//            managementService.createBulkMatterTimeTickets(arrMatterTimeTicket, "BATCH_UPLOAD",
//                    authTokenForMgmtService.getAccess_token());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * @param file
//     */
//    private void matterNotes(MultipartFile file) {
//        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
//            HeaderColumnNameMappingStrategy<MatterNote> strategy = new HeaderColumnNameMappingStrategy<>();
//            strategy.setType(MatterNote.class);
//
//            // create csv bean reader
//            CsvToBean<MatterNote> csvToBean = new CsvToBeanBuilder<MatterNote>(reader)
//                    .withType(MatterNote.class)
//                    .withIgnoreLeadingWhiteSpace(true)
//                    .withSkipLines(1)
//                    .build();
//
//            // convert `CsvToBean` object to list of users
//            List<MatterNote> listMatterNotes = csvToBean.parse();
//            log.info("listMatterNotes:" + listMatterNotes);
//
//            MatterNote[] arrMatterNote = listMatterNotes.toArray(MatterNote[]::new);
//            AuthToken authTokenForMgmtService = authTokenService.getManagementServiceAuthToken();
//            managementService.createBulkMatterNotes(arrMatterNote, "BATCH_UPLOAD",
//                    authTokenForMgmtService.getAccess_token());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * @param file
//     * @throws IOException
//     */
//    private void matterGeneral(MultipartFile file) throws IOException {
//        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
//            HeaderColumnNameMappingStrategy<MatterGenAcc> strategy = new HeaderColumnNameMappingStrategy<>();
//            strategy.setType(MatterGenAcc.class);
//
//            // create csv bean reader
//            CsvToBean<MatterGenAcc> csvToBean = new CsvToBeanBuilder<MatterGenAcc>(reader)
//                    .withType(MatterGenAcc.class)
//                    .withIgnoreLeadingWhiteSpace(true)
//                    .withSkipLines(1)
//                    .build();
//
//            // convert `CsvToBean` object to list of users
//            List<MatterGenAcc> listMatterGenerals = csvToBean.parse();
//            log.info("listMatterGenerals:" + listMatterGenerals);
//            MatterGenAcc[] arrMatterGenAcc = listMatterGenerals.toArray(MatterGenAcc[]::new);
//            AuthToken authTokenForMgmtService = authTokenService.getManagementServiceAuthToken();
//            managementService.createBulkMatterGenerals(arrMatterGenAcc, "BATCH_UPLOAD",
//                    authTokenForMgmtService.getAccess_token());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * @param file
//     */
//    private void matterAssignment(MultipartFile file) {
//        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
//            HeaderColumnNameMappingStrategy<MatterAssignment> strategy = new HeaderColumnNameMappingStrategy<>();
//            strategy.setType(MatterAssignment.class);
//
//            // create csv bean reader
//            CsvToBean<MatterAssignment> csvToBean = new CsvToBeanBuilder<MatterAssignment>(reader)
//                    .withType(MatterAssignment.class)
//                    .withIgnoreLeadingWhiteSpace(true)
//                    .withSkipLines(1)
//                    .build();
//
//            // convert `CsvToBean` object to list of users
//            List<MatterAssignment> listMatterAssignments = csvToBean.parse();
//            log.info("listMatterAssignments:" + listMatterAssignments);
//            MatterAssignment[] arrMatterAssignment = listMatterAssignments.toArray(MatterAssignment[]::new);
//            AuthToken authTokenForMgmtService = authTokenService.getManagementServiceAuthToken();
//            managementService.createBulkMatterAssignments(arrMatterAssignment, "BATCH_UPLOAD",
//                    authTokenForMgmtService.getAccess_token());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * @param file
//     * @throws IOException
//     */
//    private void clientGeneral(MultipartFile file) throws IOException {
//        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
//            HeaderColumnNameMappingStrategy<ClientGeneral> strategy = new HeaderColumnNameMappingStrategy<>();
//            strategy.setType(ClientGeneral.class);
//
//            // create csv bean reader
//            CsvToBean<ClientGeneral> csvToBean = new CsvToBeanBuilder<ClientGeneral>(reader)
//                    .withType(ClientGeneral.class)
//                    .withIgnoreLeadingWhiteSpace(true)
//                    .withSkipLines(1)
//                    .build();
//
//            // convert `CsvToBean` object to list of users
//            List<ClientGeneral> listClientGenerals = csvToBean.parse();
//            log.info("listClientGenerals:" + listClientGenerals);
//            ClientGeneral[] arrClientGeneral = listClientGenerals.toArray(ClientGeneral[]::new);
//            AuthToken authTokenForMgmtService = authTokenService.getManagementServiceAuthToken();
//            managementService.createBulkClientGenerals(arrClientGeneral, "BATCH_UPLOAD",
//                    authTokenForMgmtService.getAccess_token());
//        }
//    }
//
//    /**
//     * @param file
//     * @throws IOException
//     */
//    private void clientNotes(MultipartFile file) throws IOException {
//        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
//            HeaderColumnNameMappingStrategy<ClientNote> strategy = new HeaderColumnNameMappingStrategy<>();
//            strategy.setType(ClientNote.class);
//
//            // create csv bean reader
//            CsvToBean<ClientNote> csvToBean = new CsvToBeanBuilder<ClientNote>(reader).withType(ClientNote.class)
//                    .withIgnoreLeadingWhiteSpace(true).withSkipLines(1).build();
//
//            // convert `CsvToBean` object to list of users
//            List<ClientNote> listClientNotes = csvToBean.parse();
//            log.info("listClientNotes:" + listClientNotes);
//            ClientNote[] arrClientNote = listClientNotes.toArray(ClientNote[]::new);
//            AuthToken authTokenForMgmtService = authTokenService.getManagementServiceAuthToken();
//            managementService.createBulkClientNotes(arrClientNote, "BATCH_UPLOAD",
//                    authTokenForMgmtService.getAccess_token());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
