package com.mnrclara.api.setup.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.setup.model.expirationdoctype.AddExpirationDocType;
import com.mnrclara.api.setup.model.expirationdoctype.ExpirationDocType;
import com.mnrclara.api.setup.model.expirationdoctype.UpdateExpirationDocType;
import com.mnrclara.api.setup.service.ExpirationDocTypeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"ExpirationDocType"}, value = "ExpirationDocType  Operations related to ExpirationDocTypeController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ExpirationDocType ",description = "Operations related to ExpirationDocType ")})
@RequestMapping("/expirationdoctype")
@RestController
public class ExpirationDocTypeController {
	
	@Autowired
	ExpirationDocTypeService expirationdoctypeService;
	
    @ApiOperation(response = ExpirationDocType.class, value = "Get all ExpirationDocType details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ExpirationDocType> expirationdoctypeList = expirationdoctypeService.getExpirationDocTypes();
		return new ResponseEntity<>(expirationdoctypeList, HttpStatus.OK); 
	}
    
    @ApiOperation(response = ExpirationDocType.class, value = "Get a ExpirationDocType") // label for swagger 
	@GetMapping("/{documentType}")
	public ResponseEntity<?> getExpirationDocType(@PathVariable String documentType, @RequestParam String languageId, 
			@RequestParam Long classId) {
    	ExpirationDocType expirationdoctype = expirationdoctypeService.getExpirationDocType(languageId, classId, documentType);
    	log.info("ExpirationDocType : " + expirationdoctype);
		return new ResponseEntity<>(expirationdoctype, HttpStatus.OK);
	}
    
    @ApiOperation(response = ExpirationDocType.class, value = "Create ExpirationDocType") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postExpirationDocType(@Valid @RequestBody AddExpirationDocType newExpirationDocType, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ExpirationDocType createdExpirationDocType = expirationdoctypeService.createExpirationDocType(newExpirationDocType, loginUserID);
		return new ResponseEntity<>(createdExpirationDocType , HttpStatus.OK);
	}
    
    @ApiOperation(response = ExpirationDocType.class, value = "Update ExpirationDocType") // label for swagger
    @PatchMapping("/{documentType}")
	public ResponseEntity<?> patchExpirationDocType(@PathVariable String documentType, 
			@Valid @RequestBody UpdateExpirationDocType updateExpirationDocType, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ExpirationDocType createdExpirationDocType = expirationdoctypeService.updateExpirationDocType(documentType, updateExpirationDocType, loginUserID);
		return new ResponseEntity<>(createdExpirationDocType , HttpStatus.OK);
	}
    
    @ApiOperation(response = ExpirationDocType.class, value = "Delete ExpirationDocType") // label for swagger
	@DeleteMapping("/{documentType}")
	public ResponseEntity<?> deleteExpirationDocType(@PathVariable String documentType, @RequestParam String languageId, 
			@RequestParam Long classId, @RequestParam String loginUserID) {
    	expirationdoctypeService.deleteExpirationDocType(languageId, classId, documentType, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}