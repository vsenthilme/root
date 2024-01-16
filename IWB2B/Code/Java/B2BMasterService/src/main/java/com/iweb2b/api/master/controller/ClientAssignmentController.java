package com.iweb2b.api.master.controller;

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

import com.iweb2b.api.master.model.clientassignment.AddClientAssignment;
import com.iweb2b.api.master.model.clientassignment.ClientAssignment;
import com.iweb2b.api.master.model.clientassignment.UpdateClientAssignment;
import com.iweb2b.api.master.service.ClientAssignmentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "ClientAssignment" }, value = "ClientAssignment Operations related to ClientAssignmentController")
@SwaggerDefinition(tags = { @Tag(name = "ClientAssignment", description = "Operations related to ClientAssignment") })
@RequestMapping("/clientAssignment")
@RestController
public class ClientAssignmentController {

	@Autowired
	ClientAssignmentService clientAssignmentService;

	@ApiOperation(response = ClientAssignment.class, value = "Get all ClientAssignment details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ClientAssignment> clientAssignmentList = clientAssignmentService.getClientAssignment();
		return new ResponseEntity<>(clientAssignmentList, HttpStatus.OK);
	}

	@ApiOperation(response = ClientAssignment.class, value = "Get a ClientAssignment") // label for swagger
	@GetMapping("/{clientAssignmentId}")
	public ResponseEntity<?> getClientAssignment(@PathVariable String clientAssignmentId) {
		ClientAssignment dbClientAssignment = clientAssignmentService.getClientAssignment(clientAssignmentId);
		log.info("ClientAssignment : " + dbClientAssignment);
		return new ResponseEntity<>(dbClientAssignment, HttpStatus.OK);
	}

	@ApiOperation(response = ClientAssignment.class, value = "Create ClientAssignment") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postClientAssignment(@Valid @RequestBody AddClientAssignment newClientAssignment,
			@RequestParam String loginUserID) throws Exception {
		ClientAssignment createdClientAssignment = clientAssignmentService.createClientAssignment(newClientAssignment, loginUserID);
		return new ResponseEntity<>(createdClientAssignment, HttpStatus.OK);
	}

	@ApiOperation(response = ClientAssignment.class, value = "Update ClientAssignment") // label for swagger
	@PatchMapping("/{clientAssignment}")
	public ResponseEntity<?> patchClientAssignment(@PathVariable String clientAssignment,
			@RequestParam String loginUserID,
			@Valid @RequestBody UpdateClientAssignment updateClientAssignment)
			throws IllegalAccessException, InvocationTargetException {
		ClientAssignment updatedClientAssignment = clientAssignmentService.updateClientAssignment(clientAssignment, loginUserID,
				updateClientAssignment);
		return new ResponseEntity<>(updatedClientAssignment, HttpStatus.OK);
	}

	@ApiOperation(response = ClientAssignment.class, value = "Delete ClientAssignment") // label for swagger
	@DeleteMapping("/{clientAssignment}")
	public ResponseEntity<?> deleteClientAssignment(@PathVariable String clientAssignment, @RequestParam String loginUserID) {
		clientAssignmentService.deleteClientAssignment(clientAssignment, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
