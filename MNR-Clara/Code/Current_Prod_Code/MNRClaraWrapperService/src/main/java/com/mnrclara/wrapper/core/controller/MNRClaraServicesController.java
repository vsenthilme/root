package com.mnrclara.wrapper.core.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.UploadErrorException;
import com.mnrclara.wrapper.core.batch.scheduler.BatchJobScheduler;
import com.mnrclara.wrapper.core.exception.BadRequestException;
import com.mnrclara.wrapper.core.model.accounting.AddInvoiceHeader;
import com.mnrclara.wrapper.core.model.accounting.CustomerRet;
import com.mnrclara.wrapper.core.model.accounting.InvoiceCreateRet;
import com.mnrclara.wrapper.core.model.accounting.InvoiceHeader;
import com.mnrclara.wrapper.core.model.accounting.InvoiceRet;
import com.mnrclara.wrapper.core.model.accounting.PaymentUpdate;
import com.mnrclara.wrapper.core.model.accounting.ReceivePaymentResponse;
import com.mnrclara.wrapper.core.model.auth.AuthToken;
import com.mnrclara.wrapper.core.model.auth.AuthTokenRequest;
import com.mnrclara.wrapper.core.model.crm.UploadFileResponse;
import com.mnrclara.wrapper.core.model.management.ClientGeneral;
import com.mnrclara.wrapper.core.model.management.MatterGenAcc;
import com.mnrclara.wrapper.core.model.management.QBSync;
import com.mnrclara.wrapper.core.model.management.SearchQbSync;
import com.mnrclara.wrapper.core.model.setup.GlMappingMaster;
import com.mnrclara.wrapper.core.model.user.NewUser;
import com.mnrclara.wrapper.core.model.user.RegisteredUser;
import com.mnrclara.wrapper.core.service.AccountingService;
import com.mnrclara.wrapper.core.service.AuthTokenService;
import com.mnrclara.wrapper.core.service.CommonService;
import com.mnrclara.wrapper.core.service.DocStorageService;
import com.mnrclara.wrapper.core.service.FileStorageService;
import com.mnrclara.wrapper.core.service.ManagementService;
import com.mnrclara.wrapper.core.service.RegisterService;
import com.mnrclara.wrapper.core.service.SetupService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@Api(tags = {"MNR Clara Services"}, value = "MNR Clara Services") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "MNRClara Services",description = "Operations related to MNR Clara Services")})
public class MNRClaraServicesController { 
	
	@Autowired
	RegisterService registerService;
	
	@Autowired
	DocStorageService docStorageService;
	
	@Autowired
    FileStorageService fileStorageService;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	SetupService setupService;
	
	@Autowired
	ManagementService managementService;
	
	@Autowired
	AccountingService accountingService;
	
	@Autowired
	BatchJobScheduler batchJobScheduler;
	
	@Autowired
	AuthTokenService authTokenService;
	
	/**
	 * This endpoint is for registering thrd party clients to get the Client ID and Secret Key
	 * @param clientName
	 * @return
	 */
    @ApiOperation(response = Optional.class, value = "Register Client") // label for swagger
	@PostMapping("/register-app-client")
	public ResponseEntity<?> registerClient (String clientName) {
		// Generate Unique ID for each client
    	// Store Client Unique ID and send Client Secret ID along with RegistrationID
    	NewUser registeredUser = registerService.registerNewUser(clientName);
    	return new ResponseEntity<>(registeredUser, HttpStatus.OK);
	}
    
