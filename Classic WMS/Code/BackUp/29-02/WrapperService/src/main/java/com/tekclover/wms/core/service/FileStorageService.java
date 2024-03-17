package com.tekclover.wms.core.service;

import com.tekclover.wms.core.config.PropertiesConfig;
import com.tekclover.wms.core.exception.BadRequestException;
import com.tekclover.wms.core.exception.CustomErrorResponse;
import com.tekclover.wms.core.exception.RestResponseEntityExceptionHandler;
import com.tekclover.wms.core.model.auth.AuthToken;
import com.tekclover.wms.core.model.idmaster.FileNameForEmail;
import com.tekclover.wms.core.model.idmaster.MailingReport;
import com.tekclover.wms.core.model.transaction.SOHeader;
import com.tekclover.wms.core.model.transaction.SOLine;
import com.tekclover.wms.core.model.transaction.ShipmentOrder;

import com.tekclover.wms.core.model.warehouse.inbound.InterWarehouseTransferInHeader;
import com.tekclover.wms.core.model.warehouse.inbound.WarehouseApiResponse;
import com.tekclover.wms.core.model.warehouse.inbound.almailem.*;
import com.tekclover.wms.core.model.warehouse.outbound.almailem.*;
import com.tekclover.wms.core.repository.MailingReportRepository;
import com.tekclover.wms.core.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Slf4j
@Service
public class FileStorageService {

    @Autowired
    PropertiesConfig propertiesConfig;

    @Autowired
    AuthTokenService authTokenService;
    @Autowired
    IDMasterService idMasterService;

    @Autowired
    MailingReportRepository mailingReportRepository;

    //-----------------------------------------------------------------------------------
    @Autowired
    TransactionService transactionService;
    //-----------------------------------------------------------------------------------

    private Path fileStorageLocation = null;

    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    private String getIDMasterServiceApiUrl() {
        return propertiesConfig.getIdmasterServiceUrl();
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
                throw new BadRequestException(
                        "Could not create the directory where the uploaded files will be stored.");
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
        return Collections.singletonMap("message", "File uploaded successfully!");
    }

