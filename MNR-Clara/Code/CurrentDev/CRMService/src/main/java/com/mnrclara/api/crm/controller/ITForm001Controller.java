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

import com.mnrclara.api.crm.model.itform.ITForm001;
import com.mnrclara.api.crm.service.ITForm001Service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"ITForm001"}, value = "ITForm001 Operations related to ITForm001Controller") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ITForm001",description = "Operations related to ITForm001")})
@RequestMapping("/itform001")
@RestController
public class ITForm001Controller {
	
	@Autowired
	ITForm001Service itform001Service;
	
    @ApiOperation(response = ITForm001.class, value = "Get all ITForm001 details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ITForm001> itform001List = itform001Service.getITForm001s();
		return new ResponseEntity<>(itform001List, HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm001.class, value = "Get a ITForm001") // label for swagger 
	@GetMapping("/id") 
	public ResponseEntity<?> getITForm001ById(@RequestParam String inquiryNo, 
												@RequestParam Long classID, 
												@RequestParam String language,
												@RequestParam String itFormNo,
												@RequestParam Long itFormID) {
    	ITForm001 itform001 = itform001Service.getITForm001(inquiryNo, classID, language, itFormNo, itFormID);
    	log.info("ITForm001 : " + itform001);
    	return new ResponseEntity<>(itform001, HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm001.class, value = "Create ITForm001") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addITForm001(@Valid @RequestBody ITForm001 newITForm001, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ITForm001 createdITForm001 = itform001Service.createITForm001(newITForm001, loginUserID);
		return new ResponseEntity<>(createdITForm001 , HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm001.class, value = "Update ITForm001") // label for swagger
	@PatchMapping("")
	public ResponseEntity<?> updateITForm001(@RequestBody ITForm001 modifiedITForm001, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ITForm001 updatedITForm001 = itform001Service.updateITForm001(modifiedITForm001, loginUserID);
		return new ResponseEntity<>(updatedITForm001 , HttpStatus.OK);
	}
}