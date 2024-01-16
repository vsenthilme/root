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

import com.mnrclara.api.setup.model.clienttype.AddClientType;
import com.mnrclara.api.setup.model.clienttype.ClientType;
import com.mnrclara.api.setup.model.clienttype.UpdateClientType;
import com.mnrclara.api.setup.service.ClientTypeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"ClientType"}, value = "ClientType Operations related to ClientTypeController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ClientType",description = "Operations related to ClientType")})
@RequestMapping("/clientType")
@RestController
public class ClientTypeController {
	
	@Autowired
	ClientTypeService clientTypeService;
	
    @ApiOperation(response = ClientType.class, value = "Get all ClientType details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ClientType> clientTypeList = clientTypeService.getClientTypes();
		return new ResponseEntity<>(clientTypeList, HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientType.class, value = "Get a ClientType") // label for swagger 
	@GetMapping("/{clientTypeId}")
	public ResponseEntity<?> getClientType(@PathVariable Long clientTypeId) {
    	ClientType clienttype = clientTypeService.getClientType(clientTypeId);
    	log.info("ClientType : " + clienttype);
		return new ResponseEntity<>(clienttype, HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientType.class, value = "Create ClientType") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addClientType(@Valid @RequestBody AddClientType newClientType, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ClientType createdClientType = clientTypeService.createClientType(newClientType, loginUserID);
		return new ResponseEntity<>(createdClientType , HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientType.class, value = "Update ClientType") // label for swagger
    @PatchMapping("/{clientTypeId}")
	public ResponseEntity<?> patchClientType(@PathVariable Long clientTypeId, @RequestParam String loginUserID,
			@Valid @RequestBody UpdateClientType updateClientType) 
			throws IllegalAccessException, InvocationTargetException {
		ClientType updatedClientType = clientTypeService.updateClientType(clientTypeId, loginUserID, updateClientType);
		return new ResponseEntity<>(updatedClientType , HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientType.class, value = "Delete ClientType") // label for swagger
	@DeleteMapping("/{clientTypeId}")
	public ResponseEntity<?> deleteClientType(@PathVariable Long clientTypeId, @RequestParam String loginUserID) {
    	clientTypeService.deleteClientType(clientTypeId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}