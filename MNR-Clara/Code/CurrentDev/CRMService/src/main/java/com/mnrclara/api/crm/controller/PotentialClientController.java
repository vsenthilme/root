package com.mnrclara.api.crm.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.crm.model.potentialclient.ClientGeneral;
import com.mnrclara.api.crm.model.potentialclient.PotentialClient;
import com.mnrclara.api.crm.model.potentialclient.ReferralReport;
import com.mnrclara.api.crm.model.potentialclient.SearchPotentialClient;
import com.mnrclara.api.crm.model.potentialclient.SearchPotentialClientReport;
import com.mnrclara.api.crm.model.potentialclient.SearchReferralReport;
import com.mnrclara.api.crm.model.potentialclient.UpdatePotentialClient;
import com.mnrclara.api.crm.model.potentialclient.UpdatePotentialClientAgreement;
import com.mnrclara.api.crm.service.CommonService;
import com.mnrclara.api.crm.service.PotentialClientService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"PotentialClient"}, value = "PotentialClient Operations related to PotentialClientController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "PotentialClient",description = "Operations related to PotentialClient")})
@RequestMapping("/potentialClient")
@RestController
public class PotentialClientController {
	
	@Autowired
	PotentialClientService potentialClientService;
	
	@Autowired
	CommonService commonService;
	
    @ApiOperation(response = PotentialClient.class, value = "Get all PotentialClient details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<PotentialClient> potentialClientList = potentialClientService.getPotentialClients();
		return new ResponseEntity<>(potentialClientList, HttpStatus.OK);
	}
    
    @ApiOperation(response = PotentialClient.class, value = "Get a PotentialClient") // label for swagger 
	@GetMapping("/{potentialClientId}")
	public ResponseEntity<?> getPotentialClient(@PathVariable String potentialClientId) {
    	PotentialClient potentialClient = potentialClientService.getPotentialClient(potentialClientId);
    	log.info("PotentialClient : " + potentialClient);
    	return new ResponseEntity<>(potentialClient, HttpStatus.OK);
	}
    
    @ApiOperation(response = PotentialClient.class, value = "Find PotentialClient") // label for swagger
    @PostMapping("/findPotentialClient")
    public List<PotentialClient> findPotentialClient(@RequestBody SearchPotentialClient searchPotentialClient) throws ParseException {
		return potentialClientService.findPotentialClient(searchPotentialClient);
	}
    
    @ApiOperation(response = PotentialClient.class, value = "Create Client General") // label for swagger
	@GetMapping("/{potentialClientId}/clientGeneral")
	public ResponseEntity<?> getClientGeneral(@PathVariable String potentialClientId, @RequestParam String loginUserID ) 
			throws IllegalAccessException, InvocationTargetException {
		ClientGeneral createdClientGeneral = potentialClientService.createClientGeneral(potentialClientId, loginUserID);
		return new ResponseEntity<>(createdClientGeneral , HttpStatus.OK);
	}
    
    @ApiOperation(response = PotentialClient.class, value = "Update PotentialClient") // label for swagger
    @PatchMapping("/{potentialClientId}")
	public ResponseEntity<?> patchPotentialClient(@PathVariable String potentialClientId, @RequestParam String loginUserID,
			@Valid @RequestBody UpdatePotentialClient updatePotentialClient)
			throws IllegalAccessException, InvocationTargetException {
		PotentialClient updatedPotentialClient = 
				potentialClientService.updatePotentialClient(potentialClientId, loginUserID, updatePotentialClient);
		return new ResponseEntity<>(updatedPotentialClient , HttpStatus.OK);
	}
    
    @ApiOperation(response = PotentialClient.class, value = "Update PotentialClient Agreement") // label for swagger
    @PatchMapping("/{potentialClientId}/agreement")
	public ResponseEntity<?> patchPotentialClientAgreement(@PathVariable String potentialClientId, @RequestParam String loginUserID,
			@Valid @RequestBody UpdatePotentialClientAgreement updatePotentialClientAgreement) 
			throws IllegalAccessException, InvocationTargetException {
		PotentialClient updatedPotentialClient = 
				potentialClientService.updatePotentialClientAgreement(potentialClientId, loginUserID, updatePotentialClientAgreement);
		return new ResponseEntity<>(updatedPotentialClient , HttpStatus.OK);
	}
    
    @ApiOperation(response = PotentialClient.class, value = "Update PotentialClient") // label for swagger
    @PatchMapping("/{potentialClientId}/status")
	public ResponseEntity<?> patchPotentialClient(@PathVariable String potentialClientId, @RequestParam String loginUserID,
			@RequestParam Long status) 
			throws IllegalAccessException, InvocationTargetException {
		PotentialClient updatedPotentialClient = 
				potentialClientService.updatePotentialClientStatus(potentialClientId, loginUserID, status);
		return new ResponseEntity<>(updatedPotentialClient , HttpStatus.OK);
	}
    
    @ApiOperation(response = PotentialClient.class, value = "Delete PotentialClient") // label for swagger
	@DeleteMapping("/{potentialClientId}")
	public ResponseEntity<?> deletePotentialClient(@PathVariable String potentialClientId, @RequestParam String loginUserID) {
		boolean isDeleted = potentialClientService.deletePotentialClient(potentialClientId, loginUserID);
		log.info("isDeleted : " + isDeleted);
		if (isDeleted) {
			return new ResponseEntity<>("Potential Client Id : " + potentialClientId + " deleted sucessfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Error in Deleteing ID: " + potentialClientId , HttpStatus.BAD_REQUEST);
		}
	}
    
    //------------------------------------Reports------------------------------------------------------------------
    @ApiOperation(response = PotentialClient[].class, value = "PotentialClient Report") // label for swagger
    @PostMapping("/report")
    public List<PotentialClient> getPotentialClientReport(@RequestBody SearchPotentialClientReport searchPotentialClientReport) 
    		throws ParseException {
		return potentialClientService.getPotentialClientReport(searchPotentialClientReport);
	}
    
    @ApiOperation(response = ReferralReport[].class, value = "Referral report") // label for swagger
    @PostMapping("/referralReport")
    public List<ReferralReport> getReferralReport(@RequestBody SearchReferralReport searchReferralReport) 
    		throws ParseException {
		return potentialClientService.getReferralReport(searchReferralReport);
	}
}