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

import com.mnrclara.api.crm.model.itform.ITForm010;
import com.mnrclara.api.crm.service.ITForm010Service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"ITForm010"}, value = "ITForm010 Operations related to ITForm010Controller") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ITForm010",description = "Operations related to ITForm010")})
@RequestMapping("/itform010")
@RestController
public class ITForm010Controller {
	
	@Autowired
	ITForm010Service itForm010Service;
	
    @ApiOperation(response = ITForm010.class, value = "Get all ITForm010 details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ITForm010> itForm010List = itForm010Service.getITForm010s();
		return new ResponseEntity<>(itForm010List, HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm010.class, value = "Get a ITForm010") // label for swagger 
   	@GetMapping("/id") 
   	public ResponseEntity<?> getITForm010ById(@RequestParam String language,
   												@RequestParam Long classID, 
   												@RequestParam String matterNumber, 
   												@RequestParam String clientId,
   												@RequestParam String itFormNo,
   												@RequestParam Long itFormID) {
       	ITForm010 itform010 = itForm010Service.getITForm010(language, classID, matterNumber, clientId, itFormNo, itFormID);
       	log.info("ITForm010 : " + itform010);
       	return new ResponseEntity<>(itform010, HttpStatus.OK);
   	}
    
    @ApiOperation(response = ITForm010.class, value = "Create ITForm010") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addITForm010(@Valid @RequestBody ITForm010 newITForm010, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ITForm010 createdITForm010 = itForm010Service.createITForm010(newITForm010, loginUserID);
		return new ResponseEntity<>(createdITForm010 , HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm010.class, value = "Update ITForm010") // label for swagger
	@PatchMapping("")
	public ResponseEntity<?> updateITForm010(@RequestBody ITForm010 modifiedITForm010, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ITForm010 updatedITForm010 = itForm010Service.updateITForm010(modifiedITForm010, loginUserID);
		return new ResponseEntity<>(updatedITForm010 , HttpStatus.OK);
	}
}