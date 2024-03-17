package com.mnrclara.wrapper.core.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.wrapper.core.model.crm.Dashboard;
import com.mnrclara.wrapper.core.service.DashboardService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/mnr-crm-service/dashboard")
@Api(tags = { "CRM Dashboard Service" }, value = "CRM Dashboard Service Operations") // label for swagger
@SwaggerDefinition(tags = { @Tag(name = "User", description = "Operations related to CRM Dashboard Modules") })
public class DashboardController {

	@Autowired
	DashboardService dashboardService;

	@ApiOperation(response = Optional.class, value = "Dashboard - Inquiry") // label for swagger
	@GetMapping("/inquiry")
	public ResponseEntity<?> inquiryDashboard (@RequestParam String loginUserID, @RequestParam String authToken) {
		Dashboard inquiryCount = dashboardService.getInquiryCount(loginUserID, authToken);
    	return new ResponseEntity<>(inquiryCount, HttpStatus.OK);
	}
	
	@ApiOperation(response = Optional.class, value = "Dashboard - Intake") // label for swagger
	@GetMapping("/pcIntakeForm")
	public ResponseEntity<?> pcIntakeFormDashboard (@RequestParam String loginUserID, @RequestParam String authToken) {
    	Dashboard inquiryCount = dashboardService.getPCIntakeFormCount(loginUserID, authToken);
    	return new ResponseEntity<>(inquiryCount, HttpStatus.OK);
	}
    
    @ApiOperation(response = Optional.class, value = "Dashboard - Prospective Client") // label for swagger
	@GetMapping("/potentialClient")
	public ResponseEntity<?> potentialClientDashboard (@RequestParam String loginUserID, @RequestParam String authToken) {
    	Dashboard potentialClientCount = dashboardService.getPotentialClientCount(loginUserID, authToken);
    	return new ResponseEntity<>(potentialClientCount, HttpStatus.OK);
	}
    
    @ApiOperation(response = Optional.class, value = "Dashboard - Agreement") // label for swagger
	@GetMapping("/agreement")
	public ResponseEntity<?> agreementCountDashboard (@RequestParam String loginUserID, @RequestParam String authToken) {
    	Dashboard agreementCount = dashboardService.getAgreementCount(loginUserID, authToken);
    	return new ResponseEntity<>(agreementCount, HttpStatus.OK);
	}
    
    @ApiOperation(response = Optional.class, value = "Dashboard - Agreement Total") // label for swagger
	@GetMapping("/agreementTotal")
	public ResponseEntity<?> agreementTotalDashboard (@RequestParam String loginUserID, @RequestParam String authToken) {
    	Dashboard agreementCount = dashboardService.getAgreementTotalCount(loginUserID, authToken);
    	return new ResponseEntity<>(agreementCount, HttpStatus.OK);
	}
    
    @ApiOperation(response = Optional.class, value = "Dashboard - AGREEMENT_SENT") // label for swagger
	@GetMapping("/agreementSent")
	public ResponseEntity<?> agreementSentDashboard (@RequestParam String loginUserID, @RequestParam String authToken) {
		Dashboard agreementCount = dashboardService.getAgreementSentCount(loginUserID, authToken);
    	return new ResponseEntity<>(agreementCount, HttpStatus.OK);
	}
    
    @ApiOperation(response = Optional.class, value = "Dashboard - AGREEMENT_RECEIVED") // label for swagger
	@GetMapping("/agreementReceived")
	public ResponseEntity<?> agreementReceivedDashboard (@RequestParam String loginUserID, @RequestParam String authToken) {
		Dashboard agreementCount = dashboardService.getAgreementReceivedCount(loginUserID, authToken);
    	return new ResponseEntity<>(agreementCount, HttpStatus.OK);
	}
    
    @ApiOperation(response = Optional.class, value = "Dashboard - AGREEMENT_RESENT") // label for swagger
	@GetMapping("/agreementResent")
	public ResponseEntity<?> agreementResentDashboard (@RequestParam String loginUserID, @RequestParam String authToken) {
		Dashboard agreementCount = dashboardService.getAgreementResentCount(loginUserID, authToken);
    	return new ResponseEntity<>(agreementCount, HttpStatus.OK);
	}
}