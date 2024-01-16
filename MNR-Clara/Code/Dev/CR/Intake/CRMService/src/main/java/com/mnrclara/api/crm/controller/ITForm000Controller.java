package com.mnrclara.api.crm.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.crm.model.itform.ITForm000;
import com.mnrclara.api.crm.service.MongoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"ITForm000"}, value = "ITForm001 Operations related to ITForm000Controller") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ITForm001",description = "Operations related to ITForm001")})
@RequestMapping("/itform000")
@RestController
public class ITForm000Controller {
	
	@Autowired
	MongoService mongoService;
	
    @ApiOperation(response = ITForm000.class, value = "Get a ITForm000") // label for swagger 
	@GetMapping("/id") 
	public ResponseEntity<?> getITForm001ById(@RequestParam String inquiryNo, 
												@RequestParam Long classID, 
												@RequestParam String language,
												@RequestParam String itFormNo,
												@RequestParam Long itFormID) {
    	String id = inquiryNo + ":" + classID + ":" + language + ":" + itFormNo + ":" + itFormID;
    	Optional<ITForm000> optItform000 = mongoService.getITForm000(id);
    	if (!optItform000.isEmpty()) {
    		log.info("ITForm000 : " + optItform000.get());
        	return new ResponseEntity<>(optItform000.get(), HttpStatus.OK);
    	}
		return null;
	}
}