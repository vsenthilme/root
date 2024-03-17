package com.mnrclara.api.management.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.management.model.clientemail.AddClientEmail;
import com.mnrclara.api.management.model.clientemail.ClientEmail;
import com.mnrclara.api.management.model.clientemail.SearchClientEmail;
import com.mnrclara.api.management.model.clientgeneral.ClientGeneral;
import com.mnrclara.api.management.service.ClientEmailService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "ClientEmail" }, value = "ClientEmail Operations related to ClientEmailController") // label for swagger
@SwaggerDefinition(tags = { @Tag(name = "ClientEmail", description = "Operations related to ClientEmail") })
@RequestMapping("/clientemail")
@RestController
public class ClientEmailController {

	@Autowired
	ClientEmailService clientEmailService;

	@ApiOperation(response = ClientEmail.class, value = "Get all ClientEmail details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ClientEmail> clientEmailList = clientEmailService.getClientEmails();
		return new ResponseEntity<>(clientEmailList, HttpStatus.OK);
	}

	@ApiOperation(response = ClientEmail.class, value = "Get a ClientEmail") // label for swagger
	@GetMapping("/{emailId}")
	public ResponseEntity<?> getClientEmail(@PathVariable Long emailId) {
		ClientEmail clientemail = clientEmailService.getClientEmail(emailId);
		log.info("emailId : " + clientemail);
		return new ResponseEntity<>(clientemail, HttpStatus.OK);
	}

	@ApiOperation(response = ClientGeneral.class, value = "Search Inquiry") // label for swagger
	@PostMapping("/findClientEmail")
	public List<ClientEmail> findClientEmail(@RequestBody SearchClientEmail searchClientEmail) throws ParseException {
		return clientEmailService.findClientEmails(searchClientEmail);
	}

	@ApiOperation(response = ClientEmail.class, value = "Create ClientEmail") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postClientEmail(@Valid @RequestBody AddClientEmail newClientEmail,
			@RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
		ClientEmail createdClientEmail = clientEmailService.createClientEmail(newClientEmail, loginUserID);
		return new ResponseEntity<>(createdClientEmail, HttpStatus.OK);
	}

	@ApiOperation(response = ClientEmail.class, value = "Delete ClientEmail") // label for swagger
	@DeleteMapping("/{emailId}")
	public ResponseEntity<?> deleteClientEmail(@PathVariable Long emailId) {
		clientEmailService.deleteClientEmail(emailId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}