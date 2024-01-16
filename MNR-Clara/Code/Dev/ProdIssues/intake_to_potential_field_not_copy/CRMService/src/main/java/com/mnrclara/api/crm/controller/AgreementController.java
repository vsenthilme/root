package com.mnrclara.api.crm.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

import com.mnrclara.api.crm.model.agreement.AddAgreement;
import com.mnrclara.api.crm.model.agreement.Agreement;
import com.mnrclara.api.crm.model.agreement.SearchAgreement;
import com.mnrclara.api.crm.model.agreement.UpdateAgreement;
import com.mnrclara.api.crm.model.auth.AuthToken;
import com.mnrclara.api.crm.model.dto.EnvelopeStatus;
import com.mnrclara.api.crm.service.AgreementService;
import com.mnrclara.api.crm.service.CommonService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"Agreement"}, value = "Agreement Operations related to AgreementController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "Agreement",description = "Operations related to Agreement")})
@RequestMapping("/agreement")
@RestController
public class AgreementController {

	@Autowired
	AgreementService agreementService;

	@Autowired
	CommonService commonService;

    @ApiOperation(response = Agreement.class, value = "Get all Agreement details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<Agreement> agreementList = agreementService.getAgreements();
		return new ResponseEntity<>(agreementList, HttpStatus.OK);
	}

    @ApiOperation(response = Agreement.class, value = "Get a Agreement") // label for swagger 
	@GetMapping("/{agreementCode}")
	public ResponseEntity<?> getAgreement(@PathVariable String agreementCode) {
    	Agreement agreement = agreementService.getAgreement(agreementCode);
    	log.info("Agreement : " + agreement);
    	return new ResponseEntity<>(agreement, HttpStatus.OK);
	}

    @ApiOperation(response = Agreement.class, value = "Get a Agreement") // label for swagger 
	@GetMapping("/{potentialClientId}/potentialClientId")
	public ResponseEntity<?> getAgreementByPotentialClientId(@PathVariable String potentialClientId) {
    	Agreement agreement = agreementService.getAgreementByPotentialClientId(potentialClientId);
    	log.info("Agreement : " + agreement);
    	return new ResponseEntity<>(agreement, HttpStatus.OK);
	}

    @ApiOperation(response = Agreement.class, value = "Find Agreement") // label for swagger
    @PostMapping("/findAgreement")
    public List<Agreement> findAgreement(@RequestBody SearchAgreement searchAgreement) throws ParseException {
		return agreementService.findAgreement(searchAgreement);
	}

    @ApiOperation(response = Agreement.class, value = "Send Envelope to Docusign") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postAgreement(@RequestBody AddAgreement addAgreement,
			@RequestParam String potentialClientId, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		Agreement createdAgreement = agreementService.createAgreement(addAgreement, potentialClientId, loginUserID);
		return new ResponseEntity<>(createdAgreement , HttpStatus.OK);
	}

    @ApiOperation(response = Agreement.class, value = "Update Agreement") // label for swagger
    @PatchMapping("/{agreementCode}")
	public ResponseEntity<?> patchAgreement(@PathVariable String agreementCode,
			@RequestParam String loginUserId,
			@Valid @RequestBody UpdateAgreement updateAgreement)
			throws IllegalAccessException, InvocationTargetException {
		Agreement updatedAgreement = agreementService.updateAgreement(agreementCode, updateAgreement, loginUserId);
		return new ResponseEntity<>(updatedAgreement , HttpStatus.OK);
	}

    @ApiOperation(response = Agreement.class, value = "Update Agreement") // label for swagger
    @PatchMapping("/{agreementCode}/fromDocusign")
	public ResponseEntity<?> patchAgreementFromDocusign(@PathVariable String agreementCode,
			@RequestParam String potentialClientId,
			@RequestParam String loginUserId,
			@Valid @RequestBody UpdateAgreement updateAgreement)
			throws IllegalAccessException, InvocationTargetException {
		Agreement updatedAgreement =
				agreementService.updateAgreementFromDocusignFlow(agreementCode, potentialClientId, updateAgreement, loginUserId);
		return new ResponseEntity<>(updatedAgreement , HttpStatus.OK);
	}

    @ApiOperation(response = Agreement.class, value = "Update Agreement Status") // label for swagger
    @PatchMapping("/{agreementCode}/status")
	public ResponseEntity<?> patchAgreement(@PathVariable String agreementCode,
			@RequestParam String loginUserId, @RequestParam Long status)
			throws IllegalAccessException, InvocationTargetException {
		Agreement updatedAgreement = agreementService.updateAgreementStatus(agreementCode, status, loginUserId);
		return new ResponseEntity<>(updatedAgreement , HttpStatus.OK);
	}

    @ApiOperation(response = Agreement.class, value = "Delete Agreement") // label for swagger
	@DeleteMapping("/{agreementCode}")
	public ResponseEntity<?> deleteAgreement(@PathVariable String agreementCode, @RequestParam String loginUserId) {
    	agreementService.deleteAgreement(agreementCode, loginUserId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

    @ApiOperation(response = Optional.class, value = "DocuSign") // label for swagger
   	@GetMapping("/docusign/token")
   	public ResponseEntity<?> getToken(@RequestParam String code)
   			throws Exception {
    	AuthToken response = agreementService.genToken(code);
   		return new ResponseEntity<>(response, HttpStatus.OK);
   	}

    @ApiOperation(response = Optional.class, value = "DocuSign") // label for swagger
   	@GetMapping("/docusign/envelope/status")
   	public ResponseEntity<?> getStatusFromDocusign(@RequestParam String potentialClientId) throws Exception {
    	EnvelopeStatus response = agreementService.getDocusignEnvelopeStatus(potentialClientId);
        agreementService.sendNotification(potentialClientId);
   		return new ResponseEntity<>(response, HttpStatus.OK);
   	}

    @ApiOperation(response = Optional.class, value = "DocuSign") // label for swagger
   	@GetMapping("/docusign/envelope/download")
   	public ResponseEntity<?> downloadEnvelopeFromDocusign(@RequestParam String potentialClientId,
   			@RequestParam String loginUserID) throws Exception {
    	String response = agreementService.downloadEnvelopeFromDocusign(potentialClientId, loginUserID);
   		return new ResponseEntity<>(response, HttpStatus.OK);
   	}
}