    /**
     * @param file
     * @return
     * @throws Exception
     */
    public Map<String, String> processSOOrders(MultipartFile file) throws Exception {
        this.fileStorageLocation = Paths.get(propertiesConfig.getFileUploadDir()).toAbsolutePath().normalize();
        if (!Files.exists(fileStorageLocation)) {
            try {
                Files.createDirectories(this.fileStorageLocation);
            } catch (Exception ex) {
                throw new BadRequestException(
                        "Could not create the directory where the uploaded files will be stored.");
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

            List<List<String>> allRowsList = readExcelData(targetLocation.toFile());
            List<ShipmentOrder> shipmentOrders = prepSOData(allRowsList);
            log.info("shipmentOrders : " + shipmentOrders);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new BadRequestException("Could not store file " + fileName + ". Please try again!");
        }
        return null;
    }

    /**
     * @param location
     * @param file
     * @return
     * @throws Exception
     * @throws BadRequestException
     */
    public Map<String, String> storingFile(String location, MultipartFile file) throws Exception {

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

            if (location != null && location.toLowerCase().startsWith("document")) {
                if (location.indexOf('/') > 0) {
                    locationPath = propertiesConfig.getDocStorageBasePath() + "/" + location;
                } else {
                    // Document template
                    locationPath = propertiesConfig.getDocStorageBasePath() + propertiesConfig.getDocStorageDocumentPath();
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
//			if(fileName.toLowerCase().startsWith("110")){
//				if(fileName.toLowerCase().contains("delivery")){
//					AuthToken authTokenForSetupService = authTokenService.getIDMasterServiceAuthToken();
//					FileNameForEmail dbFileNameForEmail = new FileNameForEmail();
//					dbFileNameForEmail.setDelivery110(fileName);
//					dbFileNameForEmail.setReportDate(DateUtils.getCurrentDateWithoutTimestamp());
//					dbFileNameForEmail.setDeletionIndicator(0L);
//					FileNameForEmail fileNameForEmail = idMasterService.updateFileNameForEmail(dbFileNameForEmail, authTokenForSetupService.getAccess_token());
//				}else if(fileName.toLowerCase().contains("dispatch")){
//					AuthToken authTokenForSetupService = authTokenService.getIDMasterServiceAuthToken();
//					FileNameForEmail dbFileNameForEmail = new FileNameForEmail();
//					dbFileNameForEmail.setDispatch110(fileName);
//					dbFileNameForEmail.setReportDate(DateUtils.getCurrentDateWithoutTimestamp());
//					dbFileNameForEmail.setDeletionIndicator(0L);
//					FileNameForEmail fileNameForEmail = idMasterService.updateFileNameForEmail(dbFileNameForEmail, authTokenForSetupService.getAccess_token());
//				}
//			}
//			if(fileName.toLowerCase().startsWith("111")){
//				if(fileName.toLowerCase().contains("delivery")){
//					AuthToken authTokenForSetupService = authTokenService.getIDMasterServiceAuthToken();
//					FileNameForEmail dbFileNameForEmail = new FileNameForEmail();
//					dbFileNameForEmail.setDelivery111(fileName);
//					dbFileNameForEmail.setReportDate(DateUtils.getCurrentDateWithoutTimestamp());
//					dbFileNameForEmail.setDeletionIndicator(0L);
//					FileNameForEmail fileNameForEmail = idMasterService.updateFileNameForEmail(dbFileNameForEmail, authTokenForSetupService.getAccess_token());
//				}else if(fileName.toLowerCase().contains("dispatch")){
//					AuthToken authTokenForSetupService = authTokenService.getIDMasterServiceAuthToken();
//					FileNameForEmail dbFileNameForEmail = new FileNameForEmail();
//					dbFileNameForEmail.setDispatch111(fileName);
//					dbFileNameForEmail.setReportDate(DateUtils.getCurrentDateWithoutTimestamp());
//					dbFileNameForEmail.setDeletionIndicator(0L);
//					FileNameForEmail fileNameForEmail = idMasterService.updateFileNameForEmail(dbFileNameForEmail, authTokenForSetupService.getAccess_token());
//				}
//			}
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
     * @param file
     * @return
     * @throws Exception
     * @throws BadRequestException
     */
//	public Map<String, String> storingFileMailingReport(String location, MultipartFile file)
//			throws Exception {
//
//		// Normalize file name
//		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//		log.info("filename before: " + fileName);
//		fileName = fileName.replace(" ", "_");
//		log.info("filename after: " + fileName);
//
//		String locationPath = null;
//		try {
//			// Check if the file's name contains invalid characters
//			if (fileName.contains("..")) {
//				throw new BadRequestException("Sorry! Filename contains invalid path sequence " + fileName);
//			}
//
//			if (location != null && location.toLowerCase().startsWith("document")) {
//				if (location.indexOf('/') > 0) {
//					locationPath = propertiesConfig.getDocStorageBasePath() + "/" + location;
//				} else {
//					// Document template
//					locationPath = propertiesConfig.getDocStorageBasePath() + propertiesConfig.getDocStorageDocumentPath();
//				}
//			}
//
//			log.info("locationPath : " + locationPath);
//
//			this.fileStorageLocation = Paths.get(locationPath).toAbsolutePath().normalize();
//			log.info("fileStorageLocation--------> " + fileStorageLocation);
//
//			if (!Files.exists(fileStorageLocation)) {
//				try {
//					Files.createDirectories(this.fileStorageLocation);
//				} catch (Exception ex) {
//					ex.printStackTrace();
//					throw new BadRequestException("Could not create the directory where the uploaded files will be stored.");
//				}
//			}
//
//			AuthToken authTokenForSetupService = authTokenService.getIDMasterServiceAuthToken();
//
//			MailingReport newMailingReport = new MailingReport();
//
//			newMailingReport.setReportDate(DateUtils.getCurrentDateWithoutTimestamp());
//			newMailingReport.setDeletionIndicator(0L);
//			newMailingReport.setCompanyCodeId("1000");		//HardCode
//			newMailingReport.setPlantId("1001");			//HardCode
//			newMailingReport.setLanguageId("EN");			//HardCode
//			newMailingReport.setMailSent("0");				//HardCode
//			newMailingReport.setMailSentFailed("0");		//HardCode
//
//			if(fileName.toLowerCase().startsWith("110")){
//
//				newMailingReport.setWarehouseId("110");
//
//			}
//			if(fileName.toLowerCase().startsWith("111")){
//
//				newMailingReport.setWarehouseId("111");
//
//			}
//
//			Optional<MailingReport> dbMailingReport = mailingReportRepository
//														.findBycompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndFileNameAndDeletionIndicator(
//																"1000",			//HardCode
//																"1001", 						//HardCode
//																newMailingReport.getWarehouseId(),
//																"EN",					//HardCode
//																fileName, 0L );
//
//			Long countMailingReportByDate = mailingReportRepository.countByReportDateAndWarehouseId(
//					DateUtils.getCurrentDateWithoutTimestamp(),
//					newMailingReport.getWarehouseId());
//
//			if(countMailingReportByDate == null) {
//				countMailingReportByDate = 0L;
//			}
//
//			if(dbMailingReport.isEmpty()) {
//				if (countMailingReportByDate != 1) {
//
//					// Copy file to the target location (Replacing existing file with the same name)
//					Path targetLocation = this.fileStorageLocation.resolve(fileName);
//					Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//
//					newMailingReport.setFileName(fileName);
//					newMailingReport.setUploaded(true);
//
//					Boolean uploadedMailingReport = idMasterService.createMailingReport(newMailingReport, authTokenForSetupService.getAccess_token());
//
//					if (uploadedMailingReport) {
//
//						Map<String, String> mapFileProps = new HashMap<>();
//						mapFileProps.put("file", fileName);
//						mapFileProps.put("location", location);
//						mapFileProps.put("status", "UPLOADED");
//						return mapFileProps;
//
//					} else {
//						throw new BadRequestException("Could not store file " + fileName + ". Please try again!");
//					}
//				}
//			}
//		} catch (IOException ex) {
//			ex.printStackTrace();
//			throw new BadRequestException("Could not store file " + fileName + ". Please try again!");
//		}
//		return null;
//	}
    private List<List<String>> readExcelData(File file) {
        try {
            Workbook workbook = new XSSFWorkbook(file);
            workbook.setMissingCellPolicy(Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);

            List<List<String>> allRowsList = new ArrayList<>();
            DataFormatter fmt = new DataFormatter();
            for (int rn = sheet.getFirstRowNum(); rn <= sheet.getLastRowNum(); rn++) {
                List<String> listUploadData = new ArrayList<String>();
                Row row = sheet.getRow(rn);
                log.info("Row:  " + row.getRowNum());
                if (row == null) {
                    // There is no data in this row, handle as needed
                } else if (row.getRowNum() != 0) {
                    for (int cn = 0; cn <= row.getLastCellNum(); cn++) {
                        Cell cell = row.getCell(cn);
                        if (cell == null) {
                            log.info("cell empty: " + cell);
                            listUploadData.add("");
                        } else {
                            String cellStr = fmt.formatCellValue(cell);
                            log.info("cellStr: " + cellStr);
                            listUploadData.add(cellStr);
                        }
                    }
                    allRowsList.add(listUploadData);
                }
            }

//			Iterator<Row> iterator = sheet.iterator();
//			List<List<String>> allRowsList = new ArrayList<>();
//			while (iterator.hasNext()) {
//				Row currentRow = iterator.next();
//				Iterator<Cell> cellIterator = currentRow.iterator();
//
//				// Moving to data row instead of header row
//				currentRow = iterator.next();
//				cellIterator = currentRow.iterator();
//
//				List<String> listUploadData = new ArrayList<String>();
//				while (cellIterator.hasNext()) {
//					Cell currentCell = cellIterator.next();
//					log.info("===currentCell===== " + currentCell);
//					if (currentCell.getColumnIndex() == 7) {
//						listUploadData.add(" ");
//						log.info("=#= " + listUploadData.size());
//					}
//					if (currentCell.getCellType() == CellType.STRING) {
//						log.info(currentCell.getStringCellValue() + "*****");
//						if (currentCell.getStringCellValue() != null
//								&& !currentCell.getStringCellValue().trim().isEmpty()) {
//							listUploadData.add(currentCell.getStringCellValue());
////							log.info("== " + listUploadData.size());
//						} else {
//							listUploadData.add(" ");
////							log.info("=#= " + listUploadData.size());
//						}
//					} else if (currentCell.getCellType() == CellType.NUMERIC) {
////						log.info(currentCell.getNumericCellValue() + "--");
//						listUploadData.add(String.valueOf(currentCell.getNumericCellValue()));
//					}
//				}
//				log.info("=#= " + listUploadData);
//				allRowsList.add(listUploadData);
//			}
            log.info("list data: " + allRowsList);
            return allRowsList;
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    /**
     * 0 - requiredDeliveryDate
     * 1 - storeID
     * 2 - storeName
     * 3 - transferOrderNumber
     * 4 - wareHouseId
     * 5 - lineReference
     * 6 - orderType
     * 7 - orderedQty
     * 8 - sku
     * 9 - skuDescription
     * 10 - uom
     *
     * @param allRowsList
     * @return
     */
    private List<ShipmentOrder> prepSOData(List<List<String>> allRowsList) {
        List<ShipmentOrder> shipmentOrderList = new ArrayList<>();

        for (List<String> listUploadedData : allRowsList) {
            Set<SOHeader> setSOHeader = new HashSet<>();
            List<SOLine> soLines = new ArrayList<>();

            // Header
            SOHeader soHeader = null;
            boolean oneTimeAllow = true;
            for (String column : listUploadedData) {
                if (oneTimeAllow) {
                    soHeader = new SOHeader();
                    soHeader.setRequiredDeliveryDate(listUploadedData.get(0));
                    soHeader.setStoreID(listUploadedData.get(1));
                    soHeader.setStoreName(listUploadedData.get(2));
                    soHeader.setTransferOrderNumber(listUploadedData.get(3));
                    soHeader.setWareHouseId(listUploadedData.get(4));
                    setSOHeader.add(soHeader);
                }
                oneTimeAllow = false;

                // Line
                SOLine soLine = new SOLine();
                soLine.setLineReference(Long.valueOf(listUploadedData.get(5)));
                soLine.setOrderType(listUploadedData.get(6));
                soLine.setOrderedQty(Double.valueOf(listUploadedData.get(7)));
                soLine.setSku(listUploadedData.get(8));
                soLine.setSkuDescription(listUploadedData.get(9));
                soLine.setUom(listUploadedData.get(10));
                soLines.add(soLine);
            }

            ShipmentOrder shipmentOrder = new ShipmentOrder();
            shipmentOrder.setSoHeader(soHeader);
            shipmentOrder.setSoLine(soLines);
            shipmentOrderList.add(shipmentOrder);
        }
        return shipmentOrderList;
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


    //=============================================OrderProcess===================================================================

    /**
     * @param file
     * @return
     * @throws Exception
     */
    public Map<String, String> processAsnOrders(MultipartFile file) throws Exception {
        this.fileStorageLocation = Paths.get(propertiesConfig.getFileUploadDir()).toAbsolutePath().normalize();
        if (!Files.exists(fileStorageLocation)) {
            try {
                Files.createDirectories(this.fileStorageLocation);
            } catch (Exception ex) {
                throw new BadRequestException(
                        "Could not create the directory where the uploaded files will be stored.");
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

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            log.info("Copied : " + targetLocation);

            List<List<String>> allRowsList = readExcelData(targetLocation.toFile());
            List<ASNV2> asnV2Orders = prepAsnData(allRowsList);
            log.info("asnOrders : " + asnV2Orders);

            // Uploading Orders
            WarehouseApiResponse[] dbWarehouseApiResponse = new WarehouseApiResponse[0];
            AuthToken authToken = authTokenService.getTransactionServiceAuthToken();
            dbWarehouseApiResponse = transactionService.postASNV2Upload(asnV2Orders, "Uploaded", authToken.getAccess_token());

            if (dbWarehouseApiResponse != null) {
                Map<String, String> mapFileProps = new HashMap<>();
                mapFileProps.put("file", fileName);
                mapFileProps.put("status", "UPLOADED SUCCESSFULLY");
                return mapFileProps;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new BadRequestException("Could not store file " + fileName + ". Please try again!");
        }
        return null;
    }

    /**
     * @param file
     * @return
     * @throws Exception
     */
    public Map<String, String> processSalesOrderReturnV2Orders(MultipartFile file) throws Exception {
        this.fileStorageLocation = Paths.get(propertiesConfig.getFileUploadDir()).toAbsolutePath().normalize();
        if (!Files.exists(fileStorageLocation)) {
            try {
                Files.createDirectories(this.fileStorageLocation);
            } catch (Exception ex) {
                throw new BadRequestException(
                        "Could not create the directory where the uploaded files will be stored.");
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

            List<List<String>> allRowsList = readExcelData(targetLocation.toFile());
            List<SaleOrderReturnV2> soReturn = prepSOReturnData(allRowsList);
            log.info("wh2whOrders : " + soReturn);

            // Uploading Orders
            WarehouseApiResponse[] dbWarehouseApiResponse = new WarehouseApiResponse[0];
            AuthToken authToken = authTokenService.getTransactionServiceAuthToken();
            dbWarehouseApiResponse = transactionService.postSalesOrderReturnUploadV2(soReturn, "Uploaded", authToken.getAccess_token());

            if (dbWarehouseApiResponse != null) {
                Map<String, String> mapFileProps = new HashMap<>();
                mapFileProps.put("file", fileName);
                mapFileProps.put("status", "UPLOADED SUCCESSFULLY");
                return mapFileProps;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new BadRequestException("Could not store file " + fileName + ". Please try again!");
        }
        return null;
    }


    // STOCKRECEIPT

    /**
     * @param file
     * @return
     * @throws Exception
     */
    public Map<String, String> processStockReceiptUpload(MultipartFile file) throws Exception {
        this.fileStorageLocation = Paths.get(propertiesConfig.getFileUploadDir()).toAbsolutePath().normalize();
        if (!Files.exists(fileStorageLocation)) {
            try {
                Files.createDirectories(this.fileStorageLocation);
            } catch (Exception ex) {
                throw new BadRequestException(
                        "Could not create the directory where the uploaded files will be stored.");
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

            List<List<String>> allRowsList = readExcelData(targetLocation.toFile());
            List<StockReceipt> stockReceiptList = prepStockReceiptData(allRowsList);
            log.info("wh2whOrders : " + stockReceiptList);

            // Uploading Orders
            WarehouseApiResponse[] dbWarehouseApiResponse = new WarehouseApiResponse[0];
            AuthToken authToken = authTokenService.getTransactionServiceAuthToken();
            dbWarehouseApiResponse = transactionService.postStockReceiptUpload(stockReceiptList, "Uploaded", authToken.getAccess_token());

            if (dbWarehouseApiResponse != null) {
                Map<String, String> mapFileProps = new HashMap<>();
                mapFileProps.put("file", fileName);
                mapFileProps.put("status", "UPLOADED SUCCESSFULLY");
                return mapFileProps;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new BadRequestException("Could not store file " + fileName + ". Please try again!");
        }
        return null;
    }

    /**
     * @param file
     * @return
     * @throws Exception
     */
    public Map<String, String> processInterWarehouseTransferInOrders(MultipartFile file) throws Exception {
        this.fileStorageLocation = Paths.get(propertiesConfig.getFileUploadDir()).toAbsolutePath().normalize();
        if (!Files.exists(fileStorageLocation)) {
            try {
                Files.createDirectories(this.fileStorageLocation);
            } catch (Exception ex) {
                throw new BadRequestException(
                        "Could not create the directory where the uploaded files will be stored.");
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

            List<List<String>> allRowsList = readExcelData(targetLocation.toFile());
            List<InterWarehouseTransferInV2> wh2whOrders = prepInterwareHouseInData(allRowsList);
            log.info("wh2whOrders : " + wh2whOrders);

            // Uploading Orders
            WarehouseApiResponse[] dbWarehouseApiResponse = new WarehouseApiResponse[0];
            AuthToken authToken = authTokenService.getTransactionServiceAuthToken();
            dbWarehouseApiResponse = transactionService.postInterWarehouseTransferInUploadV2(wh2whOrders, "Uploaded", authToken.getAccess_token());

            if (dbWarehouseApiResponse != null) {
                Map<String, String> mapFileProps = new HashMap<>();
                mapFileProps.put("file", fileName);
                mapFileProps.put("status", "UPLOADED SUCCESSFULLY");
                return mapFileProps;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new BadRequestException("Could not store file " + fileName + ". Please try again!");
        }
        return null;
    }


    /**
     * @param file
     * @return
     * @throws Exception
     */
    public Map<String, String> processB2bTransferInOrders(MultipartFile file) throws Exception {
        this.fileStorageLocation = Paths.get(propertiesConfig.getFileUploadDir()).toAbsolutePath().normalize();
        if (!Files.exists(fileStorageLocation)) {
            try {
                Files.createDirectories(this.fileStorageLocation);
            } catch (Exception ex) {
                throw new BadRequestException(
                        "Could not create the directory where the uploaded files will be stored.");
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

            List<List<String>> allRowsList = readExcelData(targetLocation.toFile());
            List<B2bTransferIn> b2bTransferIn = prePB2bTransferIn(allRowsList);
            log.info("wh2whOrders : " + b2bTransferIn);

            // Uploading Orders
            WarehouseApiResponse[] dbWarehouseApiResponse = new WarehouseApiResponse[0];
            AuthToken authToken = authTokenService.getTransactionServiceAuthToken();
            dbWarehouseApiResponse = transactionService.postB2bTransferInUploadV2(b2bTransferIn, "Uploaded", authToken.getAccess_token());

            if (dbWarehouseApiResponse != null) {
                Map<String, String> mapFileProps = new HashMap<>();
                mapFileProps.put("file", fileName);
                mapFileProps.put("status", "UPLOADED SUCCESSFULLY");
                return mapFileProps;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new BadRequestException("Could not store file " + fileName + ". Please try again!");
        }
        return null;
    }


    //OutBound
    // InterWarehouseTransferIn

    /**
     * @param file
     * @return
     * @throws Exception
     */
    public Map<String, String> processInterWarehouseTransferOutfOrders(MultipartFile file) throws Exception {
        this.fileStorageLocation = Paths.get(propertiesConfig.getFileUploadDir()).toAbsolutePath().normalize();
        if (!Files.exists(fileStorageLocation)) {
            try {
                Files.createDirectories(this.fileStorageLocation);
            } catch (Exception ex) {
                throw new BadRequestException(
                        "Could not create the directory where the uploaded files will be stored.");
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

            List<List<String>> allRowsList = readExcelData(targetLocation.toFile());
            List<InterWarehouseTransferOutV2> interWarehouseTransferOutV2s = prepIWTransferOutData(allRowsList);
            log.info("wh2whOrders : " + interWarehouseTransferOutV2s);

            // Uploading Orders
            WarehouseApiResponse[] dbWarehouseApiResponse = new WarehouseApiResponse[0];
            AuthToken authToken = authTokenService.getTransactionServiceAuthToken();
            dbWarehouseApiResponse = transactionService.postInterWarehouseTransferOut(interWarehouseTransferOutV2s, "Uploaded", authToken.getAccess_token());

            if (dbWarehouseApiResponse != null) {
                Map<String, String> mapFileProps = new HashMap<>();
                mapFileProps.put("file", fileName);
                mapFileProps.put("status", "UPLOADED SUCCESSFULLY");
                return mapFileProps;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new BadRequestException("Could not store file " + fileName + ". Please try again!");
        }
        return null;
    }

    // ShipmentOrderV2 - Upload
    public Map<String, String> processShipmentOrderUpload(MultipartFile file) throws Exception {
        this.fileStorageLocation = Paths.get(propertiesConfig.getFileUploadDir()).toAbsolutePath().normalize();
        if (!Files.exists(fileStorageLocation)) {
            try {
                Files.createDirectories(this.fileStorageLocation);
            } catch (Exception ex) {
                throw new BadRequestException(
                        "Could not create the directory where the uploaded files will be stored.");
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

            List<List<String>> allRowsList = readExcelData(targetLocation.toFile());
            List<ShipmentOrderV2> shipmentOrders = prepShipmentOrderOutUpload(allRowsList);
            log.info("wh2whOrders : " + shipmentOrders);

            // Uploading Orders
            WarehouseApiResponse[] dbWarehouseApiResponse = new WarehouseApiResponse[0];
            AuthToken authToken = authTokenService.getTransactionServiceAuthToken();
            dbWarehouseApiResponse = transactionService.postShipmentOrderV2(shipmentOrders, "Uploaded", authToken.getAccess_token());

            if (dbWarehouseApiResponse != null) {
                Map<String, String> mapFileProps = new HashMap<>();
                mapFileProps.put("file", fileName);
                mapFileProps.put("status", "UPLOADED SUCCESSFULLY");
                return mapFileProps;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new BadRequestException("Could not store file " + fileName + ". Please try again!");
        }
        return null;
    }

    // ReturnPoV2 - Upload
    public Map<String, String> processReturnPoV2Upload(MultipartFile file) throws Exception {
        this.fileStorageLocation = Paths.get(propertiesConfig.getFileUploadDir()).toAbsolutePath().normalize();
        if (!Files.exists(fileStorageLocation)) {
            try {
                Files.createDirectories(this.fileStorageLocation);
            } catch (Exception ex) {
                throw new BadRequestException(
                        "Could not create the directory where the uploaded files will be stored.");
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

            List<List<String>> allRowsList = readExcelData(targetLocation.toFile());
            List<ReturnPOV2> returnPOV2List = prepReturnPoV2Upload(allRowsList);
            log.info("wh2whOrders : " + returnPOV2List);

            // Uploading Orders
            WarehouseApiResponse[] dbWarehouseApiResponse = new WarehouseApiResponse[0];
            AuthToken authToken = authTokenService.getTransactionServiceAuthToken();
            dbWarehouseApiResponse = transactionService.postReturnPoV2(returnPOV2List, "Uploaded", authToken.getAccess_token());

            if (dbWarehouseApiResponse != null) {
                Map<String, String> mapFileProps = new HashMap<>();
                mapFileProps.put("file", fileName);
                mapFileProps.put("status", "UPLOADED SUCCESSFULLY");
                return mapFileProps;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new BadRequestException("Could not store file " + fileName + ". Please try again!");
        }
        return null;
    }

    //==========================================Order-FieldSet=======================================================
    //ASNV2

    /**
     * @param allRowsList
     * @return
     */
    private List<ASNV2> prepAsnData(List<List<String>> allRowsList) {
        List<ASNV2> orderList = new ArrayList<>();
        Map<String, ASNV2> asnMap = new HashMap<>();
        for (List<String> listUploadedData : allRowsList) {

            String asnNumber = listUploadedData.get(2);
            // Check if ASN number already exists in the map
            ASNV2 asn = asnMap.get(asnNumber);

            // Header
            if (asn == null) {
                asn = new ASNV2();
                ASNHeaderV2 header = new ASNHeaderV2();
                header.setBranchCode(listUploadedData.get(0));
                header.setCompanyCode(listUploadedData.get(1));
                header.setAsnNumber(asnNumber);
                asn.setAsnHeader(header);
                asn.setAsnLine(new ArrayList<>());
                asnMap.put(asnNumber, asn);
            }
            // Line
            ASNLineV2 line = new ASNLineV2();
            line.setLineReference(Long.valueOf(listUploadedData.get(3)));
            line.setSku(listUploadedData.get(4));
            line.setSkuDescription(listUploadedData.get(5));
            line.setContainerNumber(listUploadedData.get(6));
            line.setSupplierCode(listUploadedData.get(7));
            line.setSupplierPartNumber(listUploadedData.get(8));
            line.setManufacturerName(listUploadedData.get(9));
            line.setManufacturerCode(listUploadedData.get(10));
            line.setExpectedDate(listUploadedData.get(11));
            line.setExpectedQty(Double.valueOf(listUploadedData.get(12)));
            line.setUom(listUploadedData.get(13));
            line.setOrigin(listUploadedData.get(14));
            line.setSupplierName(listUploadedData.get(15));
            line.setBrand(listUploadedData.get(16));

            if (listUploadedData.get(17).trim().length() > 0) {
                line.setPackQty(Double.valueOf(listUploadedData.get(17)));
            }
            asn.getAsnLine().add(line);
        }
        orderList.addAll(asnMap.values());

        return orderList;
    }


    // SO Return

    /**
     * @param allRowsList
     * @return
     */
    private List<SaleOrderReturnV2> prepSOReturnData(List<List<String>> allRowsList) {
        List<SaleOrderReturnV2> orderList = new ArrayList<>();
        Map<String, SaleOrderReturnV2> v2Map = new HashMap<>();
        for (List<String> listUploadedData : allRowsList) {

            String transferNumber = listUploadedData.get(2);
            SaleOrderReturnV2 saleOrderReturnV2 = v2Map.get(transferNumber);

            // SaleOrderReturnHeader
            if (saleOrderReturnV2 == null) {
                saleOrderReturnV2 = new SaleOrderReturnV2();
                SOReturnHeaderV2 header = new SOReturnHeaderV2();

                header.setCompanyCode(listUploadedData.get(0));
                header.setBranchCode(listUploadedData.get(1));
                header.setTransferOrderNumber(listUploadedData.get(2));
                saleOrderReturnV2.setSoReturnHeader(header);
                saleOrderReturnV2.setSoReturnLine(new ArrayList<>());
                v2Map.put(transferNumber, saleOrderReturnV2);
            }
            //SOReturnLineV2
            SOReturnLineV2 line = new SOReturnLineV2();
            line.setLineReference(Long.valueOf(listUploadedData.get(3)));
            line.setSku(listUploadedData.get(4));
            line.setSkuDescription(listUploadedData.get(5));
            line.setInvoiceNumber(listUploadedData.get(6));
            line.setStoreID(listUploadedData.get(7));
            line.setSupplierPartNumber(listUploadedData.get(8));
            line.setManufacturerName(listUploadedData.get(9));
            line.setExpectedDate(listUploadedData.get(10));
            line.setExpectedQty(Double.valueOf(listUploadedData.get(11)));
            line.setUom(listUploadedData.get(12));
            line.setOrigin(listUploadedData.get(13));
            line.setManufacturerCode(listUploadedData.get(14));
            line.setBrand(listUploadedData.get(15));
            line.setSalesOrderReference(listUploadedData.get(16));
            line.setPackQty(Double.valueOf(listUploadedData.get(17)));

            if (listUploadedData.get(18).trim().length() > 0) {
                line.setManufacturerFullName(listUploadedData.get(18));
            }
            saleOrderReturnV2.getSoReturnLine().add(line);
        }
        orderList.addAll(v2Map.values());
        return orderList;
    }


    //StockReceipt
    private List<StockReceipt> prepStockReceiptData(List<List<String>> allRowsList) {
        List<StockReceipt> orderList = new ArrayList<>();
        Map<String, StockReceipt> stockReceiptMap = new HashMap<>();
        for (List<String> listUploadedData : allRowsList) {

            String receiptNo = listUploadedData.get(2);
            StockReceipt stockReceipt = stockReceiptMap.get(receiptNo);

            // StockReceiptHeader
            if (stockReceipt == null) {
                stockReceipt = new StockReceipt();

                StockReceiptHeader header = new StockReceiptHeader();
                header.setBranchCode(listUploadedData.get(0));
                header.setCompanyCode(listUploadedData.get(1));
                header.setReceiptNo(listUploadedData.get(2));
                stockReceipt.setStockReceiptHeader(header);
                stockReceipt.setStockReceiptLine(new ArrayList<>());
                stockReceiptMap.put(receiptNo, stockReceipt);
            }

            // StockReceiptLine
            StockReceiptLine line = new StockReceiptLine();
            line.setBranchCode(listUploadedData.get(0));
            line.setCompanyCode(listUploadedData.get(1));
            line.setReceiptNo(listUploadedData.get(2));
            line.setLineNoForEachItem(Long.valueOf(listUploadedData.get(3)));
            line.setItemCode(listUploadedData.get(4));
            line.setItemDescription(listUploadedData.get(5));
            line.setSupplierCode(listUploadedData.get(6));
            line.setSupplierPartNo(listUploadedData.get(7));
            line.setManufacturerShortName(listUploadedData.get(8));
            line.setManufacturerCode(listUploadedData.get(9));
            line.setReceiptDate(listUploadedData.get(10));
            line.setReceiptQty(Double.valueOf(listUploadedData.get(11)));
            line.setUnitOfMeasure(listUploadedData.get(12));
            line.setSupplierName(listUploadedData.get(13));

            if (listUploadedData.size() > 14 && listUploadedData.get(14).trim().length() > 0) {
                line.setManufacturerFullName(listUploadedData.get(14));
            }
            stockReceipt.getStockReceiptLine().add(line);
        }
        orderList.addAll(stockReceiptMap.values());
        return orderList;
    }


    // B2bTransferIn
    private List<B2bTransferIn> prePB2bTransferIn(List<List<String>> allRowsList) {
        List<B2bTransferIn> orderList = new ArrayList<>();
        Map<String, B2bTransferIn> transferInMap = new HashMap<>();
        for (List<String> listUploadedData : allRowsList) {

            String transferNo = listUploadedData.get(2);
            B2bTransferIn b2bTransferIn = transferInMap.get(transferNo);

            // B2bTransferInHeader
            if (b2bTransferIn == null) {
                b2bTransferIn = new B2bTransferIn();
                B2bTransferInHeader header = new B2bTransferInHeader();
                header.setCompanyCode(listUploadedData.get(0));
                header.setBranchCode(listUploadedData.get(1));
                header.setTransferOrderNumber(listUploadedData.get(2));
                header.setSourceCompanyCode(listUploadedData.get(3));
                header.setSourceBranchCode(listUploadedData.get(4));
                header.setTransferOrderDate(listUploadedData.get(5));
                b2bTransferIn.setB2bTransferInHeader(header);
                b2bTransferIn.setB2bTransferLine(new ArrayList<>());
                transferInMap.put(transferNo, b2bTransferIn);
            }

            // B2bTransferInLine
            B2bTransferInLine line = new B2bTransferInLine();
            line.setLineReference(Long.valueOf(listUploadedData.get(6)));
            line.setSku(listUploadedData.get(7));
            line.setSkuDescription(listUploadedData.get(8));
            line.setStoreID(listUploadedData.get(9));
            line.setSupplierPartNumber(listUploadedData.get(10));
            line.setManufacturerName(listUploadedData.get(11));
            line.setExpectedDate(listUploadedData.get(12));
            line.setExpectedQty(Double.valueOf(listUploadedData.get(13)));
            line.setUom(listUploadedData.get(14));
            line.setPackQty(Long.valueOf(listUploadedData.get(15)));
            line.setOrigin(listUploadedData.get(16));
            line.setManufacturerCode(listUploadedData.get(17));
            line.setBrand(listUploadedData.get(18));

            if (listUploadedData.size() > 19 && listUploadedData.get(19).trim().length() > 0) {
                line.setSupplierName(listUploadedData.get(19));
            }
            b2bTransferIn.getB2bTransferLine().add(line);
        }
        orderList.addAll(transferInMap.values());
        return orderList;
    }

    // InterWarehouseTransferInV2
    private List<InterWarehouseTransferInV2> prepInterwareHouseInData(List<List<String>> allRowsList) {
        List<InterWarehouseTransferInV2> whOrderList = new ArrayList<>();
        Map<String, InterWarehouseTransferInV2> transferInV2Map = new HashMap<>();

        for (List<String> listUploadedData : allRowsList) {

            String transNumber = listUploadedData.get(0);
            InterWarehouseTransferInV2 listWHLines = transferInV2Map.get(transNumber);

            // InterWarehouseTransferInHeaderV2
            if (listWHLines == null) {
                listWHLines = new InterWarehouseTransferInV2();
                InterWarehouseTransferInHeaderV2 header = new InterWarehouseTransferInHeaderV2();
                header.setTransferOrderNumber(listUploadedData.get(0));
                header.setToCompanyCode(listUploadedData.get(1));
                header.setToBranchCode(listUploadedData.get(2));

                listWHLines.setInterWarehouseTransferInHeader(header);
                listWHLines.setInterWarehouseTransferInLine(new ArrayList<>());
                transferInV2Map.put(transNumber, listWHLines);
            }

            // InterWarehouseTransferInLineV2
            InterWarehouseTransferInLineV2 line = new InterWarehouseTransferInLineV2();
            line.setFromCompanyCode(listUploadedData.get(3));
            line.setOrigin(listUploadedData.get(4));
            line.setSupplierName(listUploadedData.get(5));
            line.setManufacturerCode(listUploadedData.get(6));
            line.setBrand(listUploadedData.get(7));
            line.setFromBranchCode(listUploadedData.get(8));
            line.setLineReference(Long.valueOf(listUploadedData.get(9)));
            line.setSku(listUploadedData.get(10));
            line.setSkuDescription(listUploadedData.get(11));
            line.setSupplierPartNumber(listUploadedData.get(12));
            line.setManufacturerName(listUploadedData.get(13));
            line.setExpectedDate(listUploadedData.get(14));
            line.setExpectedQty(Double.valueOf(listUploadedData.get(15)));
            line.setUom(listUploadedData.get(16));

            if (listUploadedData.get(17).trim().length() > 0) {
                line.setPackQty(Double.valueOf(listUploadedData.get(17)));
            }
            listWHLines.getInterWarehouseTransferInLine().add(line);
        }

        whOrderList.addAll(transferInV2Map.values());
        return whOrderList;
    }


    // InterWarehouseTransferOutV2
    private List<InterWarehouseTransferOutV2> prepIWTransferOutData(List<List<String>> allRowsList) {
        List<InterWarehouseTransferOutV2> orderList = new ArrayList<>();
        Map<String, InterWarehouseTransferOutV2> transferOutV2Map = new HashMap<>();

        for (List<String> listUploadedData : allRowsList) {

            String transNumber = listUploadedData.get(4);
            InterWarehouseTransferOutV2 interWarehouseTransferOutV2 = transferOutV2Map.get(transNumber);

            //InterWarehouseTransferOutHeaderV2
            if (interWarehouseTransferOutV2 == null) {
                interWarehouseTransferOutV2 = new InterWarehouseTransferOutV2();
                InterWarehouseTransferOutHeaderV2 header = new InterWarehouseTransferOutHeaderV2();
                header.setFromCompanyCode(listUploadedData.get(0));
                header.setToCompanyCode(listUploadedData.get(1));
                header.setFromBranchCode(listUploadedData.get(2));
                header.setToBranchCode(listUploadedData.get(3));
                header.setTransferOrderNumber(listUploadedData.get(4));
                header.setRequiredDeliveryDate(listUploadedData.get(5));
                interWarehouseTransferOutV2.setInterWarehouseTransferOutHeader(header);
                interWarehouseTransferOutV2.setInterWarehouseTransferOutLine(new ArrayList<>());
                transferOutV2Map.put(transNumber, interWarehouseTransferOutV2);
            }
            //InterWarehouseTransferOutLineV2
            InterWarehouseTransferOutLineV2 line = new InterWarehouseTransferOutLineV2();
            line.setLineReference(Long.valueOf(listUploadedData.get(6)));
            line.setSku(listUploadedData.get(7));
            line.setSkuDescription(listUploadedData.get(8));
            line.setOrderedQty(Double.valueOf(listUploadedData.get(9)));
            line.setUom(listUploadedData.get(10));
            line.setOrderType(listUploadedData.get(11));
            line.setManufacturerCode(listUploadedData.get(12));
            line.setManufacturerName(listUploadedData.get(13));

            if (listUploadedData.get(14).trim().length() > 0) {
                line.setBrand(listUploadedData.get(14));
            }
            interWarehouseTransferOutV2.getInterWarehouseTransferOutLine().add(line);
        }
        orderList.addAll(transferOutV2Map.values());
        return orderList;
    }

    // ShipmentOrderV2
    private List<ShipmentOrderV2> prepShipmentOrderOutUpload(List<List<String>> allRowsList) {
        List<ShipmentOrderV2> orderList = new ArrayList<>();
        Map<String, ShipmentOrderV2> shipmentOrderV2Map = new HashMap<>();

        for (List<String> listUploadedData : allRowsList) {

            String transNo = listUploadedData.get(2);
            ShipmentOrderV2 soLineV2s = shipmentOrderV2Map.get(transNo);

            //SOHeaderV2
            if (soLineV2s == null) {
                soLineV2s = new ShipmentOrderV2();
                SOHeaderV2 header = new SOHeaderV2();
                header.setCompanyCode(listUploadedData.get(0));
                header.setBranchCode(listUploadedData.get(1));
                header.setTransferOrderNumber(listUploadedData.get(2));
                header.setStoreID(listUploadedData.get(3));
                header.setStoreName(listUploadedData.get(4));
                header.setRequiredDeliveryDate(listUploadedData.get(5));
                soLineV2s.setSoHeader(header);
                soLineV2s.setSoLine(new ArrayList<>());
                shipmentOrderV2Map.put(transNo, soLineV2s);
            }
            //SOLineV2
            SOLineV2 line = new SOLineV2();
            line.setFromCompanyCode(listUploadedData.get(0));
            line.setSourceBranchCode(listUploadedData.get(1));
            line.setTransferOrderNumber(listUploadedData.get(2));
            line.setLineReference(Long.valueOf(listUploadedData.get(6)));
            line.setSku(listUploadedData.get(7));
            line.setSkuDescription(listUploadedData.get(8));
            line.setOrderedQty(Double.valueOf(listUploadedData.get(9)));
            line.setUom(listUploadedData.get(10));
            line.setOrderType(listUploadedData.get(11));
            line.setManufacturerCode(listUploadedData.get(12));
            line.setManufacturerName(listUploadedData.get(13));
            line.setBrand(listUploadedData.get(14));
            line.setOrigin(listUploadedData.get(15));
            line.setSupplierName(listUploadedData.get(16));
            line.setPackQty(Double.valueOf(listUploadedData.get(17)));
            line.setExpectedQty(Double.valueOf(listUploadedData.get(18)));
            line.setStoreID(listUploadedData.get(19));

            if (listUploadedData.get(20).trim().length() > 0) {
                line.setCountryOfOrigin(listUploadedData.get(20));
            }
            soLineV2s.getSoLine().add(line);
        }
        orderList.addAll(shipmentOrderV2Map.values());
        return orderList;
    }


    // ReturnPOV2
    private List<ReturnPOV2> prepReturnPoV2Upload(List<List<String>> allRowsList) {
        List<ReturnPOV2> orderList = new ArrayList<>();
        Map<String, ReturnPOV2> pov2Map = new HashMap<>();
        for (List<String> listUploadedData : allRowsList) {

            String poNumber = listUploadedData.get(0);

            ReturnPOV2 returnPOV2 = pov2Map.get(poNumber);

            //ReturnPOHeaderV2
            if (returnPOV2 == null) {
                returnPOV2 = new ReturnPOV2();
                ReturnPOHeaderV2 header = new ReturnPOHeaderV2();
                header.setPoNumber(listUploadedData.get(0));
                header.setStoreID(listUploadedData.get(1));
                header.setStoreName(listUploadedData.get(2));
                header.setRequiredDeliveryDate(listUploadedData.get(3));
                header.setCompanyCode(listUploadedData.get(4));
                header.setBranchCode(listUploadedData.get(5));
                returnPOV2.setReturnPOHeader(header);
                returnPOV2.setReturnPOLine(new ArrayList<>());
                pov2Map.put(poNumber, returnPOV2);

            }

            //ReturnPOLineV2
            ReturnPOLineV2 line = new ReturnPOLineV2();
            line.setStoreID(listUploadedData.get(1));
            line.setFromCompanyCode(listUploadedData.get(4));
            line.setSourceBranchCode(listUploadedData.get(1));
            line.setLineReference(Long.valueOf(listUploadedData.get(6)));
            line.setSku(listUploadedData.get(7));
            line.setSkuDescription(listUploadedData.get(8));
            line.setReturnQty(Double.valueOf(listUploadedData.get(9)));
            line.setUom(listUploadedData.get(10));
            line.setOrderType(listUploadedData.get(11));
            line.setManufacturerCode(listUploadedData.get(12));
            line.setManufacturerName(listUploadedData.get(13));
            line.setBrand(listUploadedData.get(14));
            line.setOrigin(listUploadedData.get(15));
            line.setSupplierName(listUploadedData.get(16));
            line.setPackQty(Double.valueOf(listUploadedData.get(17)));
            line.setExpectedQty(Double.valueOf(listUploadedData.get(18)));
            line.setCountryOfOrigin(listUploadedData.get(19));
            line.setReturnOrderNo(listUploadedData.get(20));

            if (listUploadedData.get(21).trim().length() > 0) {
                line.setSupplierInvoiceNo(listUploadedData.get(21));
            }
            returnPOV2.getReturnPOLine().add(line);
        }
        orderList.addAll(pov2Map.values());
        return orderList;
    }

}