    @ApiOperation(response = Optional.class, value = "Get Client Secret Key") // label for swagger
    @RequestMapping(value = "/client-secret-key", method = RequestMethod.POST, produces = "application/json")
 	public ResponseEntity<?> getClientSecretKey (@Valid @RequestBody RegisteredUser registeredUser) {
    	try {
			String secretKey = registerService.validateRegisteredUser(registeredUser);
			Map<String, String> responseMap = Collections.singletonMap("client-secret-key", secretKey);
			return new ResponseEntity<>(responseMap, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error on getting Client Secret key: " + e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		}
 	}

    @ApiOperation(response = Optional.class, value = "OAuth Token") // label for swagger
	@PostMapping("/auth-token")
	public ResponseEntity<?> authToken (@Valid @RequestBody AuthTokenRequest authTokenRequest) {
		AuthToken authToken = registerService.getAuthToken(authTokenRequest);
    	return new ResponseEntity<>(authToken, HttpStatus.OK);
	}
    
    @ApiOperation(response = UploadFileResponse.class, value = "Document Storage Upload") // label for swagger
    @PostMapping("/doc-storage/upload")
    public ResponseEntity<?> docStorageUpload(@RequestParam String location,
											  @RequestParam(required = false) String emailId,
											  @RequestParam(required = false) String userName,
											  @RequestParam("file") MultipartFile file)
    		throws UploadErrorException, DbxException {
    	if (location == null) {
    		throw new BadRequestException("Location can't be blank. Please provide either Agreement or Document as Location");
    	}
        Map<String, String> response = fileStorageService.storeFile(location, file, emailId, userName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

	@ApiOperation(response = UploadFileResponse.class, value = "Send Mail Check_Request") // label for swagger
	@PostMapping("/check-request/sendmail")
	public ResponseEntity<?> checkRequestSendMail(@RequestParam String location,
											  @RequestParam String emailId,
											  @RequestParam(required = false) String userName)
			throws UploadErrorException, DbxException, IOException {
		if (location == null) {
			throw new BadRequestException("Location can't be blank. Please provide either Agreement or Document as Location");
		}
		Map<String, String> response = fileStorageService.checkRequestMail(location, emailId, userName);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @ApiOperation(response = UploadFileResponse.class, value = "Document Storage Upload") // label for swagger
    @PostMapping("/doc-storage/nonMailMergeUpload")
    public ResponseEntity<?> docStorageNonEemailMergeUpload(@RequestParam String location, 
    		@RequestParam("file") MultipartFile file, @RequestParam String loginUserID,
    		@RequestParam(required = false) Long classId) 
    		throws UploadErrorException, DbxException {
    	if (location == null) {
    		throw new BadRequestException("Location can't be blank. Please provide either Agreement or Document as Location");
    	}
        Map<String, String> response = fileStorageService.storeFileForNonMailMerge(location, file, loginUserID, classId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @ApiOperation(response = Optional.class, value = "Document Storage Download") // label for swagger
   	@GetMapping("/doc-storage/download")
   	public ResponseEntity<?> docStorageDownload(@RequestParam String location, @RequestParam String fileName,
   			@RequestParam(required = false) Long classId) 
   			throws Exception {
    	String filePath = docStorageService.getQualifiedFilePath (location, fileName, classId);
    	File file = new File (filePath);
    	Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
   	}
    
	@ApiOperation(response = Optional.class, value = "Outlook Email") // label for swagger
	@GetMapping("/readMail")
	public ResponseEntity<?> readEmails() throws Exception {
		commonService.readEmails();
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
	
	@ApiOperation(response = Optional.class, value = "Run Job") // label for swagger
	@GetMapping("/run")
	public ResponseEntity<?> runJob() {
		String response = "hello";
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	//---------------------------BATCH-UPLOAD---------------------------------------------------------
	
	@ApiOperation(response = UploadFileResponse.class, value = "Upload Files") // label for swagger
    @PostMapping("/batchupload/jobMatterQuery")
    public ResponseEntity<?> batchUploadjobMatterQuery() throws Exception {
        batchJobScheduler.runJobdbToCsvJob();
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@ApiOperation(response = UploadFileResponse.class, value = "Upload Files") // label for swagger
    @PostMapping("/batchupload/clientgeneral")
    public ResponseEntity<?> batchUploadClientGeneral(@RequestParam("file") MultipartFile file) 
    		throws Exception {
		Map<String, String> response = fileStorageService.storeFile(file);
        batchJobScheduler.runJobClientGeneral();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	@ApiOperation(response = UploadFileResponse.class, value = "Upload Files") // label for swagger
    @PostMapping("/batchupload/clientNote")
    public ResponseEntity<?> batchUploadClientNote(@RequestParam("file") MultipartFile file) 
    		throws Exception {
		Map<String, String> response = fileStorageService.storeFile(file);
        batchJobScheduler.runJobClientNote();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	@ApiOperation(response = UploadFileResponse.class, value = "Upload Files") // label for swagger
    @PostMapping("/batchupload/matterAssignment")
    public ResponseEntity<?> batchUploadMatterAssignment(@RequestParam("file") MultipartFile file) 
    		throws Exception {
		Map<String, String> response = fileStorageService.storeFile(file);
        batchJobScheduler.runJobMatterAssignment();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	@ApiOperation(response = UploadFileResponse.class, value = "Upload Files") // label for swagger
    @PostMapping("/batchupload/matterExpense")
    public ResponseEntity<?> batchUploadMatterExpense(@RequestParam("file") MultipartFile file) 
    		throws Exception {
		Map<String, String> response = fileStorageService.storeFile(file);
        batchJobScheduler.runJobMatterExpense();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	@ApiOperation(response = UploadFileResponse.class, value = "Upload Files") // label for swagger
    @PostMapping("/batchupload/matterGeneral")
    public ResponseEntity<?> batchUploadMatterGeneral(@RequestParam("file") MultipartFile file) 
    		throws Exception {
		Map<String, String> response = fileStorageService.storeFile(file);
        batchJobScheduler.runJobMatterGenAcc();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	@ApiOperation(response = UploadFileResponse.class, value = "Upload Files") // label for swagger
    @PostMapping("/batchupload/matterNotes")
    public ResponseEntity<?> batchUploadMatterNotes(@RequestParam("file") MultipartFile file) 
    		throws Exception {
		Map<String, String> response = fileStorageService.storeFile(file);
        batchJobScheduler.runJobMatterNote();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	@ApiOperation(response = UploadFileResponse.class, value = "Upload Files") // label for swagger
    @PostMapping("/batchupload/matterRate")
    public ResponseEntity<?> batchUploadMatterRate(@RequestParam("file") MultipartFile file) 
    		throws Exception {
		Map<String, String> response = fileStorageService.storeFile(file);
        batchJobScheduler.runJobMatterRate();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	@ApiOperation(response = UploadFileResponse.class, value = "Upload Files") // label for swagger
    @PostMapping("/batchupload/matterTimeTickets")
    public ResponseEntity<?> batchUploadMatterTimeTickets(@RequestParam("file") MultipartFile file) 
    		throws Exception {
		Map<String, String> response = fileStorageService.storeFile(file);
        batchJobScheduler.runJobMatterTimeTicket();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	@ApiOperation(response = UploadFileResponse.class, value = "Upload Files") // label for swagger
    @PostMapping("/batchupload/invoiceHeader")
    public ResponseEntity<?> batchUploadInvoiceHeader(@RequestParam("file") MultipartFile file) 
    		throws Exception {
		Map<String, String> response = fileStorageService.storeFile(file);
        batchJobScheduler.runJobInvoiceHeader();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	@ApiOperation(response = UploadFileResponse.class, value = "Upload Files") // label for swagger
    @PostMapping("/batchupload/invoiceLine")
    public ResponseEntity<?> batchUploadInvoiceLine(@RequestParam("file") MultipartFile file) 
    		throws Exception {
		Map<String, String> response = fileStorageService.storeFile(file);
        batchJobScheduler.runJobInvoiceLine();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	@ApiOperation(response = UploadFileResponse.class, value = "Upload Files") // label for swagger
    @PostMapping("/batchupload/paymentPlanHeader")
    public ResponseEntity<?> batchUploadPaymentPlanHeader(@RequestParam("file") MultipartFile file) 
    		throws Exception {
		Map<String, String> response = fileStorageService.storeFile(file);
        batchJobScheduler.runJobPaymentPlanHeader();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	@ApiOperation(response = UploadFileResponse.class, value = "Upload Files") // label for swagger
    @PostMapping("/batchupload/paymentPlanLine")
    public ResponseEntity<?> batchUploadPaymentPlanLine(@RequestParam("file") MultipartFile file) 
    		throws Exception {
		Map<String, String> response = fileStorageService.storeFile(file);
        batchJobScheduler.runJobPaymentPlanLine();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	@ApiOperation(response = UploadFileResponse.class, value = "Upload Files") // label for swagger
    @PostMapping("/batchupload/paymentUpdate")
    public ResponseEntity<?> batchUploadPaymentUpdate(@RequestParam("file") MultipartFile file) 
    		throws Exception {
		Map<String, String> response = fileStorageService.storeFile(file);
        batchJobScheduler.runJobPaymentUpdate();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	//------------------------------------------------------------------------------------------------
	
	/*-------------------GET CLIENT DATA------------------------------------------------------------*/
	@ApiOperation(response = Optional.class, value = "Get ClientGeneral") // label for swagger
	@GetMapping("/soapservice/clientgeneral")
	public ResponseEntity<?> getClientGeneral () {
		AuthToken authTokenForMgmtService = authTokenService.getManagementServiceAuthToken();
		ClientGeneral clientGeneral = managementService.getClientGeneralByLimit(authTokenForMgmtService.getAccess_token());
		log.info("clientGeneral : " + clientGeneral);
		return new ResponseEntity<>(clientGeneral, HttpStatus.OK);
	}
	
	@ApiOperation(response = Optional.class, value = "Get ClientGeneral") // label for swagger
	@GetMapping("/soapservice/clientgeneral/{clientgeneralId}")
	public ResponseEntity<?> getClientGeneral (@PathVariable String clientgeneralId) {
		AuthToken authTokenForMgmtService = authTokenService.getManagementServiceAuthToken();
		ClientGeneral clientgeneral = managementService.getClientGeneral(clientgeneralId, authTokenForMgmtService.getAccess_token());
		log.info("ClientGeneral : " + clientgeneral);
		return new ResponseEntity<>(clientgeneral, HttpStatus.OK);
	}
	
	@ApiOperation(response = Optional.class, value = "Get ClientGeneral Response") // label for swagger
	@PostMapping("/soapservice/clientgeneral/response")
	public ResponseEntity<?> getClientGeneralResponse (@RequestBody CustomerRet customerRet) {
		log.info("Clara:: ClientGeneral- Response received: " + customerRet);
		AuthToken authTokenForMgmtService = authTokenService.getManagementServiceAuthToken();
		ClientGeneral clientGeneral = managementService.getClientGeneral(customerRet.getName(), authTokenForMgmtService.getAccess_token());
		if (clientGeneral != null && customerRet != null && customerRet.getStatusCode().equalsIgnoreCase("0")) {
			clientGeneral.setSentToQB(1L);
			clientGeneral.setReferenceField12("Sent to QB");
			ClientGeneral updatedClientgeneral = 
					managementService.updateClientGeneral(clientGeneral.getClientId(), clientGeneral,
							"QBUpdate", authTokenForMgmtService.getAccess_token());
			return new ResponseEntity<>(updatedClientgeneral, HttpStatus.OK);
		} else {
			clientGeneral.setSentToQB(2L);
			clientGeneral.setReferenceField12("Sent to QB but with Error");
			ClientGeneral updatedClientgeneral = 
					managementService.updateClientGeneral(clientGeneral.getClientId(), clientGeneral,
							"QBUpdate", authTokenForMgmtService.getAccess_token());
			return new ResponseEntity<>(updatedClientgeneral, HttpStatus.OK);
		}
	}
	
	@ApiOperation(response = Optional.class, value = "ClientGeneral Rerun") // label for swagger
	@PostMapping("/soapservice/clientgeneral/rerun")
	public ResponseEntity<?> getClientGeneralRerun (@RequestParam String clientId) {
		AuthToken authTokenForMgmtService = authTokenService.getManagementServiceAuthToken();
		ClientGeneral clientGeneral = managementService.getClientGeneral(clientId, authTokenForMgmtService.getAccess_token());
		if (clientGeneral != null) {
			clientGeneral.setSentToQB(0L);
			clientGeneral.setReferenceField12("Rerun");
			ClientGeneral updatedClientgeneral = 
					managementService.updateClientGeneral(clientGeneral.getClientId(), clientGeneral,
							"QBUpdate", authTokenForMgmtService.getAccess_token());
			/*
			 * QBSync Update
			 */
			QBSync qbSync = new QBSync();
			qbSync.setStatusId(1L);
			managementService.updateQbSync(clientGeneral.getClientId(), qbSync, authTokenForMgmtService.getAccess_token());
			return new ResponseEntity<>(updatedClientgeneral, HttpStatus.OK);
		}
		return null;
	}
	
	/*-------------------GET MATTER DATA------------------------------------------------------------*/
	@ApiOperation(response = Optional.class, value = "Get latest MatterGeneral") // label for swagger
	@GetMapping("/soapservice/mattergeneral")
	public ResponseEntity<?> getMatterGeneral () {
		AuthToken authTokenForMgmtService = authTokenService.getManagementServiceAuthToken();
		MatterGenAcc matterGeneral = managementService.getMatterGeneralByLimit(authTokenForMgmtService.getAccess_token());
		log.info("MatterGeneral : " + matterGeneral);
		return new ResponseEntity<>(matterGeneral, HttpStatus.OK);
	}
	
	@ApiOperation(response = Optional.class, value = "Get MatterGeneral Response") // label for swagger
	@PostMapping("/soapservice/mattergeneral/response")
	public ResponseEntity<?> getMatterGeneralResponse (@RequestBody CustomerRet customerRet) throws Exception {
		log.info("customerRet : " + customerRet);
		AuthToken authTokenForMgmtService = authTokenService.getManagementServiceAuthToken();
		if (customerRet.getName() != null) {
			MatterGenAcc matterGeneral = managementService.getMatterGenAcc(customerRet.getName(), authTokenForMgmtService.getAccess_token());
			log.info("modifiedMatterGenAcc : " + matterGeneral);
			if (matterGeneral != null && customerRet != null && 
					(customerRet.getStatusCode().equalsIgnoreCase("0") || customerRet.getStatusCode().equalsIgnoreCase("3100"))) {
				matterGeneral.setSentToQB(1L);
//				MatterGenAcc matterGenAcc = managementService.updateMatterGenAcc(matterGeneral.getMatterNumber(), 
//						matterGeneral, "QBUpdate", authTokenForMgmtService.getAccess_token());
				MatterGenAcc matterGenAcc = 
						managementService.getQbSyncUpdated(matterGeneral.getMatterNumber(), "QBUpdate", authTokenForMgmtService.getAccess_token());
				log.info("matterGenAcc QBSync--------> : " + matterGenAcc);
				
				/*
				 * QBSync Update
				 */
				try {
					QBSync qbSync = new QBSync();
					qbSync.setStatusId(1L);
					managementService.updateQbSync(customerRet.getName(), qbSync, authTokenForMgmtService.getAccess_token());
				} catch (Exception e) {
				}
				return new ResponseEntity<>(matterGenAcc, HttpStatus.OK);
			} else {
				matterGeneral.setSentToQB(2L);
				MatterGenAcc matterGenAcc = managementService.updateMatterGenAcc(matterGeneral.getMatterNumber(), 
						matterGeneral, "QBUpdate", authTokenForMgmtService.getAccess_token());
				return new ResponseEntity<>(matterGenAcc, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@ApiOperation(response = Optional.class, value = "MatterGeneral Rerun") // label for swagger
	@PostMapping("/soapservice/mattergeneral/rerun")
	public ResponseEntity<?> getMatterGeneralRerun(@RequestParam String matterNumber) throws Exception {
		AuthToken authTokenForMgmtService = authTokenService.getManagementServiceAuthToken();
		MatterGenAcc matterGeneral = managementService.getMatterGenAcc(matterNumber, authTokenForMgmtService.getAccess_token());
		log.info("modifiedMatterGenAcc : " + matterGeneral);
		if (matterGeneral != null) {
			matterGeneral.setSentToQB(0L);
			MatterGenAcc matterGenAcc = managementService.updateMatterGenAcc(matterGeneral.getMatterNumber(), 
					matterGeneral, "QBUpdate", authTokenForMgmtService.getAccess_token());
			
			/*
			 * QBSync Update
			 */
			QBSync qbSync = new QBSync();
			qbSync.setStatusId(1L);
			managementService.updateQbSync(matterNumber, qbSync, authTokenForMgmtService.getAccess_token());
			return new ResponseEntity<>(matterGenAcc, HttpStatus.OK);
		}
		return null;
	}
	
	/*-------------------GET-INVOICE-DATA------------------------------------------------------------*/
	@ApiOperation(response = Optional.class, value = "Get latest Invoice") // label for swagger
	@GetMapping("/soapservice/invoice")
	public ResponseEntity<?> getInvoice() throws Exception {
		AuthToken authTokenForAccService = authTokenService.getAccountingServiceAuthToken();
		InvoiceHeader invoiceHeader = accountingService.getLatestInvoice(authTokenForAccService.getAccess_token());
		log.info("Invoice : " + invoiceHeader);
		
		return new ResponseEntity<>(invoiceHeader, HttpStatus.OK);
	}
	
	@ApiOperation(response = Optional.class, value = "Invoice Response") // label for swagger
	@PostMapping("/soapservice/invoice/response")
	public ResponseEntity<?> getInvoiceResponse (@RequestBody InvoiceCreateRet invoiceCreateRet) {
		log.info("Invoice- Response : " + invoiceCreateRet);
		AuthToken authTokenForAcctService = authTokenService.getAccountingServiceAuthToken();
		if (invoiceCreateRet != null && invoiceCreateRet.getStatusCode().equalsIgnoreCase("0")) {
			InvoiceHeader invHeader = accountingService.updateInvoiceHeaderQB(invoiceCreateRet.getName(), 1L, 
					authTokenForAcctService.getAccess_token());
			log.info("InvoiceHeader : " + invHeader);
			
			/*
			 * QBSync Update
			 */
			QBSync qbSync = new QBSync();
			qbSync.setStatusId(1L);
			managementService.updateQbSync(invoiceCreateRet.getName(), qbSync, authTokenForAcctService.getAccess_token());
			return new ResponseEntity<>(invHeader, HttpStatus.OK);
		} else {
			InvoiceHeader invHeader = accountingService.updateInvoiceHeaderQB(invoiceCreateRet.getName(), 2L, 
					authTokenForAcctService.getAccess_token());
			log.info("InvoiceHeader : " + invHeader);
			return new ResponseEntity<>(invHeader, HttpStatus.OK);
		}
	}
	
	@ApiOperation(response = Optional.class, value = "Invoice Rerun") // label for swagger
	@PostMapping("/soapservice/invoice/rerun")
	public ResponseEntity<?> getInvoiceRerun(@RequestParam String invoiceNumber) {
		AuthToken authTokenForAcctService = authTokenService.getAccountingServiceAuthToken();
		InvoiceHeader invHeader = accountingService.updateInvoiceHeaderQB(invoiceNumber, 0L, 
				authTokenForAcctService.getAccess_token());
		log.info("InvoiceHeader : " + invHeader);
		
		/*
		 * QBSync Update
		 */
		QBSync qbSync = new QBSync();
		qbSync.setStatusId(1L);
		managementService.updateQbSync(invoiceNumber, qbSync, authTokenForAcctService.getAccess_token());
		return new ResponseEntity<>(invHeader, HttpStatus.OK);
	}
	
	/*-------------------INVOICE-QUERY------------------------------------------------------------*/
	// Invoice Status
	@ApiOperation(response = Optional.class, value = "Get latest Invoice") // label for swagger
	@GetMapping("/soapservice/invoiceQuery")
	public ResponseEntity<?> getInvoiceQuery() {
		AuthToken authTokenForAccService = authTokenService.getAccountingServiceAuthToken();
		String invoiceNumber = accountingService.getTopByQbQuery(authTokenForAccService.getAccess_token());
		log.info("Invoice : " + invoiceNumber);
		if (invoiceNumber != null) {
			return new ResponseEntity<>(invoiceNumber, HttpStatus.OK);
		}
		return new ResponseEntity<>("", HttpStatus.OK);
	}
	
	@ApiOperation(response = Optional.class, value = "Post Invoice Response") // label for swagger
	@PostMapping("/soapservice/invoiceQuery/response")
	public ResponseEntity<?> saveInvoiceQueryResponse (@RequestBody InvoiceRet invoiceRet) {
		log.info("Invoice - Response : " + invoiceRet);
		
		AuthToken authTokenForAccService = authTokenService.getAccountingServiceAuthToken();
		accountingService.createPaymentUpdate (invoiceRet, authTokenForAccService.getAccess_token());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//---------------------Receive-Payments---------------------------------------------------------*/
	@ApiOperation(response = Optional.class, value = "Get Invoice Response") // label for swagger
	@PostMapping("/soapservice/receivePaymentQuery/response")
	public ResponseEntity<?> saveReceivePaymentResponse (@RequestBody List<ReceivePaymentResponse> receivePaymentResponseList) {
		log.info("receivePaymentRet - Response : " + receivePaymentResponseList);
		AuthToken authTokenForAccService = authTokenService.getAccountingServiceAuthToken();
		PaymentUpdate[] paymentUpdate = 
				accountingService.createPaymentUpdateByReceivePayment(receivePaymentResponseList, authTokenForAccService.getAccess_token());
		log.info("PaymentUpdate created : " + paymentUpdate);
		return new ResponseEntity<>(paymentUpdate, HttpStatus.OK);
	}

	/*---------------------GL-Mapping-Master--------------------------------------------------------*/
	@ApiOperation(response = Optional.class, value = "Get Invoice Response") // label for swagger
	@GetMapping("/soapservice/glmapping/{itemNumber}")
	public ResponseEntity<?> getInvoiceResponse (@PathVariable Long itemNumber, @RequestParam String languageId) {
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		GlMappingMaster glMappingMaster = 
				setupService.getGlMappingMaster(itemNumber, authTokenForSetupService.getAccess_token());
		return new ResponseEntity<>(glMappingMaster.getItemDescription(), HttpStatus.OK);
	}
	
	/*---------------------QBSync------------------------------------------------------------------*/
	@ApiOperation(response = Optional.class, value = "Create QBSync") // label for swagger
	@PostMapping("/soapservice/qbsync")
	public ResponseEntity<?> postQBSync (@RequestBody QBSync qbSync) {
		AuthToken authTokenForMgmtService = authTokenService.getManagementServiceAuthToken();
		QBSync qbSyncResponse = managementService.createQbSync(qbSync, authTokenForMgmtService.getAccess_token());
		return new ResponseEntity<>(qbSyncResponse, HttpStatus.OK);
	}
	
	@ApiOperation(response = Optional.class, value = "GetAll QBSync") // label for swagger
	@GetMapping("/soapservice/qbsync")
	public ResponseEntity<?> QBSync () {
		AuthToken authTokenForMgmtService = authTokenService.getManagementServiceAuthToken();
		com.mnrclara.wrapper.core.model.management.QBSync[] qbSyncList = 
				managementService.getQBSync(authTokenForMgmtService.getAccess_token());
		return new ResponseEntity<>(qbSyncList, HttpStatus.OK);
	}
	
	@ApiOperation(response = QBSync.class, value = "Search QBSync") // label for swagger
	@PostMapping("/soapservice/qbsync/findQbSync")
	public ResponseEntity<?> findQbSync(@RequestBody SearchQbSync searchQbSync) throws ParseException {
		AuthToken authTokenForMgmtService = authTokenService.getManagementServiceAuthToken();
		QBSync[] qbSyncList = managementService.findQbSync(searchQbSync, authTokenForMgmtService.getAccess_token());
		return new ResponseEntity<> (qbSyncList, HttpStatus.OK);
	}
}