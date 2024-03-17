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

import com.mnrclara.api.management.model.clientmatter.AddClientMatter;
import com.mnrclara.api.management.model.clientmatter.ClientMatter;
import com.mnrclara.api.management.model.clientmatter.UpdateClientMatter;
import com.mnrclara.api.management.service.ClientMatterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "ClientMatter" }, value = "ClientMatter Operations related to ClientMatterController")
@SwaggerDefinition(tags = { @Tag(name = "ClientMatter", description = "Operations related to ClientMatter") })
@RequestMapping("/clientmatter")
@RestController
public class ClientMatterController {

	@Autowired
	ClientMatterService clientMatterService;

	@ApiOperation(response = ClientMatter.class, value = "Get all ClientMatter details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ClientMatter> clientMatterList = clientMatterService.getClientMatters();
		return new ResponseEntity<>(clientMatterList, HttpStatus.OK);
	}

	@ApiOperation(response = ClientMatter.class, value = "Get a ClientMatter") // label for swagger
	@GetMapping("/{clientmatterId}")
	public ResponseEntity<?> getClientMatter(@PathVariable Long clientmatterId) {
		ClientMatter clientmatter = clientMatterService.getClientMatter(clientmatterId);
		log.info("ClientMatter : " + clientmatter);
		return new ResponseEntity<>(clientmatter, HttpStatus.OK);
	}

	@ApiOperation(response = ClientMatter.class, value = "Create ClientMatter") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addClientMatter(@Valid @RequestBody AddClientMatter newClientMatter)
			throws IllegalAccessException, InvocationTargetException {
		ClientMatter createdClientMatter = clientMatterService.createClientMatter(newClientMatter);
		return new ResponseEntity<>(createdClientMatter, HttpStatus.OK);
	}

	@ApiOperation(response = ClientMatter.class, value = "Update ClientMatter") // label for swagger
	@PatchMapping("/{clientmatterId}")
	public ResponseEntity<?> patchClientMatter(@PathVariable Long clientmatterId,
			@Valid @RequestBody UpdateClientMatter updateClientMatter)
			throws IllegalAccessException, InvocationTargetException {
		ClientMatter updatedClientMatter = clientMatterService.updateClientMatter(clientmatterId, updateClientMatter);
		return new ResponseEntity<>(updatedClientMatter, HttpStatus.OK);
	}

	@ApiOperation(response = ClientMatter.class, value = "Delete ClientMatter") // label for swagger
	@DeleteMapping("/{clientmatterId}")
	public ResponseEntity<?> deleteClientMatter(@PathVariable Long clientmatterId) {
		clientMatterService.deleteClientMatter(clientmatterId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}