package com.iweb2b.api.integration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iweb2b.api.integration.model.tracking.ConsignmentTracking;
import com.iweb2b.api.integration.service.ConsignmentTrackingService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "ConsignmentTracking" }, value = "ConsignmentTracking Operations related to ConsignmentTrackingController") // label for
																										// swagger
@SwaggerDefinition(tags = { @Tag(name = "ConsignmentTracking", description = "Operations related to ConsignmentTracking") })
@RequestMapping("/consignmentTracking")
@RestController
public class ConsignmentTrackingController {

	@Autowired
	ConsignmentTrackingService consignmentTrackingService;
	
	// To fetch shipment tracking for client
	@ApiOperation(response = ConsignmentTracking.class, value = "Get a ConsignmentTracking") // label for swagger
	@GetMapping("/{referenceNumber}/shipment")
	public ResponseEntity<?> getConsignmentTracking(@PathVariable String referenceNumber) {
		ConsignmentTracking dbConsignmentTracking = consignmentTrackingService.getConsignmentTracking(referenceNumber);
//		log.info("ConsignmentTracking : " + dbConsignmentTracking);
		return new ResponseEntity<>(dbConsignmentTracking, HttpStatus.OK);
	}
}
