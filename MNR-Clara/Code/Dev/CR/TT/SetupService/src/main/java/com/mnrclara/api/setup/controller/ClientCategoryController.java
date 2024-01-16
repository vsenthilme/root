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

import com.mnrclara.api.setup.model.clientcategory.AddClientCategory;
import com.mnrclara.api.setup.model.clientcategory.ClientCategory;
import com.mnrclara.api.setup.model.clientcategory.UpdateClientCategory;
import com.mnrclara.api.setup.service.ClientCategoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"ClientCategory"}, value = "ClientCategory Operations related to ClientCategoryController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ClientCategory",description = "Operations related to ClientCategory")})
@RequestMapping("/clientCategory")
@RestController
public class ClientCategoryController {
	
	@Autowired
	ClientCategoryService clientCategoryService;
	
    @ApiOperation(response = ClientCategory.class, value = "Get all ClientCategory details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ClientCategory> clientCategoryList = clientCategoryService.getClientCategories();
		return new ResponseEntity<>(clientCategoryList, HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientCategory.class, value = "Get a ClientCategory") // label for swagger 
	@GetMapping("/{clientCategoryId}")
	public ResponseEntity<?> getClientCategory(@PathVariable Long clientCategoryId) {
    	ClientCategory clientcategory = clientCategoryService.getClientCategory(clientCategoryId);
    	log.info("ClientCategory : " + clientcategory);
		return new ResponseEntity<>(clientcategory, HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientCategory.class, value = "Create ClientCategory") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postClientCategory(@Valid @RequestBody AddClientCategory newClientCategory, 
			@RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ClientCategory createdClientCategory = clientCategoryService.createClientCategory(newClientCategory, loginUserID);
		return new ResponseEntity<>(createdClientCategory , HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientCategory.class, value = "Update ClientCategory") // label for swagger
    @PatchMapping("/{clientCategoryId}")
	public ResponseEntity<?> patchClientCategory(@PathVariable Long clientCategoryId, @RequestParam String loginUserID,
			@Valid @RequestBody UpdateClientCategory updateClientCategory) 
			throws IllegalAccessException, InvocationTargetException {
		ClientCategory updatedClientCategory = clientCategoryService.updateClientCategory(clientCategoryId, loginUserID, updateClientCategory);
		return new ResponseEntity<>(updatedClientCategory , HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientCategory.class, value = "Delete ClientCategory") // label for swagger
	@DeleteMapping("/{clientCategoryId}")
	public ResponseEntity<?> deleteClientCategory(@PathVariable Long clientCategoryId, @RequestParam String loginUserID) {
    	clientCategoryService.deleteClientCategory(clientCategoryId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}