package com.iweb2b.core.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import com.iweb2b.core.config.PropertiesConfig;
import com.iweb2b.core.exception.BadRequestException;
import com.iweb2b.core.exception.CustomErrorResponse;
import com.iweb2b.core.model.auth.AuthToken;
import com.iweb2b.core.model.auth.AuthTokenRequest;
import com.iweb2b.core.model.auth.CustomerAccessToken;
import com.iweb2b.core.model.integration.AddUserAccess;
import com.iweb2b.core.model.integration.CancelShipmentRequest;
import com.iweb2b.core.model.integration.ConsignmentTracking;
import com.iweb2b.core.model.integration.CountInput;
import com.iweb2b.core.model.integration.DashboardCountOutput;
import com.iweb2b.core.model.integration.FindConsignment;
import com.iweb2b.core.model.integration.FindUserAccess;
import com.iweb2b.core.model.integration.InventoryScanRequest;
import com.iweb2b.core.model.integration.UpdateUserAccess;
import com.iweb2b.core.model.integration.UserAccess;
import com.iweb2b.core.model.integration.asyad.Consignment;
import com.iweb2b.core.model.integration.asyad.ConsignmentResponse;
import com.iweb2b.core.model.integration.asyad.ConsignmentWebhook;
import com.iweb2b.core.model.integration.asyad.JNTPrintLabelResponse;
import com.iweb2b.core.model.integration.asyad.JNTResponse;
import com.iweb2b.core.model.integration.asyad.JNTWebhookEntity;
import com.iweb2b.core.model.integration.asyad.JNTWebhookRequest;
import com.iweb2b.core.model.integration.asyad.OrderStatusUpdate;
import com.iweb2b.core.model.integration.asyad.OrderStatusUpdateResponse;
import com.iweb2b.core.model.integration.asyad.PdfLabelInput;
import com.iweb2b.core.model.integration.asyad.QPOrderCreateResponse;
import com.iweb2b.core.model.integration.asyad.QPTrackingRequest;
import com.iweb2b.core.model.integration.asyad.QPTrackingResponse;
import com.iweb2b.core.service.AuthTokenService;
import com.iweb2b.core.service.IntegrationService;
import com.iweb2b.core.util.PDFMergeExample;
import com.iweb2b.core.util.PasswordEncoder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@Api(tags = {"Integration Service"}, value = "Integration Service Operations") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "Integration",description = "Operations related to User")})
@RequestMapping("/iwe-integration-service")
public class IntegrationController {

	@Autowired
	IntegrationService integrationService;

	@Autowired
	AuthTokenService authTokenService;

	@Autowired
	PropertiesConfig propertiesConfig;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	private static final String BASIC = "Basic ";	
	private String JNT_HUBCODE = "JT";
    private String QAP_HUBCODE = "QATARPOST";

	//-----------------------------------CONSIGNMENT-CREATION-----------------------------------------------------
	@ApiOperation(response = Optional.class, value = "OAuth Token") // label for swagger
	@PostMapping("/auth-token")
	public ResponseEntity<?> authToken(@Valid @RequestBody AuthTokenRequest authTokenRequest) {
		AuthToken authToken = authTokenService.getAuthToken(authTokenRequest);
		return new ResponseEntity<>(authToken, HttpStatus.OK);
	}

	//-----------------------------------CONSIGNMENT-CREATION-----------------------------------------------------
	@ApiOperation(response = ConsignmentResponse.class, value = "SoftData Upload") // label for swagger
	@PostMapping("/softdata/upload")
	public ResponseEntity<?> softDataUpload (@RequestHeader(value="Authorization") String authorization,
											 @Valid @RequestBody Consignment newConsignment) throws Exception {
		String LIFETIME_TOKEN = propertiesConfig.getB2bIntegrationToken();

		if (!authorization.startsWith(BASIC)) {
			throw new BadRequestException("Authorization should be supplied prefixing with Basic<space> " + authorization);
		}
		
//		if (!authorization.equalsIgnoreCase(BASIC + LIFETIME_TOKEN)) {
//			throw new BadRequestException("Authorization not valid. Please provide valid Token");
//		}
		
		String passedToken = authorization.substring(6);
		log.info("extracted : " + passedToken);
		AuthToken integAuthToken = authTokenService.getIntegrationServiceAuthToken();
		CustomerAccessToken customerToken = integrationService.getCustomerToken(passedToken, integAuthToken.getAccess_token());
		boolean matched = false;
		
		if (LIFETIME_TOKEN.equalsIgnoreCase(passedToken)) {
			matched = true;
		} else if (customerToken.getToken().equalsIgnoreCase(passedToken)) {
			matched = true;		
		} else {
			matched = false;
		}
		
		if (matched) {
			ConsignmentResponse consignmentResponse = integrationService.createConsignment(newConsignment, "NEW_ORDER");
			log.info("Consignment Created: " + consignmentResponse);

			String refNumber = consignmentResponse.getReference_number();
			log.info("Consignment refNumber: " + refNumber);
			return new ResponseEntity<>(refNumber, HttpStatus.OK);
		} else {
			throw new BadRequestException("Password wrong. Please enter correct password.");
		}
	}
	
