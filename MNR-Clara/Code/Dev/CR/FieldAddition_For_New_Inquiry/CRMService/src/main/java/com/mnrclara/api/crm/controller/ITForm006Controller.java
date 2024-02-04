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

import com.mnrclara.api.crm.model.itform.ITForm006;
import com.mnrclara.api.crm.service.ITForm006Service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"ITForm006"}, value = "ITForm006 Operations related to ITForm006Controller") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ITForm006",description = "Operations related to ITForm006")})
@RequestMapping("/itform006")
@RestController
public class ITForm006Controller {
	
	@Autowired
	ITForm006Service itform006Service;
	
    @ApiOperation(response = ITForm006.class, value = "Get all ITForm006 details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ITForm006> itform006List = itform006Service.getITForm006s();
		return new ResponseEntity<>(itform006List, HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm006.class, value = "Get a ITForm006") // label for swagger 
	@GetMapping("/id") 
	public ResponseEntity<?> getITForm006ById(@RequestParam String inquiryNo, 
												@RequestParam Long classID, 
												@RequestParam String language,
												@RequestParam String itFormNo,
												@RequestParam Long itFormID) {
    	ITForm006 itform001 = itform006Service.getITForm006(inquiryNo, classID, language, itFormNo, itFormID);
    	log.info("ITForm006 : " + itform001);
    	return new ResponseEntity<>(itform001, HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm006.class, value = "Create ITForm006") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addITForm006(@Valid @RequestBody ITForm006 newITForm006, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ITForm006 createdITForm006 = itform006Service.createITForm006(newITForm006, loginUserID);
		return new ResponseEntity<>(createdITForm006 , HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm006.class, value = "Update ITForm006") // label for swagger
	@PatchMapping("")
	public ResponseEntity<?> updateITForm006(@RequestBody ITForm006 modifiedITForm006, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ITForm006 updatedITForm006 = itform006Service.updateITForm006(modifiedITForm006, loginUserID);
		return new ResponseEntity<>(updatedITForm006 , HttpStatus.OK);
	}
}