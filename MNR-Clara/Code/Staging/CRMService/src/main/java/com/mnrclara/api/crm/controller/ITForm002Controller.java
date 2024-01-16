package com.mnrclara.api.crm.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
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

import com.mnrclara.api.crm.model.itform.ITForm002;
import com.mnrclara.api.crm.model.itform.ITForm002Att;
import com.mnrclara.api.crm.service.ITForm002Service;
import com.mnrclara.api.crm.util.CommonUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"ITForm002"}, value = "ITForm002 Operations related to ITForm002Controller") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ITForm002",description = "Operations related to ITForm002")})
@RequestMapping("/itform002")
@RestController
public class ITForm002Controller {
	
	@Autowired
	ITForm002Service itform002Service;
	
    @ApiOperation(response = ITForm002.class, value = "Get all ITForm002 details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ITForm002> itform002List = itform002Service.getITForm002s();
		return new ResponseEntity<>(itform002List, HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm002.class, value = "Get a ITForm002") // label for swagger 
	@GetMapping("/id") 
	public ResponseEntity<?> getITForm002ById(@RequestParam String inquiryNo, 
												@RequestParam Long classID, 
												@RequestParam String language,
												@RequestParam String itFormNo,
												@RequestParam Long itFormID) {
    	ITForm002 itform002 = itform002Service.getITForm002(inquiryNo, classID, language, itFormNo, itFormID);
    	log.info("ITForm002 : " + itform002);
    	return new ResponseEntity<>(itform002, HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm002.class, value = "Create ITForm002") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addITForm002(@Valid @RequestBody ITForm002 newITForm002, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ITForm002 createdITForm002 = itform002Service.createITForm002(newITForm002, loginUserID);
		return new ResponseEntity<>(createdITForm002 , HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm002.class, value = "Update ITForm002") // label for swagger
	@PatchMapping("")
	public ResponseEntity<?> updateITForm002(@RequestBody ITForm002 modifiedITForm002, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ITForm002 updatedITForm002 = itform002Service.updateITForm002(modifiedITForm002, loginUserID);
		return new ResponseEntity<>(updatedITForm002 , HttpStatus.OK);
	}
    
  //---------------------------ITFORM002-Attorney----------------------------------------------------------
    @ApiOperation(response = ITForm002Att.class, value = "Get a ITForm002-Attorney") // label for swagger 
	@GetMapping("/attorney") 
	public ResponseEntity<?> getITForm002AttById(@RequestParam String inquiryNo, 
												@RequestParam Long classID, 
												@RequestParam String language,
												@RequestParam String itFormNo,
												@RequestParam Long itFormID) {
    	ITForm002Att itForm002Att = itform002Service.getITForm002Att(inquiryNo, classID, language, itFormNo, itFormID);
    	return new ResponseEntity<>(itForm002Att, HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm002Att.class, value = "Create ITForm002-Attorney") // label for swagger
   	@PostMapping("/attorney")
   	public ResponseEntity<?> addITForm002Attorney(@Valid @RequestBody ITForm002Att newITForm002Att, @RequestParam String loginUserID) 
   			throws Exception {
   		ITForm002Att createdITForm002Att = itform002Service.createITForm002Att(newITForm002Att, loginUserID);
   		return new ResponseEntity<>(createdITForm002Att , HttpStatus.OK);
   	}
}