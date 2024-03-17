package com.mnrclara.api.management.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.management.model.clientitform.AddClientItform;
import com.mnrclara.api.management.model.clientitform.ClientItform;
import com.mnrclara.api.management.model.clientitform.UpdateClientItform;
import com.mnrclara.api.management.service.ClientItformService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "ClientItform" }, value = "ClientItform Operations related to ClientItformController")
@SwaggerDefinition(tags = { @Tag(name = "ClientItform", description = "Operations related to ClientItform") })
@RequestMapping("/clientitform")
@RestController
public class ClientItformController {

	@Autowired
	ClientItformService clientItformService;

	@ApiOperation(response = ClientItform.class, value = "Get all ClientItform details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ClientItform> clientItformList = clientItformService.getClientItforms();
		return new ResponseEntity<>(clientItformList, HttpStatus.OK);
	}

	@ApiOperation(response = ClientItform.class, value = "Get a ClientItform") // label for swagger
	@GetMapping("/{clientitformId}")
	public ResponseEntity<?> getClientItform(@PathVariable Long clientitformId) {
		ClientItform clientitform = clientItformService.getClientItform(clientitformId);
		log.info("ClientItform : " + clientitform);
		return new ResponseEntity<>(clientitform, HttpStatus.OK);
	}

	@ApiOperation(response = ClientItform.class, value = "Create ClientItform") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addClientItform(@Valid @RequestBody AddClientItform newClientItform)
			throws IllegalAccessException, InvocationTargetException {
		ClientItform createdClientItform = clientItformService.createClientItform(newClientItform);
		return new ResponseEntity<>(createdClientItform, HttpStatus.OK);
	}

	@ApiOperation(response = ClientItform.class, value = "Update ClientItform") // label for swagger
	@PatchMapping("/{clientitformId}")
	public ResponseEntity<?> patchClientItform(@PathVariable Long clientitformId,
			@Valid @RequestBody UpdateClientItform updateClientItform)
			throws IllegalAccessException, InvocationTargetException {
		ClientItform updatedClientItform = clientItformService.updateClientItform(clientitformId, updateClientItform);
		return new ResponseEntity<>(updatedClientItform, HttpStatus.OK);
	}

	@ApiOperation(response = ClientItform.class, value = "Delete ClientItform") // label for swagger
	@DeleteMapping("/{clientitformId}")
	public ResponseEntity<?> deleteClientItform(@PathVariable Long clientitformId) {
		clientItformService.deleteClientItform(clientitformId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}