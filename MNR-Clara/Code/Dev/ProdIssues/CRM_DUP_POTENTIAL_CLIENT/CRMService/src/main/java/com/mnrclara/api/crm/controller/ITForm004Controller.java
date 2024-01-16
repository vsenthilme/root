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
import com.mnrclara.api.crm.model.itform.ITForm003;
import com.mnrclara.api.crm.model.itform.ITForm003Att;
import com.mnrclara.api.crm.model.itform.ITForm004;
import com.mnrclara.api.crm.model.itform.ITForm004Att;
import com.mnrclara.api.crm.service.ITForm004Service;
import com.mnrclara.api.crm.util.CommonUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"ITForm004"}, value = "ITForm004 Operations related to ITForm004Controller") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ITForm004",description = "Operations related to ITForm004")})
@RequestMapping("/itform004")
@RestController
public class ITForm004Controller {
	
	@Autowired
	ITForm004Service itform004Service;
	
    @ApiOperation(response = ITForm004.class, value = "Get all ITForm004 details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ITForm004> itform004List = itform004Service.getITForm004s();
		return new ResponseEntity<>(itform004List, HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm004.class, value = "Get a ITForm004") // label for swagger 
	@GetMapping("/id") 
	public ResponseEntity<?> getITForm004ById(@RequestParam String inquiryNo, 
												@RequestParam Long classID, 
												@RequestParam String language,
												@RequestParam String itFormNo,
												@RequestParam Long itFormID) {
    	ITForm004 itform001 = itform004Service.getITForm004(inquiryNo, classID, language, itFormNo, itFormID);
    	log.info("ITForm004 : " + itform001);
    	return new ResponseEntity<>(itform001, HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm004.class, value = "Create ITForm004") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addITForm004(@Valid @RequestBody ITForm004 newITForm004, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ITForm004 createdITForm004 = itform004Service.createITForm004(newITForm004, loginUserID);
		return new ResponseEntity<>(createdITForm004 , HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm004.class, value = "Update ITForm004") // label for swagger
	@PatchMapping("")
	public ResponseEntity<?> updateITForm004(@RequestBody ITForm004 modifiedITForm004, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ITForm004 updatedITForm004 = itform004Service.updateITForm004(modifiedITForm004, loginUserID);
		return new ResponseEntity<>(updatedITForm004 , HttpStatus.OK);
	}
    
  //---------------------------ITFORM004-Attorney----------------------------------------------------------
    @ApiOperation(response = ITForm004.class, value = "Get a ITForm004-Attorney") // label for swagger 
	@GetMapping("/attorney") 
	public ResponseEntity<?> getITForm004AttById(@RequestParam String inquiryNo, 
												@RequestParam Long classID, 
												@RequestParam String language,
												@RequestParam String itFormNo,
												@RequestParam Long itFormID) {
    	ITForm004Att itForm004Att = itform004Service.getITForm004Att(inquiryNo, classID, language, itFormNo, itFormID);
    	log.info("ITForm004Att : " + itForm004Att);
    	if (itForm004Att == null) {
    		itForm004Att = new ITForm004Att();
    		ITForm004 itform004 = itform004Service.getITForm004(inquiryNo, classID, language, itFormNo, itFormID);
    		log.info("ITForm004 : " + itform004);
    		BeanUtils.copyProperties(itform004, itForm004Att, CommonUtils.getNullPropertyNames(itform004));
    	}
    	return new ResponseEntity<>(itForm004Att, HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm004Att.class, value = "Create ITForm004-Attorney") // label for swagger
   	@PostMapping("/attorney")
   	public ResponseEntity<?> addITForm003Attorney(@Valid @RequestBody ITForm004Att newITForm004Att, @RequestParam String loginUserID) 
   			throws IllegalAccessException, InvocationTargetException {
   		ITForm004Att createdITForm003Att = itform004Service.createITForm004Att(newITForm004Att, loginUserID);
   		return new ResponseEntity<>(createdITForm003Att , HttpStatus.OK);
   	}
}