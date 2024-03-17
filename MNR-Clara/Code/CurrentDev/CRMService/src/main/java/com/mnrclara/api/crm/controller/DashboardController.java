package com.mnrclara.api.crm.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.crm.model.dto.Dashboard;
import com.mnrclara.api.crm.service.AgreementService;
import com.mnrclara.api.crm.service.InquiryService;
import com.mnrclara.api.crm.service.PCIntakeFormService;
import com.mnrclara.api.crm.service.PotentialClientService;
import com.mnrclara.api.crm.util.CommonUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@Api(tags = {"Dashboard"}, value = "DashboardController Operations") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "User",description = "Operations related to Dashboard")})
@RequestMapping("/dashboard")
public class DashboardController {
	
	@Autowired
	InquiryService inquiryService;
	
	@Autowired
	PCIntakeFormService pcIntakeFormService;
	
	@Autowired
	PotentialClientService potentialClientService;
	
	@Autowired
	AgreementService agreementService;
	
	/* 
	 * Dashboard - Inquiry
	 * ----------------------
	 * STATUS_ID=03,04
	 */
    @ApiOperation(response = Optional.class, value = "Dashboard - Inquiry") // label for swagger
	@GetMapping("/inquiry")
	public ResponseEntity<?> inquiryDashboard (@RequestParam String loginUserId) {
		Dashboard inquiryCount = inquiryService.getInquiryCount(loginUserId);
    	return new ResponseEntity<>(inquiryCount, HttpStatus.OK);
	}
    
    /*
     * Dashboard - Intake
     * -------------------
     * STATUS_ID=07,08,09
     */
    @ApiOperation(response = Optional.class, value = "Dashboard - Intake") // label for swagger
	@GetMapping("/pcIntakeForm")
	public ResponseEntity<?> pcIntakeFormDashboard (@RequestParam String loginUserId) {
    	Dashboard inquiryCount = pcIntakeFormService.getPCIntakeFormCount(loginUserId);
    	return new ResponseEntity<>(inquiryCount, HttpStatus.OK);
	}
    
    /*
     * Dashboard - Prospective Client
     * --------------------------------
     * STATUS_ID=11,12,13,14
     */
    @ApiOperation(response = Optional.class, value = "Dashboard - Prospective Client") // label for swagger
	@GetMapping("/potentialClient")
	public ResponseEntity<?> potentialClientDashboard (@RequestParam String loginUserId) {
    	Dashboard potentialClientCount = potentialClientService.getPotentialClientCount(loginUserId);
    	return new ResponseEntity<>(potentialClientCount, HttpStatus.OK);
	}
    
    /*
     * Dashboard - Agreement
     * -------------------------
     * STATUS_ID=12,13,14
     */
    @ApiOperation(response = Optional.class, value = "Dashboard - Agreement") // label for swagger
	@GetMapping("/agreement")
	public ResponseEntity<?> agreementCountDashboard (@RequestParam String loginUserId) {
    	Dashboard agreementCount = agreementService.getAgreementCount(loginUserId, CommonUtils.DashboardTypes.AGREEMENT.name());
    	return new ResponseEntity<>(agreementCount, HttpStatus.OK);
	}
    
    /*
     * Dashboard - Total
     * ------------------------
     * STATUS_ID=12,13,14,15,16
     */
    @ApiOperation(response = Optional.class, value = "Dashboard - Agreement Total") // label for swagger
	@GetMapping("/agreementTotal")
	public ResponseEntity<?> agreementTotalDashboard (@RequestParam String loginUserId) {
    	Dashboard agreementCount = agreementService.getAgreementCount(loginUserId, CommonUtils.DashboardTypes.AGREEMENT_TOTAL.name());
    	return new ResponseEntity<>(agreementCount, HttpStatus.OK);
	}
    
    /*
     * Dashboard - Sent
     * ------------------------
     * STATUS_ID=12,13
     */
    @ApiOperation(response = Optional.class, value = "Dashboard - AGREEMENT_SENT") // label for swagger
	@GetMapping("/agreementSent")
	public ResponseEntity<?> agreementSentDashboard (@RequestParam String loginUserId) {
		Dashboard agreementCount = agreementService.getAgreementCount(loginUserId, CommonUtils.DashboardTypes.AGREEMENT_SENT.name());
    	return new ResponseEntity<>(agreementCount, HttpStatus.OK);
	}
    
    /*
     * Dashboard - Received
     * ------------------------
     * STATUS_ID=14,15
     */
    @ApiOperation(response = Optional.class, value = "Dashboard - AGREEMENT_RECEIVED") // label for swagger
	@GetMapping("/agreementReceived")
	public ResponseEntity<?> agreementReceivedDashboard (@RequestParam String loginUserId) {
		Dashboard agreementCount = agreementService.getAgreementCount(loginUserId, CommonUtils.DashboardTypes.AGREEMENT_RECEIVED.name());
    	return new ResponseEntity<>(agreementCount, HttpStatus.OK);
	}
    
    /*
     * Dashboard - Resent
     * ------------------------
     * STATUS_ID=16
     */
    @ApiOperation(response = Optional.class, value = "Dashboard - AGREEMENT_RESENT") // label for swagger
	@GetMapping("/agreementResent")
	public ResponseEntity<?> agreementResentDashboard (@RequestParam String loginUserId) {
		Dashboard agreementCount = agreementService.getAgreementCount(loginUserId, CommonUtils.DashboardTypes.AGREEMENT_RESENT.name());
    	return new ResponseEntity<>(agreementCount, HttpStatus.OK);
	}
}