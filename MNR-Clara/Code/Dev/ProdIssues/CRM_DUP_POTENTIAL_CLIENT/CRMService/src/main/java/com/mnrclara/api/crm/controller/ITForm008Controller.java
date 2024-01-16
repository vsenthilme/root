package com.mnrclara.api.crm.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.crm.model.itform.ITForm008;
import com.mnrclara.api.crm.service.ITForm008Service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"ITForm008"}, value = "ITForm008 Operations related to ITForm008Controller") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ITForm008",description = "Operations related to ITForm008")})
@RequestMapping("/itform008")
@RestController
public class ITForm008Controller {
	
	@Autowired
	ITForm008Service itform008Service;
	
    @ApiOperation(response = ITForm008.class, value = "Get all ITForm008 details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ITForm008> itform008List = itform008Service.getITForm008s();
		log.info("itform008List : " + itform008List);	
		return new ResponseEntity<>(itform008List, HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm008.class, value = "Get a ITForm008") // label for swagger 
	@GetMapping("/id") 
	public ResponseEntity<?> getITForm008ById(@RequestParam String language,
												@RequestParam Long classID, 
												@RequestParam String matterNumber, 
												@RequestParam String clientId,
												@RequestParam String itFormNo,
												@RequestParam Long itFormID) {
    	ITForm008 itform008 = itform008Service.getITForm008(language, classID, matterNumber, clientId, itFormNo, itFormID);
    	log.info("ITForm008 : " + itform008);
    	return new ResponseEntity<>(itform008, HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm008.class, value = "Create ITForm008") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addITForm008(@Valid @RequestBody ITForm008 newITForm008, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ITForm008 createdITForm008 = itform008Service.createITForm008(newITForm008, loginUserID);
		return new ResponseEntity<>(createdITForm008 , HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm008.class, value = "Update ITForm008") // label for swagger
	@PatchMapping("")
	public ResponseEntity<?> updateITForm008(@RequestBody ITForm008 modifiedITForm008, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ITForm008 updatedITForm008 = itform008Service.updateITForm008(modifiedITForm008, loginUserID);
		return new ResponseEntity<>(updatedITForm008 , HttpStatus.OK);
	}
}