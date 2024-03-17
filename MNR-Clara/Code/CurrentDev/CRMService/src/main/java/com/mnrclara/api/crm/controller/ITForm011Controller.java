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

import com.mnrclara.api.crm.model.itform.ITForm011;
import com.mnrclara.api.crm.service.ITForm011Service;
import com.mnrclara.api.crm.service.MongoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"ITForm011"}, value = "ITForm011 Operations related to ITForm011Controller") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ITForm011",description = "Operations related to ITForm011")})
@RequestMapping("/itform011")
@RestController
public class ITForm011Controller {
	
	@Autowired
	ITForm011Service itForm011Service;
	
	@Autowired
	MongoService mongoService;
	
    @ApiOperation(response = ITForm011.class, value = "Get all ITForm011 details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ITForm011> itForm011List = itForm011Service.getITForm011s();
		return new ResponseEntity<>(itForm011List, HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm011.class, value = "Get a ITForm011") // label for swagger 
   	@GetMapping("/id") 
   	public ResponseEntity<?> getITForm011ById(@RequestParam String language,
   												@RequestParam Long classID, 
   												@RequestParam String matterNumber, 
   												@RequestParam String clientId,
   												@RequestParam String itFormNo,
   												@RequestParam Long itFormID) {
       	ITForm011 itform011 = itForm011Service.getITForm011(language, classID, matterNumber, clientId, itFormNo, itFormID);
       	log.info("ITForm011 : " + itform011);
       	return new ResponseEntity<>(itform011, HttpStatus.OK);
   	}
    
    @ApiOperation(response = ITForm011.class, value = "Create ITForm011") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addITForm011(@Valid @RequestBody ITForm011 newITForm011, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ITForm011 createdITForm011 = itForm011Service.createITForm011(newITForm011, loginUserID);
		return new ResponseEntity<>(createdITForm011 , HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm011.class, value = "Update ITForm011") // label for swagger
	@PatchMapping("")
	public ResponseEntity<?> updateITForm011(@RequestBody ITForm011 modifiedITForm011, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ITForm011 updatedITForm011 = itForm011Service.updateITForm011(modifiedITForm011, loginUserID);
		return new ResponseEntity<>(updatedITForm011 , HttpStatus.OK);
	}
}