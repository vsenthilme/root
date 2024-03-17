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

import com.mnrclara.api.crm.model.itform.ITForm007;
import com.mnrclara.api.crm.service.ITForm007Service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"ITForm007"}, value = "ITForm007 Operations related to ITForm007Controller") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ITForm007",description = "Operations related to ITForm007")})
@RequestMapping("/itform007")
@RestController
public class ITForm007Controller {
	
	@Autowired
	ITForm007Service itform007Service;
	
    @ApiOperation(response = ITForm007.class, value = "Get all ITForm007 details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ITForm007> itform007List = itform007Service.getITForm007s();
		log.info("itform007List : " + itform007List);	
		return new ResponseEntity<>(itform007List, HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm007.class, value = "Get a ITForm007") // label for swagger 
	@GetMapping("/id") 
	public ResponseEntity<?> getITForm007ById(@RequestParam String language,
												@RequestParam Long classID, 
												@RequestParam String matterNumber, 
												@RequestParam String clientId,
												@RequestParam String itFormNo,
												@RequestParam Long itFormID) {
    	ITForm007 itform007 = itform007Service.getITForm007(language, classID, matterNumber, clientId, itFormNo, itFormID);
    	log.info("ITForm007 : " + itform007);
    	return new ResponseEntity<>(itform007, HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm007.class, value = "Create ITForm007") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addITForm007(@Valid @RequestBody ITForm007 newITForm007, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ITForm007 createdITForm007 = itform007Service.createITForm007(newITForm007, loginUserID);
		return new ResponseEntity<>(createdITForm007 , HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm007.class, value = "Update ITForm007") // label for swagger
	@PatchMapping("")
	public ResponseEntity<?> updateITForm007(@RequestBody ITForm007 modifiedITForm007, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ITForm007 updatedITForm007 = itform007Service.updateITForm007(modifiedITForm007, loginUserID);
		return new ResponseEntity<>(updatedITForm007 , HttpStatus.OK);
	}
}