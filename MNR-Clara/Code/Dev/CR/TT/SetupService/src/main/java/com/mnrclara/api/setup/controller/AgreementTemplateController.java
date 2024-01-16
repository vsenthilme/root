package com.mnrclara.api.setup.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.mnrclara.api.setup.model.agreementtemplate.AddAgreementTemplate;
import com.mnrclara.api.setup.model.agreementtemplate.AgreementTemplate;
import com.mnrclara.api.setup.model.agreementtemplate.UpdateAgreementTemplate;
import com.mnrclara.api.setup.service.AgreementTemplateService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"AgreementTemplate"}, value = "AgreementTemplate Operations related to AgreementTemplateController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "AgreementTemplate",description = "Operations related to AgreementTemplate")})
@RequestMapping("/agreementTemplate")
@RestController
public class AgreementTemplateController {
	
	@Autowired
	AgreementTemplateService agreementTemplateService;
	
    @ApiOperation(response = AgreementTemplate.class, value = "Get all AgreementTemplate details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<AgreementTemplate> aggrementTemplateList = agreementTemplateService.getAgreementTemplates();
		return new ResponseEntity<>(aggrementTemplateList, HttpStatus.OK);
	}
    
    @ApiOperation(response = AgreementTemplate.class, value = "Get a AgreementTemplate") // label for swagger 
	@GetMapping("/{agreementCode}")
	public ResponseEntity<?> getAgreementTemplate(@PathVariable String agreementCode) {
    	AgreementTemplate aggrementTemplate = agreementTemplateService.getAgreementTemplate(agreementCode);
    	log.info("AgreementTemplate : " + aggrementTemplate);
		return new ResponseEntity<>(aggrementTemplate, HttpStatus.OK);
	}
    
    @ApiOperation(response = AgreementTemplate.class, value = "Get a AgreementTemplate") // label for swagger 
	@GetMapping("/{agreementCode}/documentUrl")
	public ResponseEntity<?> getAgreementTemplateDocURL(@PathVariable String agreementCode) {
    	AgreementTemplate aggrementTemplate = agreementTemplateService.getAgreementTemplate(agreementCode);
    	log.info("DocURL : " + aggrementTemplate.getAgreementUrl()); 
    	log.info("MailMerge : " + aggrementTemplate.getMailMerge());
    	Map<String, String> kayValueMap = new HashMap<>();
    	kayValueMap.put("documentUrl", aggrementTemplate.getAgreementUrl());
    	kayValueMap.put("mailMerge", String.valueOf(aggrementTemplate.getMailMerge()));
		return new ResponseEntity<>(kayValueMap, HttpStatus.OK);
	}
    
    //Pass CLASS_ID/CASE_CATEGORY_ID in AGREEMENTTEMPLATE table and fetch AGREEMENT_CODE/AGREEMENT_TEXT values where AGREEMENT_STATUS = ACTIVE and display in dropdown as Combination
    @ApiOperation(response = AgreementTemplate.class, value = "Get a AgreementTemplate") // label for swagger 
	@GetMapping("/{agreementCode}/classId/{classId}")
	public ResponseEntity<?> getAgreementTemplateByClassIdAndagreementCode(@PathVariable String agreementCode,
			@RequestParam Long classId) {
    	AgreementTemplate aggrementTemplate = agreementTemplateService.getAgreementTemplate(agreementCode);
    	log.info("AgreementTemplate : " + aggrementTemplate);
		return new ResponseEntity<>(aggrementTemplate, HttpStatus.OK);
	}
    
    @ApiOperation(response = AgreementTemplate.class, value = "Create AgreementTemplate") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addAgreementTemplate(@Valid @RequestBody AddAgreementTemplate newAgreementTemplate,
			@RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		AgreementTemplate createdAgreementTemplate 
			= agreementTemplateService.createAgreementTemplate(newAgreementTemplate, loginUserID);
		return new ResponseEntity<>(createdAgreementTemplate , HttpStatus.OK);
	}
    
    @ApiOperation(response = AgreementTemplate.class, value = "Update AgreementTemplate") // label for swagger
    @PatchMapping("/{agreementCode}")
	public ResponseEntity<?> patchAgreementTemplate(@PathVariable String agreementCode, @RequestParam String loginUserID, 
			@Valid @RequestBody UpdateAgreementTemplate updateAgreementTemplate) 
			throws IllegalAccessException, InvocationTargetException {
		AgreementTemplate updatedAgreementTemplate = agreementTemplateService.updateAgreementTemplate(agreementCode, loginUserID, updateAgreementTemplate);
		return new ResponseEntity<>(updatedAgreementTemplate , HttpStatus.OK);
	}
    
    @ApiOperation(response = AgreementTemplate.class, value = "Delete AgreementTemplate") // label for swagger
	@DeleteMapping("/{agreementCode}")
	public ResponseEntity<?> deleteAgreementTemplate(@PathVariable String agreementCode, @RequestParam String loginUserID) {
    	agreementTemplateService.deleteAgreementTemplate(agreementCode, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}