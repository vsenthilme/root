package com.iweb2b.api.integration.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iweb2b.api.integration.model.consignment.dto.Consignment;
import com.iweb2b.api.integration.model.consignment.dto.ConsignmentResponse;
import com.iweb2b.api.integration.model.consignment.dto.ConsignmentWebhook;
import com.iweb2b.api.integration.service.SoftDataUploadService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "SoftDataUpload" }, value = "SoftDataUpload Operations related to SoftDataUploadController") 
@SwaggerDefinition(tags = { @Tag(name = "SoftDataUpload", description = "Operations related to SoftDataUpload") })
@RequestMapping("/softDataUpload")
@RestController
public class SoftDataUploadController {

	@Autowired
	SoftDataUploadService softDataUploadService;

	/*
	 * Client Endpoints
	 */
	@ApiOperation(response = Consignment.class, value = "Create Consignment") // label for swagger
	@PostMapping("/order")
	public ResponseEntity<?> postConsignment(@Valid @RequestBody Consignment newConsignment,
			@RequestParam String loginUserID) throws Exception {
		ConsignmentResponse response = softDataUploadService.createConsignment(newConsignment, loginUserID);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@ApiOperation(response = Consignment.class, value = "Get All Consignment") // label for swagger
	@GetMapping()
	public ResponseEntity<?> getConsignments() throws Exception {
		List<Consignment> consignmentList = softDataUploadService.getConsignments();
		return new ResponseEntity<>(consignmentList, HttpStatus.OK);
	}
	
	@ApiOperation(response = Consignment.class, value = "Get Consignment") // label for swagger
	@GetMapping("/type")
	public ResponseEntity<?> getConsignmentsByStatus(@RequestParam String type) throws Exception {
		List<ConsignmentWebhook> consignmentList = softDataUploadService.getConsignmentByType(type);
		return new ResponseEntity<>(consignmentList, HttpStatus.OK);
	}
}
