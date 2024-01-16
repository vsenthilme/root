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

import com.mnrclara.api.crm.model.itform.ITForm003;
import com.mnrclara.api.crm.model.itform.ITForm003Att;
import com.mnrclara.api.crm.service.ITForm003Service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"ITForm003"}, value = "ITForm003 Operations related to ITForm003Controller") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ITForm003",description = "Operations related to ITForm003")})
@RequestMapping("/itform003")
@RestController
public class ITForm003Controller {
	
	@Autowired
	ITForm003Service itform003Service;
	
    @ApiOperation(response = ITForm003.class, value = "Get all ITForm003 details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ITForm003> itform003List = itform003Service.getITForm003s();
		return new ResponseEntity<>(itform003List, HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm003.class, value = "Get a ITForm003") // label for swagger 
	@GetMapping("/id") 
	public ResponseEntity<?> getITForm003ById(@RequestParam String inquiryNo, 
												@RequestParam Long classID, 
												@RequestParam String language,
												@RequestParam String itFormNo,
												@RequestParam Long itFormID) {
    	ITForm003 itform001 = itform003Service.getITForm003(inquiryNo, classID, language, itFormNo, itFormID);
    	log.info("ITForm003 : " + itform001);
    	return new ResponseEntity<>(itform001, HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm003.class, value = "Create ITForm003") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addITForm003(@Valid @RequestBody ITForm003 newITForm003, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ITForm003 createdITForm003 = itform003Service.createITForm003(newITForm003, loginUserID);
		return new ResponseEntity<>(createdITForm003 , HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm003.class, value = "Update ITForm003") // label for swagger
	@PatchMapping("")
	public ResponseEntity<?> updateITForm003(@RequestBody ITForm003 modifiedITForm003, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ITForm003 updatedITForm003 = itform003Service.updateITForm003(modifiedITForm003, loginUserID);
		return new ResponseEntity<>(updatedITForm003 , HttpStatus.OK);
	}
    
    //---------------------------ITFORM003-Attorney----------------------------------------------------------
    @ApiOperation(response = ITForm003.class, value = "Get a ITForm003-Attorney") // label for swagger 
	@GetMapping("/attorney") 
	public ResponseEntity<?> getITForm003AttById(@RequestParam String inquiryNo, 
												@RequestParam Long classID, 
												@RequestParam String language,
												@RequestParam String itFormNo,
												@RequestParam Long itFormID) {
    	ITForm003Att itform003Att = itform003Service.getITForm003Att(inquiryNo, classID, language, itFormNo, itFormID);
    	log.info("ITForm003Att : " + itform003Att);
    	return new ResponseEntity<>(itform003Att, HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm003.class, value = "Create ITForm003-Attorney") // label for swagger
   	@PostMapping("/attorney")
   	public ResponseEntity<?> addITForm003Attorney(@Valid @RequestBody ITForm003Att newITForm003Att, @RequestParam String loginUserID) 
   			throws IllegalAccessException, InvocationTargetException {
   		ITForm003Att createdITForm003Att = itform003Service.createITForm003Att(newITForm003Att, loginUserID);
   		return new ResponseEntity<>(createdITForm003Att , HttpStatus.OK);
   	}
}