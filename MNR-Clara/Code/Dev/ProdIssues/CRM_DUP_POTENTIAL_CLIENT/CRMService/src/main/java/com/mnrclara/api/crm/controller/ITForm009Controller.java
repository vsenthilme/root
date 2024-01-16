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

import com.mnrclara.api.crm.model.itform.ITForm009;
import com.mnrclara.api.crm.service.ITForm009Service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"ITForm009"}, value = "ITForm009 Operations related to ITForm009Controller") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ITForm009",description = "Operations related to ITForm009")})
@RequestMapping("/itform009")
@RestController
public class ITForm009Controller {
	
	@Autowired
	ITForm009Service itForm009Service;
	
    @ApiOperation(response = ITForm009.class, value = "Get all ITForm009 details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ITForm009> itForm009List = itForm009Service.getITForm009s();
		return new ResponseEntity<>(itForm009List, HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm009.class, value = "Get a ITForm009") // label for swagger 
   	@GetMapping("/id") 
   	public ResponseEntity<?> getITForm009ById(@RequestParam String language,
   												@RequestParam Long classID, 
   												@RequestParam String matterNumber, 
   												@RequestParam String clientId,
   												@RequestParam String itFormNo,
   												@RequestParam Long itFormID) {
       	ITForm009 itform009 = itForm009Service.getITForm009(language, classID, matterNumber, clientId, itFormNo, itFormID);
       	log.info("ITForm009 : " + itform009);
       	return new ResponseEntity<>(itform009, HttpStatus.OK);
   	}
    
    @ApiOperation(response = ITForm009.class, value = "Create ITForm009") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addITForm009(@Valid @RequestBody ITForm009 newITForm009, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ITForm009 createdITForm009 = itForm009Service.createITForm009(newITForm009, loginUserID);
		return new ResponseEntity<>(createdITForm009 , HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm009.class, value = "Update ITForm009") // label for swagger
	@PatchMapping("")
	public ResponseEntity<?> updateITForm009(@RequestBody ITForm009 modifiedITForm009, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ITForm009 updatedITForm009 = itForm009Service.updateITForm009(modifiedITForm009, loginUserID);
		return new ResponseEntity<>(updatedITForm009 , HttpStatus.OK);
	}
}