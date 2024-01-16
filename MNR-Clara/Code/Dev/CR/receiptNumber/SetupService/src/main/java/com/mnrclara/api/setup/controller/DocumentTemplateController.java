package com.mnrclara.api.setup.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.setup.model.documenttemplate.AddDocumentTemplate;
import com.mnrclara.api.setup.model.documenttemplate.DocumentTemplate;
import com.mnrclara.api.setup.model.documenttemplate.DocumentTemplateCompositeKey;
import com.mnrclara.api.setup.model.documenttemplate.UpdateDocumentTemplate;
import com.mnrclara.api.setup.service.DocumentTemplateService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"DocumentTemplate"}, value = "DocumentTemplate Operations related to DocumentTemplateController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "DocumentTemplate",description = "Operations related to DocumentTemplate")})
@RequestMapping("/documentTemplate")
@RestController
public class DocumentTemplateController {
	
	@Autowired
	DocumentTemplateService documentTemplateService;
	
    @ApiOperation(response = DocumentTemplate.class, value = "Get all DocumentTemplate details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<DocumentTemplate> documentTemplateList = documentTemplateService.getDocumentTemplates();
		return new ResponseEntity<>(documentTemplateList, HttpStatus.OK);
	}
    
    @ApiOperation(response = DocumentTemplate.class, value = "Get a DocumentTemplate") // label for swagger 
	@GetMapping("/{documentNumber}")
	public ResponseEntity<?> getDocumentTemplate(@PathVariable String documentNumber) {
    	DocumentTemplate documentTemplate = documentTemplateService.getDocumentTemplate(documentNumber);
    	log.info("DocumentTemplate : " + documentTemplate);
		return new ResponseEntity<>(documentTemplate, HttpStatus.OK);
	}
    
    @ApiOperation(response = DocumentTemplate.class, value = "Create DocumentTemplate") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addDocumentTemplate(@Valid @RequestBody AddDocumentTemplate newDocumentTemplate, 
			@RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		DocumentTemplate createdDocumentTemplate = documentTemplateService.createDocumentTemplate(newDocumentTemplate, loginUserID);
		return new ResponseEntity<>(createdDocumentTemplate , HttpStatus.OK);
	}
    
    @ApiOperation(response = DocumentTemplate.class, value = "Update DocumentTemplate") // label for swagger
    @PatchMapping("")
	public ResponseEntity<?> patchDocumentTemplate(@Valid @RequestBody UpdateDocumentTemplate updateDocumentTemplate, 
			@RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		DocumentTemplate updatedDocumentTemplate = documentTemplateService.updateDocumentTemplate(updateDocumentTemplate, loginUserID);
		return new ResponseEntity<>(updatedDocumentTemplate , HttpStatus.OK);
	}
    
    @ApiOperation(response = DocumentTemplate.class, value = "Delete DocumentTemplate") // label for swagger
	@DeleteMapping("")
	public ResponseEntity<?> deleteDocumentTemplate(@RequestBody DocumentTemplateCompositeKey key, @RequestParam String loginUserID) {
    	documentTemplateService.deleteDocumentTemplate(key, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}