	//-----------------------------------CONSIGNMENT-CREATION-PDF Response-----------------------------------------------------
	@ApiOperation(response = ConsignmentResponse.class, value = "SoftData Upload with PDF Response") // label for swagger
	@PostMapping("/softdata/upload/v2")
	public ResponseEntity<?> softDataUploadPDFResponse (@RequestHeader(value="Authorization") String authorization,
											 @Valid @RequestBody Consignment newConsignment) throws Exception {
		log.info("newSoftDataUpload passed: " + newConsignment);
		String LIFETIME_TOKEN = propertiesConfig.getB2bIntegrationToken();
		
		if (!authorization.startsWith(BASIC)) {
			throw new BadRequestException("Authorization should be supplied prefixing with Basic<space> " + authorization);
		}
		
//		if (!authorization.equalsIgnoreCase(BASIC + LIFETIME_TOKEN)) {
//			throw new BadRequestException("Authorization not valid. Please provide valid Token");
//		}
		
		String passedToken = authorization.substring(6);
		log.info("extracted : " + passedToken);
		AuthToken integAuthToken = authTokenService.getIntegrationServiceAuthToken();
		CustomerAccessToken customerToken = integrationService.getCustomerToken(passedToken, integAuthToken.getAccess_token());
		boolean matched = false;
		
		if (LIFETIME_TOKEN.equalsIgnoreCase(passedToken)) {
			matched = true;
		} else if (customerToken.getToken().equalsIgnoreCase(passedToken)) {
			matched = true;		
		} else {
			matched = false;
		}
		
		log.info("----matched-------> " + matched);
		
		if (matched) {
			ConsignmentResponse consignmentResponse = integrationService.createConsignment(newConsignment, "NEW_ORDER");
			log.info("Consignment Created: " + consignmentResponse);

			byte[] dbSoftDataUpload = integrationService.getShippingLabel(consignmentResponse.getReference_number());
			log.info("ShippingLabel : " + dbSoftDataUpload);

			HttpHeaders header = new HttpHeaders();
			header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + consignmentResponse.getReference_number() + ".pdf");
			header.add("Cache-Control", "no-cache, no-store, must-revalidate");
			header.add("Pragma", "no-cache");
			header.add("Expires", "0");
			return ResponseEntity.ok()
					.headers(header)
					.contentType(MediaType.APPLICATION_PDF)
					.body(dbSoftDataUpload);
		} else {
			throw new BadRequestException("Password wrong. Please enter correct password.");
		}
	}

	@ApiOperation(response = Consignment.class, value = "Get Consignments") // label for swagger
	@GetMapping("/softdata")
	public ResponseEntity<?> getConsignments(@RequestHeader String authToken) throws Exception {
		Consignment[] consignmentList = integrationService.getConsignments(authToken);
		return new ResponseEntity<>(consignmentList, HttpStatus.OK);
	}
	
	@ApiOperation(response = ConsignmentWebhook.class, value = "Get Consignment Webhook by Type") // label for swagger
	@GetMapping("/softdata/type")
	public ResponseEntity<?> getConsignmentsByStatus(@RequestHeader String authToken, @RequestParam String type) throws Exception {
		ConsignmentWebhook[] consignmentWebhookList = integrationService.getConsignmentByType(type, authToken);
		return new ResponseEntity<>(consignmentWebhookList, HttpStatus.OK);
	}
	
	//----------------------------------------------------------------------------------------------------------------
	@ApiOperation(response = Optional.class, value = "Push Order to JNT") // label for swagger
	@GetMapping("/softdata/jnt")
	public ResponseEntity<?> postJNTRequest(@RequestParam String referenceNumber, 
			@RequestHeader(value="Authorization") String authorization) throws Exception {
		if (!authorization.startsWith(BASIC)) {
			throw new BadRequestException("Authorization should be supplied prefixing with Basic<space> " + authorization);
		}
		
		JNTResponse response = integrationService.postJNTRequest(referenceNumber);
//		log.info("JNTRequest : " + response);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Get All using Hub Code") // label for swagger
	@GetMapping("/softdata/{hubCode}/orders")
	public ResponseEntity<?> getHubCodeOrders(@PathVariable String hubCode, @RequestHeader String authToken) throws Exception {
		Consignment[] response = integrationService.getHubCodeOrders(hubCode, authToken);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@ApiOperation(response = JNTWebhookRequest.class, value = "Get PrintLabel") // label for swagger
	@GetMapping("/softdata/jnt/{billCode}/printLabel")
	public ResponseEntity<?> printLabel(@PathVariable String billCode, @RequestHeader String authToken) throws Exception {
		JNTPrintLabelResponse printLabelResponse = integrationService.printLabel(billCode, authToken);
		return new ResponseEntity<>(printLabelResponse, HttpStatus.OK);
	}
	
	@ApiOperation(response = JNTWebhookRequest.class, value = "Get PrintLabel") // label for swagger
	@GetMapping("/softdata/jnt/{billCode}/pdf/printLabel")
	public ResponseEntity<?> pdfPrintLabel(@PathVariable String billCode, @RequestHeader String authToken) throws Exception {
		byte[] printLabelResponse = integrationService.pdfPrintLabel(billCode, authToken);
		HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + billCode + ".pdf");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        return ResponseEntity.ok()
                .headers(header)
                .contentType(MediaType.APPLICATION_PDF)
                .body(printLabelResponse);
	}
	
	@ApiOperation(response = JNTWebhookRequest.class, value = "Get PrintLabel") // label for swagger
	@PostMapping("/softdata/jnt/bulk/printLabel")
	public ResponseEntity<?> pdfPrintLabel(@RequestBody PdfLabelInput pdfLabelInput, @RequestHeader String authToken) throws Exception {
		for (String code : pdfLabelInput.getBillCodes()) {
			OutputStream os = new FileOutputStream( code + ".pdf");
			byte[] printLabelResponse = integrationService.pdfPrintLabel(code, authToken);
			os.write(printLabelResponse);
			os.close();
		}
		
		String filename = merge(pdfLabelInput.getBillCodes());
		File file = new File (filename);
		FileInputStream f1 = new FileInputStream(filename);
        byte[] result = new byte[(int)file.length()];
        f1.read(result);
        f1.close();
        
        pdfLabelInput.getBillCodes().stream().forEach(code -> {
			File f = new File (code + ".pdf");
			f.delete();
		});
  
		HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename + ".pdf");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        return ResponseEntity.ok()
                .headers(header)
                .contentType(MediaType.APPLICATION_PDF)
                .body(result);
	}
	
	/**
	 * 
	 * @param billCode
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	private String merge(List<String> billCode) throws IOException, DocumentException {
		List<String> inputPdfList = new ArrayList<>();
		for (String code : billCode) {
			inputPdfList.add(code + ".pdf");
		}
		return PDFMergeExample.mergeFiles(inputPdfList);
	}
	
	@ApiOperation(response = OrderStatusUpdateResponse.class, value = "Post JNTWebhook") // label for swagger
	@PostMapping("/softdata/jnt/{referenceNumber}/eventUpdate")
	public ResponseEntity<?> listenJNTWebhook(@RequestBody OrderStatusUpdate orderStatusUpdate, @RequestParam String event,
			@RequestHeader String authToken) throws Exception {
		OrderStatusUpdateResponse response = integrationService.updateOrderInShipsy(orderStatusUpdate, event, authToken);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	//--------------------------------QP-POST-ORDER---------------------------------------------------------------
	@ApiOperation(response = Optional.class, value = "Push Order to QP") // label for swagger
	@GetMapping("/softdata/qp")
	public ResponseEntity<?> postQPRequest(@RequestParam String referenceNumber) throws Exception {
		QPOrderCreateResponse[] response = integrationService.postQPRequest(referenceNumber);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	//-----------------------------------SHIPPING LABEL------------------------------------------------------------
	@ApiOperation(response = Optional.class, value = "Get a Shipping Label") // label for swagger
	@GetMapping("/{referenceNumber}/shippingLabel")
	public ResponseEntity<?> getShippingLabel(@RequestHeader(value="Authorization") String authorization,
			@PathVariable String referenceNumber) {
//		log.info("authorization passed: " + authorization);
//		log.info("referenceNumber passed: " + referenceNumber);
		String LIFETIME_TOKEN = propertiesConfig.getB2bIntegrationToken();
		if (!authorization.startsWith(BASIC)) {
			throw new BadRequestException("Authorization should be supplied prefixing with Basic<space> " + authorization);
		}
		
		// Lifetoken : Basic $2a$10$qWHNeBhu4FCGoJfuv2XVbO9Yq4QBUwGSvNM0bGpYUVc3iY8jXsJwO
//		if (!authorization.equalsIgnoreCase(BASIC + LIFETIME_TOKEN)) {
//			throw new BadRequestException("Authorization not valid. Please provide valid Token");
//		}
		
		String passedToken = authorization.substring(6);
//		log.info("extracted : " + passedToken);
		AuthToken integAuthToken = authTokenService.getIntegrationServiceAuthToken();
		CustomerAccessToken customerToken = integrationService.getCustomerToken(passedToken, integAuthToken.getAccess_token());
		boolean matched = false;
		
		if (LIFETIME_TOKEN.equalsIgnoreCase(passedToken)) {
			matched = true;
		} else if (customerToken.getToken().equalsIgnoreCase(passedToken)) {
			matched = true;		
		} else {
			matched = false;
		}
		
		if (matched) {
			byte[] dbSoftDataUpload = integrationService.getShippingLabel(referenceNumber);
//			log.info("ShippingLabel : " + dbSoftDataUpload);
			
			HttpHeaders header = new HttpHeaders();
	        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + referenceNumber + ".pdf");
	        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
	        header.add("Pragma", "no-cache");
	        header.add("Expires", "0");
	        return ResponseEntity.ok()
	                .headers(header)
	                .contentType(MediaType.APPLICATION_PDF)
	                .body(dbSoftDataUpload);
		} else {
			throw new BadRequestException("Password wrong. Please enter correct password.");
		}
	}
	
	//--------------------------Shipping (AWB) label-----------------------------------------------------------------------
    @ApiOperation(response = Optional.class, value = "Get a ClientLevel") // label for swagger
    @GetMapping("/{wayBillNumber}/shippingLabel/v2")
    public ResponseEntity<?> getShippingLabelV2(@RequestHeader(value="Authorization") String authorization, 
    		@PathVariable String wayBillNumber) {
//		log.info("authorization passed: " + authorization);
//		log.info("referenceNumber passed: " + referenceNumber);
		String LIFETIME_TOKEN = propertiesConfig.getB2bIntegrationToken();
		if (!authorization.startsWith(BASIC)) {
			throw new BadRequestException("Authorization should be supplied prefixing with Basic<space> " + authorization);
		}
		
		String passedToken = authorization.substring(6);
		AuthToken integAuthToken = authTokenService.getIntegrationServiceAuthToken();
		CustomerAccessToken customerToken = integrationService.getCustomerToken(passedToken, integAuthToken.getAccess_token());
		boolean matched = false;
		
		if (LIFETIME_TOKEN.equalsIgnoreCase(passedToken)) {
			matched = true;
		} else if (customerToken.getToken().equalsIgnoreCase(passedToken)) {
			matched = true;		
		} else {
			matched = false;
		}
		
		if (matched) {
			byte[] dbSoftDataUpload = integrationService.getShippingLabel(wayBillNumber);
			HttpHeaders header = new HttpHeaders();
	        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + wayBillNumber + ".pdf");
	        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
	        header.add("Pragma", "no-cache");
	        header.add("Expires", "0");
	        return ResponseEntity.ok()
	                .headers(header)
	                .contentType(MediaType.APPLICATION_PDF)
	                .body(dbSoftDataUpload);
		} else {
			throw new BadRequestException("Password wrong. Please enter correct password.");
		}
    }
    
    //--------------------------------------------------------------------------------------------------------------------------
    //-------------------------------AJEX---------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------------------------
    //-------------------------------Shipping (AWB) label-----------------------------------------------------------------------
    @ApiOperation(response = Optional.class, value = "Get a ClientLevel") // Specific to AJEX
    @GetMapping("/{customerReferenceNo}/shippingLabel/v3")
    public ResponseEntity<?> getShippingLabelV3(@RequestHeader(value="Authorization") String authorization, 
    		@PathVariable String customerReferenceNo) {
		String LIFETIME_TOKEN = propertiesConfig.getB2bIntegrationToken();
		if (!authorization.startsWith(BASIC)) {
			throw new BadRequestException("Authorization should be supplied prefixing with Basic<space> " + authorization);
		}
		
		String passedToken = authorization.substring(6);
		AuthToken integAuthToken = authTokenService.getIntegrationServiceAuthToken();
		CustomerAccessToken customerToken = integrationService.getCustomerToken(passedToken, integAuthToken.getAccess_token());
		boolean matched = false;
		
		if (LIFETIME_TOKEN.equalsIgnoreCase(passedToken)) {
			matched = true;
		} else if (customerToken.getToken().equalsIgnoreCase(passedToken)) {
			matched = true;		
		} else {
			matched = false;
		}
		
		if (matched) {
			byte[] dbSoftDataUpload = integrationService.getShippingLabelV3(customerReferenceNo);
			HttpHeaders header = new HttpHeaders();
	        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + customerReferenceNo + ".pdf");
	        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
	        header.add("Pragma", "no-cache");
	        header.add("Expires", "0");
	        return ResponseEntity.ok()
	                .headers(header)
	                .contentType(MediaType.APPLICATION_PDF)
	                .body(dbSoftDataUpload);
		} else {
			throw new BadRequestException("Password wrong. Please enter correct password.");
		}
    }
    
    //-------------------------------------Cancel-API------------------------------------------------------------------------------
    // POST /api/client/integration/consignment/cancellation
    @ApiOperation(response = ConsignmentTracking.class, value = "Cancel Shipment") // label for swagger
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelShipment(@RequestHeader(value="Authorization") String authorization,
    		@RequestBody CancelShipmentRequest cancelShipmentRequest) {
		String LIFETIME_TOKEN = propertiesConfig.getB2bIntegrationToken();
		if (!authorization.startsWith(BASIC)) {
			throw new BadRequestException("Authorization should be supplied prefixing with Basic<space> " + authorization);
		}
		
		String passedToken = authorization.substring(6);
		AuthToken integAuthToken = authTokenService.getIntegrationServiceAuthToken();
		CustomerAccessToken customerToken = integrationService.getCustomerToken(passedToken, integAuthToken.getAccess_token());
		boolean matched = false;
		
		if (LIFETIME_TOKEN.equalsIgnoreCase(passedToken)) {
			matched = true;
		} else if (customerToken.getToken().equalsIgnoreCase(passedToken)) {
			matched = true;		
		} else {
			matched = false;
		}
		
		if (matched) {
			com.iweb2b.core.model.integration.CancelShipmentResponse response = 
					integrationService.cancelShipment(cancelShipmentRequest);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			throw new BadRequestException("Password wrong. Please enter correct password.");
		}
    }
    
    //-------------------------------------InventoryScan-API-----------------------------------------------------------------------
    @ApiOperation(response = ConsignmentTracking.class, value = "Get a InventoryScan API") // label for swagger
    @PostMapping("/inventoryScan")
    public ResponseEntity<?> inventoryScan (@RequestHeader(value="Authorization") String authorization,
    		@RequestBody InventoryScanRequest inventoryScanRequest) {
    	String LIFETIME_TOKEN = propertiesConfig.getB2bIntegrationToken();
		if (!authorization.startsWith(BASIC)) {
			throw new BadRequestException("Authorization should be supplied prefixing with Basic<space> " + authorization);
		}
		
		String passedToken = authorization.substring(6);
		AuthToken integAuthToken = authTokenService.getIntegrationServiceAuthToken();
		CustomerAccessToken customerToken = integrationService.getCustomerToken(passedToken, integAuthToken.getAccess_token());
		boolean matched = false;
		
		if (LIFETIME_TOKEN.equalsIgnoreCase(passedToken)) {
			matched = true;
		} else if (customerToken.getToken().equalsIgnoreCase(passedToken)) {
			matched = true;		
		} else {
			matched = false;
		}
		
		if (matched) {
			ConsignmentWebhook[] webhooks = integrationService.scanInventory(inventoryScanRequest);
	        return new ResponseEntity<>(webhooks, HttpStatus.OK);
		} else {
			throw new BadRequestException("Password wrong. Please enter correct password.");
		}
    }

	//-----------------------------------ConsignmentTracking------------------------------------------------------------
	@ApiOperation(response = ConsignmentTracking.class, value = "Get a ConsignmentTracking") // label for swagger
	@GetMapping("/tracking/{referenceNumber}/shipment")
	public ResponseEntity<?> getConsignmentTracking(@RequestHeader(value="Authorization") String authorization,
			@PathVariable String referenceNumber) {
		log.info("referenceNumber passed: " + referenceNumber);
		String LIFETIME_TOKEN = propertiesConfig.getB2bIntegrationToken();
		
		if (!authorization.startsWith(BASIC)) {
			throw new BadRequestException("Authorization should be supplied prefixing with Basic<space> " + authorization);
		} 
		
		// Lifetoken : Basic $2a$10$qWHNeBhu4FCGoJfuv2XVbO9Yq4QBUwGSvNM0bGpYUVc3iY8jXsJwO
//		if (!authorization.equalsIgnoreCase(BASIC + LIFETIME_TOKEN)) {
//			throw new BadRequestException("Authorization not valid. Please provide valid Token");
//		}
		
		String passedToken = authorization.substring(6);
//		log.info("extracted : " + passedToken);
		AuthToken integAuthToken = authTokenService.getIntegrationServiceAuthToken();
		CustomerAccessToken customerToken = integrationService.getCustomerToken(passedToken, integAuthToken.getAccess_token());
		boolean matched = false;
		
		if (LIFETIME_TOKEN.equalsIgnoreCase(passedToken)) {
			matched = true;
		} else if (customerToken.getToken().equalsIgnoreCase(passedToken)) {
			matched = true;		
		} else {
			matched = false;
		}
		
		if (matched) {
			ConsignmentTracking dbConsignmentTracking = integrationService.getConsignmentTrackingByRefNumber(referenceNumber);
			return new ResponseEntity<>(dbConsignmentTracking, HttpStatus.OK);
		} else {
			throw new BadRequestException("Password wrong. Please enter correct password.");
		}
	}
	
	@ApiOperation(response = ConsignmentTracking.class, value = "Get a ConsignmentTracking") // label for swagger
	@GetMapping("/{wayBillNumber}/shipment/v2")
	public ResponseEntity<?> getConsignmentTrackingByWaybillNo(@RequestHeader(value="Authorization") String authorization,
			@PathVariable String wayBillNumber) {
		log.info("referenceNumber passed: " + wayBillNumber);
		String LIFETIME_TOKEN = propertiesConfig.getB2bIntegrationToken();
		
		if (!authorization.startsWith(BASIC)) {
			throw new BadRequestException("Authorization should be supplied prefixing with Basic<space> " + authorization);
		} 
		
		String passedToken = authorization.substring(6);
		AuthToken integAuthToken = authTokenService.getIntegrationServiceAuthToken();
		CustomerAccessToken customerToken = integrationService.getCustomerToken(passedToken, integAuthToken.getAccess_token());
		boolean matched = false;
		
		if (LIFETIME_TOKEN.equalsIgnoreCase(passedToken)) {
			matched = true;
		} else if (customerToken.getToken().equalsIgnoreCase(passedToken)) {
			matched = true;		
		} else {
			matched = false;
		}
		
		if (matched) {
			ConsignmentTracking dbConsignmentTracking = integrationService.getConsignmentTrackingByRefNumberV2(wayBillNumber);
			return new ResponseEntity<>(dbConsignmentTracking, HttpStatus.OK);
		} else {
			throw new BadRequestException("Password wrong. Please enter correct password.");
		}
	}
	
	//-----------------------------------WEBHOOK------------------------------------------------------------
	@ApiOperation(response = ConsignmentWebhook.class, value = "Consignment Webhook") // label for swagger
	@PostMapping("/tracking/webhook")
	public ResponseEntity<?> listenWebhook(@RequestHeader(value="Authorization") String authorization,
			@RequestBody ConsignmentWebhook consignmentWebhook) throws Exception {
		 if (consignmentWebhook.getHub_code() != null &&
				(consignmentWebhook.getHub_code().equalsIgnoreCase(JNT_HUBCODE) ||
								consignmentWebhook.getHub_code().equalsIgnoreCase(QAP_HUBCODE))) {
			log.info("Request Body received: " + consignmentWebhook);
			String WEBHOOK_TOKEN = "$2a$10$nzkfksOOLCI7mupALKmlzOQE7Zq6dCl15d4W9ZAvjR/laGXGhHt5G";
			
			if (!authorization.startsWith(BASIC)) {
				throw new BadRequestException("Authorization should be supplied prefixing with Basic<space> " + authorization);
			} 
			
			String passedToken = authorization.substring(6);
			log.info("extracted : " + passedToken);
			AuthToken integAuthToken = authTokenService.getIntegrationServiceAuthToken();
			CustomerAccessToken customerToken = integrationService.getCustomerToken(passedToken, integAuthToken.getAccess_token());
			boolean matched = false;
			
			if (WEBHOOK_TOKEN.equalsIgnoreCase(passedToken)) {
				matched = true;
			} else if (customerToken.getToken().equalsIgnoreCase(passedToken)) {
				matched = true;		
			} else {
				matched = false;
			}
			
			if (matched) {
				JNTResponse responseJNTResponse = integrationService.postConsignmentWebhookPayload(consignmentWebhook);
				log.info("consignmentWebhook response: " + responseJNTResponse);
				return new ResponseEntity<>( HttpStatus.OK );
			} else {
				throw new BadRequestException("Password wrong. Please enter correct password.");
			}
		}
		return new ResponseEntity<>( HttpStatus.OK );
	}
	
	//-----------------------------------JNT - WEBHOOK-----------------------------------------------------
	@ApiOperation(response = JNTWebhookRequest.class, value = "Post JNTWebhook") // label for swagger
	@RequestMapping(
			  path = "/tracking/jnt/webhook", 
			  method = RequestMethod.POST,
			  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, 
			  produces = {
			    MediaType.APPLICATION_JSON_VALUE
			  })
	public ResponseEntity<?> listenJNTWebhook(@RequestParam Map<String, String> webhookPayload) 
			throws Exception {
		String webhookrequest = webhookPayload.get("bizContent");
		log.info("bizContent------->" + webhookrequest);
		
		ObjectMapper mapper = new ObjectMapper();
		JNTWebhookRequest webhookRequest = mapper.readValue(webhookrequest, JNTWebhookRequest.class);
		log.info("webhookEntity------->" + webhookRequest);
		
		JNTWebhookEntity webhookResponse = integrationService.postJNTWebhook(webhookRequest);
		log.info("JNTWebhookEntity : " + webhookResponse);
		return new ResponseEntity<>(webhookResponse, HttpStatus.OK);
	}
	
	//-----------------------------------QATAR-POST-WEBHOOK-----------------------------------------------------
	@ApiOperation(response = QPTrackingRequest.class, value = "Post QPWebhook") 				// label for swagger
	@PostMapping("/tracking/qp/webhook")
	public ResponseEntity<?> listenQPWebhook(@RequestBody QPTrackingRequest qpTrackingRequest) throws Exception {
		QPTrackingResponse webhookResponse = integrationService.listenQPWebhook(qpTrackingRequest);
		log.info("JNTWebhookEntity : " + webhookResponse);
		return new ResponseEntity<>(webhookResponse, HttpStatus.OK);
	}
	
	//=====================================================================================================================
	public static void main(String[] args) throws IOException { 
      File file1 = new File("D:\\Murugavel\\Project\\7horses\\root\\IWB2B\\Code\\Java\\B2BWrapperService\\JTE000158012161.pdf"); 
      PDDocument doc1 = PDDocument.load(file1); 
      File file2 = new File("D:\\Murugavel\\Project\\7horses\\root\\IWB2B\\Code\\Java\\B2BWrapperService\\JTE000158829662.pdf"); 
      PDDocument doc2 = PDDocument.load(file2); 
      PDFMergerUtility PDFmerger = new PDFMergerUtility();       
      PDFmerger.setDestinationFileName("merged.pdf"); 
      PDFmerger.addSource(file1); 
      PDFmerger.addSource(file2); 
      PDFmerger.mergeDocuments(); 
      System.out.println("Documents merged"); 
      doc1.close(); 
      doc2.close();           
	}


	/*
	 * --------------------------------UserManagement---------------------------------
	 */
	@ApiOperation(response = UserAccess.class, value = "Get all UserAccess details") // label for swagger
	@GetMapping("/useraccess")
	public ResponseEntity<?> getAllUser(@RequestHeader String authToken) {
		UserAccess[] userAccessList = integrationService.getUserAccesss(authToken);
		return new ResponseEntity<>(userAccessList, HttpStatus.OK);
	}

	@ApiOperation(response = UserAccess.class, value = "Get a UserAccess") // label for swagger
	@GetMapping("/useraccess/{userId}")
	public ResponseEntity<?> getUserAccess(@PathVariable String userId,
										   @RequestHeader String authToken) {
		UserAccess dbUserAccess = integrationService.getUserAccess(userId, authToken);
		log.info("UserAccess : " + dbUserAccess);
		return new ResponseEntity<>(dbUserAccess, HttpStatus.OK);
	}

	@ApiOperation(response = UserAccess.class, value = "Create UserAccess") // label for swagger
	@PostMapping("/useraccess")
	public ResponseEntity<?> postUserAccess(@Valid @RequestBody AddUserAccess newUserAccess,
											@RequestParam String loginUserID, @RequestHeader String authToken)
			throws IllegalAccessException, InvocationTargetException {
		UserAccess createdUserAccess = integrationService.createUserAccess(newUserAccess, loginUserID, authToken);
		return new ResponseEntity<>(createdUserAccess, HttpStatus.OK);
	}

	@ApiOperation(response = UserAccess.class, value = "Update UserAccess") // label for swagger
	@RequestMapping(value = "/useraccess/{userId}", method = RequestMethod.PATCH)
	public ResponseEntity<?> patchUserAccess(@PathVariable String userId,
											 @RequestParam String loginUserID, @RequestHeader String authToken,
											 @Valid @RequestBody UpdateUserAccess updateUserAccess)
			throws IllegalAccessException, InvocationTargetException {
		UserAccess updatedUserAccess = integrationService.updateUserAccess(userId, loginUserID, updateUserAccess, authToken);
		return new ResponseEntity<>(updatedUserAccess, HttpStatus.OK);
	}

	@ApiOperation(response = UserAccess.class, value = "Delete UserAccess") // label for swagger
	@DeleteMapping("/useraccess/{userId}")
	public ResponseEntity<?> deleteUserAccess(@PathVariable String userId,
											  @RequestParam String loginUserID, @RequestHeader String authToken) {

		integrationService.deleteUserAccess(userId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	//FIND
	@ApiOperation(response = UserAccess[].class, value = "Find UserAccess")//label for swagger
	@PostMapping("/useraccess/findUserAccess")
	public UserAccess[] findUserAccess(@RequestBody FindUserAccess findUserAccess,
									   @RequestHeader String authToken)throws Exception{
		return integrationService.findUserAccess(findUserAccess,authToken);
	}

	/* --------------------------------LOGIN-------------------------------------------------*/

	@ApiOperation(response = Optional.class, value = "Login User") // label for swagger
	@RequestMapping(value = "/login", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> loginUser(@RequestParam String userName, @RequestParam String password,
										  @RequestHeader String authToken) {
		try {
			UserAccess loggedUser = integrationService.validateUserID(userName, password, authToken);
			log.info("LoginUser::: " + loggedUser);
			return new ResponseEntity<>(loggedUser, HttpStatus.OK);
		} catch (BadRequestException e) {
			log.error("Invalid user");
			String str = "Either UserId is invalid or Password does not match.";
			CustomErrorResponse error = new CustomErrorResponse();
			error.setTimestamp(LocalDateTime.now());
			error.setError(str);
			error.setStatus(HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
		}
	}

	/* --------------------------------DASHBOARD-------------------------------------------------*/

	@ApiOperation(response = DashboardCountOutput.class, value = "Get a DashboardCount") // label for swagger
	@PostMapping("/dashboard/getDashboardCount")
	public ResponseEntity<?> getDashboardCount(@RequestBody CountInput countInput, @RequestHeader String authToken) {
		DashboardCountOutput dbDashboardCount = integrationService.getDashboardCount(countInput, authToken);
		return new ResponseEntity<>(dbDashboardCount, HttpStatus.OK);
	}

	@ApiOperation(response = CustomerAccessToken.class, value = "Generate New Token") // label for swagger
	@GetMapping("/softdata/generate-token")
	public ResponseEntity<?> generateCustomerToken(@RequestHeader String authToken, @RequestParam String customerName) {
		CustomerAccessToken customerToken = integrationService.generateToken(customerName, authToken);
		return new ResponseEntity<>(customerToken, HttpStatus.OK);
	}

	@ApiOperation(response = Consignment.class, value = "Find Consignment") // label for swagger
	@PostMapping("/softdata/findConsignment")
	public ResponseEntity<?> findConsignment(@RequestBody FindConsignment findConsignment, @RequestHeader String authToken) throws Exception {
		Consignment[] response = integrationService.findConsignment(findConsignment, authToken);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
